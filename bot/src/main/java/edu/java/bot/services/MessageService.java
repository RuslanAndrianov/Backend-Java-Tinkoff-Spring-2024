package edu.java.bot.services;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.ChatState;
import edu.java.bot.clients.ScrapperClient;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.HelpCommand;
import edu.java.bot.commands.ListCommand;
import edu.java.bot.commands.StartCommand;
import edu.java.bot.commands.TrackCommand;
import edu.java.bot.commands.UntrackCommand;
import edu.java.bot.repository.in_memory.DBChatStates;
import edu.shared_dto.request_dto.AddLinkRequest;
import edu.shared_dto.request_dto.RemoveLinkRequest;
import java.net.URI;
import java.net.URISyntaxException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import static edu.ChatState.REGISTERED;
import static edu.ChatState.TRACK;
import static edu.ChatState.UNREGISTERED;
import static edu.ChatState.UNTRACKED;
import static edu.java.bot.commands.Answers.ANSWER_TO_UNREGISTERED_USER;
import static edu.java.bot.commands.Answers.INVALID_COMMAND;
import static edu.java.bot.commands.Answers.USE_TRACK_OR_UNTRACK;
import static edu.java.bot.configs.CommandsConfig.commands;
import static edu.utils.URLValidator.isValidURL;

@RequiredArgsConstructor
@Service
@Slf4j
@SuppressWarnings({"ReturnCount", "AvoidStarImport",
    "MissingSwitchDefault", "MultipleStringLiterals"})
public class MessageService {

    private final TelegramBot telegramBot;
    private final ScrapperClient scrapperClient;

    // TODO : featuring and refactoring

    // TL;DR :
    // 0. Ищем индексы команд в commands (чтобы не привязываться к MagicNumber
    //    и добавлять новые команды в commands в любом порядке).
    // 1. Проверяем сообщение на совпадение с командами.
    // 2. Проверяем, является ли сообщение валидным URL.
    //    Если невалидный URL, то пишем, что не смогли распознать команду.
    // 3. Если состояние чата TRACK, то пытаемся добавить в отслеживание валидный URL.
    // 4. Если состояние чата UNTRACKED, то пытаемся убрать из отслеживания валидный URL.
    // До обработки сообщения загружаем состояние чата из кэш-файла.
    // После обработки сообщения сохраняем состояние чата в кэш-файл.

    public void handleMessage(@NotNull Update update) {
        long chatId = update.message().chat().id();
        String chatMessage = update.message().text();
        Object scrapperResponse;

        ChatState chatState = DBChatStates.loadChatStateFromCache(chatId);
        DBChatStates.setChatState(chatId, chatState);

        // 0.
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

        // 1.
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

            case StartCommand.NAME:
                log.info(StartCommand.NAME + " command at chat " + chatId);
                scrapperResponse = scrapperClient.registerChat(chatId);
                telegramBot.execute(startCommand.handle(update, scrapperResponse));

                DBChatStates.addChat(chatId);
                DBChatStates.saveChatStateToCache(chatId);
                return;

            case TrackCommand.NAME:
                log.info(TrackCommand.NAME + " command at chat " + chatId);
                if (DBChatStates.getChatState(chatId) != UNREGISTERED) {
                    telegramBot.execute(trackCommand.trackURL(update));

                    DBChatStates.setChatState(chatId, TRACK);
                    DBChatStates.saveChatStateToCache(chatId);
                } else {
                    telegramBot.execute(new SendMessage(chatId, ANSWER_TO_UNREGISTERED_USER));
                }
                return;

            case UntrackCommand.NAME:
                log.info(UntrackCommand.NAME + " command at chat " + chatId);
                if (DBChatStates.getChatState(chatId) != UNREGISTERED) {
                    telegramBot.execute(untrackCommand.untrackURL(update));

                    DBChatStates.setChatState(chatId, UNTRACKED);
                    DBChatStates.saveChatStateToCache(chatId);
                } else {
                    telegramBot.execute(new SendMessage(chatId, ANSWER_TO_UNREGISTERED_USER));
                }
                return;
        }

        // 2-3.
        if (!isValidURL(chatMessage)) {
            log.error("Invalid command at chat " + chatId);
            telegramBot.execute(new SendMessage(chatId, INVALID_COMMAND));

            DBChatStates.setChatState(chatId, REGISTERED);
            DBChatStates.saveChatStateToCache(chatId);
            return;
        }

        // 4.
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

        // 5.
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
