package edu.java.bot.services;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.TrackCommand;
import edu.java.bot.commands.UntrackCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import static edu.java.bot.configs.CommandsConfig.commands;
import static edu.java.bot.repository.in_memory.Users.getUserState;
import static edu.shared_dto.ChatState.TRACK;
import static edu.shared_dto.ChatState.UNTRACKED;

@RequiredArgsConstructor
@Service
@Slf4j
@SuppressWarnings("ReturnCount")
public class MessageService {

    private final TelegramBot telegramBot;

    public void handleMessage(@NotNull Update update) {
        long chatId = update.message().chat().id();
        String chatMessage = update.message().text();
        int trackCommandIndex = -1;
        int untrackCommandIndex = -1;

        for (Command command : commands) {
            if (command.getClass().equals(TrackCommand.class)) {
                trackCommandIndex = commands.indexOf(command);
                continue;
            }
            if (command.getClass().equals(UntrackCommand.class)) {
                untrackCommandIndex = commands.indexOf(command);
            }
        }

        TrackCommand trackCommand = (TrackCommand) commands.get(trackCommandIndex);
        UntrackCommand untrackCommand = (UntrackCommand) commands.get(untrackCommandIndex);

        if (getUserState(chatId) == TRACK) {
            String logMsg = String.format("Tracking %s at chat %d", chatMessage, chatId);
            log.info(logMsg);
            telegramBot.execute(trackCommand.trackURL(update));
            return;
        }

        if (getUserState(chatId) == UNTRACKED) {
            String logMsg = String.format("Untracking %s at chat %d", chatMessage, chatId);
            log.info(logMsg);
            telegramBot.execute(untrackCommand.untrackURL(update));
            return;
        }

        for (Command command : commands) {
            if (chatMessage.equals(command.name())) {
                log.info(command.name() + " command at chat " + chatId);
                telegramBot.execute(command.handle(update));
                return;
            }
        }

        log.error("Invalid command at chat " + chatId);
        telegramBot.execute(new SendMessage(chatId, "Не удалось распознать команду!"));
    }
}
