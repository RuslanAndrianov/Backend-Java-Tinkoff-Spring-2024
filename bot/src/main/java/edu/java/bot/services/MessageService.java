package edu.java.bot.services;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.clients.ScrapperClient;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.HelpCommand;
import edu.java.bot.commands.ListCommand;
import edu.java.bot.commands.StartCommand;
import edu.java.bot.commands.TrackCommand;
import edu.java.bot.commands.UntrackCommand;
import edu.java.bot.repository.in_memory.ChatState;
import edu.java.bot.repository.in_memory.DBChatStates;
import edu.shared_dto.request_dto.AddLinkRequest;
import edu.shared_dto.request_dto.RemoveLinkRequest;
import java.net.URI;
import java.net.URISyntaxException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import static edu.java.bot.commands.Answers.ANSWER_TO_UNREGISTERED_USER;
import static edu.java.bot.commands.Answers.INVALID_COMMAND;
import static edu.java.bot.commands.Answers.USE_TRACK_OR_UNTRACK;
import static edu.java.bot.configs.CommandsConfig.commands;
import static edu.java.bot.repository.in_memory.ChatState.REGISTERED;
import static edu.java.bot.repository.in_memory.ChatState.TRACK;
import static edu.java.bot.repository.in_memory.ChatState.UNREGISTERED;
import static edu.java.bot.repository.in_memory.ChatState.UNTRACKED;
import static edu.utils.URLValidator.isValidURL;

@RequiredArgsConstructor
@Service
@Slf4j
@SuppressWarnings({"ReturnCount", "AvoidStarImport",
    "MissingSwitchDefault", "MultipleStringLiterals"})
public class MessageService {

    private final TelegramBot telegramBot;
    private final ScrapperClient scrapperClient;

    // Если лень разбираться в коде:
    // 1. Ищем индексы команд в commands (чтобы не привязываться к MagicNumber
    //    и можно было добавлять новые команды в commands в любом порядке).
    // 2. До обработки сообщения загружаем состояние чата из кэш-файла.
    // 3. Если пользователь пытается зарегистрироваться, то регистрируем его.
    // 4. Если пользователь незарегистрирован, то выдаем пользователю сообщение об ошибке.
    // 5. Проверяем сообщение на совпадение с остальными командами.
    // 6. Проверяем, является ли сообщение валидным URL.
    //    Если невалидный URL, то пишем, что не смогли распознать команду.
    // 7. Если состояние чата TRACK, то пытаемся добавить в отслеживание валидный URL.
    // 8. Если состояние чата UNTRACKED, то пытаемся убрать из отслеживания валидный URL.
    // 9. Если добрались до этого этапа, то это значит, что зарегистрированный пользователь
    //    прислал сообщение с валидным URL => говорим ему использовать команду /track или /untrack.
    // После обработки сообщения сохраняем состояние чата в кэш-файл.

    public void handleMessage(@NotNull Update update) {
        long chatId = update.message().chat().id();
        String chatMessage = update.message().text();
        Object scrapperResponse;

        // 1.
        int helpCmdIndex = getCommandIndex(HelpCommand.class);
        int listCmdIndex = getCommandIndex(ListCommand.class);
        int startCmdIndex = getCommandIndex(StartCommand.class);
        int trackCmdIndex = getCommandIndex(TrackCommand.class);
        int untrackCmdIndex = getCommandIndex(UntrackCommand.class);

        HelpCommand helpCommand = (HelpCommand) commands.get(helpCmdIndex);
        ListCommand listCommand = (ListCommand) commands.get(listCmdIndex);
        StartCommand startCommand = (StartCommand) commands.get(startCmdIndex);
        TrackCommand trackCommand = (TrackCommand) commands.get(trackCmdIndex);
        UntrackCommand untrackCommand = (UntrackCommand) commands.get(untrackCmdIndex);

        // 2.
        ChatState chatState = DBChatStates.loadChatStateFromCache(chatId);
        DBChatStates.setChatState(chatId, chatState);

        // 3.
        if (chatMessage.equals(StartCommand.NAME)) {
            log.info(StartCommand.NAME + " command at chat " + chatId);
            scrapperResponse = scrapperClient.registerChat(chatId);
            telegramBot.execute(startCommand.handle(update, scrapperResponse));
            DBChatStates.addChat(chatId);
            DBChatStates.saveChatStateToCache(chatId);
            return;
        }

        // 4.
        if (DBChatStates.getChatState(chatId) == UNREGISTERED) {
            telegramBot.execute(new SendMessage(chatId, ANSWER_TO_UNREGISTERED_USER));
            return;
        }

        // 5.
        switch (chatMessage) {
            case HelpCommand.NAME:
                log.info(HelpCommand.NAME + " command at chat " + chatId);
                telegramBot.execute(helpCommand.handle(update, null));
                DBChatStates.saveChatStateToCache(chatId);
                return;

            case ListCommand.NAME:
                log.info(ListCommand.NAME + " command at chat " + chatId);
                scrapperResponse = scrapperClient.getLinks(chatId);
                telegramBot.execute(listCommand.handle(update, scrapperResponse));
                DBChatStates.setChatState(chatId, REGISTERED);
                DBChatStates.saveChatStateToCache(chatId);
                return;

            case TrackCommand.NAME:
                log.info(TrackCommand.NAME + " command at chat " + chatId);
                telegramBot.execute(trackCommand.trackURL(update));
                DBChatStates.setChatState(chatId, TRACK);
                DBChatStates.saveChatStateToCache(chatId);
                return;

            case UntrackCommand.NAME:
                log.info(UntrackCommand.NAME + " command at chat " + chatId);
                telegramBot.execute(untrackCommand.untrackURL(update));
                DBChatStates.setChatState(chatId, UNTRACKED);
                DBChatStates.saveChatStateToCache(chatId);
                return;
        }

        // 6.
        if (!isValidURL(chatMessage)) {
            log.error("Invalid command at chat " + chatId);
            telegramBot.execute(new SendMessage(chatId, INVALID_COMMAND));

            DBChatStates.setChatState(chatId, REGISTERED);
            DBChatStates.saveChatStateToCache(chatId);
            return;
        }

        // 7.
        if (DBChatStates.getChatState(chatId) == TRACK) {
            try {
                scrapperResponse = scrapperClient.addLink(
                    chatId,
                    new AddLinkRequest(new URI(chatMessage))
                );
                telegramBot.execute(trackCommand.handle(update, scrapperResponse));
            } catch (URISyntaxException e) {
                log.error("Tracking error at chat " + chatId);
            }

            DBChatStates.setChatState(chatId, REGISTERED);
            DBChatStates.saveChatStateToCache(chatId);
            return;
        }

        // 8.
        if (DBChatStates.getChatState(chatId) == UNTRACKED) {
            try {
                scrapperResponse = scrapperClient.deleteLink(
                    chatId,
                    new RemoveLinkRequest(new URI(chatMessage))
                );
                telegramBot.execute(untrackCommand.handle(update, scrapperResponse));
            } catch (URISyntaxException e) {
                log.error("Untracking error at chat " + chatId);
            }

            DBChatStates.setChatState(chatId, REGISTERED);
            DBChatStates.saveChatStateToCache(chatId);
            return;
        }

        // 9.
        telegramBot.execute(new SendMessage(chatId, USE_TRACK_OR_UNTRACK));
    }

    private int getCommandIndex(Class cmdClass) {
        int index = -1;
        for (Command command : commands) {
            if (command.getClass().equals(cmdClass)) {
                index = commands.indexOf(command);
            }
        }
        return index;
    }
}
