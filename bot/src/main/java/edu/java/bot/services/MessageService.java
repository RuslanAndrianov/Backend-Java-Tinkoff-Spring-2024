package edu.java.bot.services;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.TrackCommand;
import edu.java.bot.commands.UntrackCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import static edu.java.bot.configs.CommandsConfig.commands;
import static edu.java.bot.repository.in_memory.DBUsersState.getUserState;
import static edu.shared_dto.ChatState.TRACK;
import static edu.shared_dto.ChatState.UNTRACKED;

@RequiredArgsConstructor
@Service
@Slf4j
public class MessageService {

    private final TelegramBot telegramBot;

    public void handleMessage(@NotNull Update update) {
        long chatId = update.message().chat().id();
        String text = update.message().text();

        if (getUserState(chatId) == TRACK) {
            telegramBot.execute(TrackCommand.trackURL(update));
        }

        if (getUserState(chatId) == UNTRACKED) {
            telegramBot.execute(UntrackCommand.untrackURL(update));
        }

        for (Command command : commands) {
            if (text.equals(command.name())) {
                telegramBot.execute(command.handle(update));
            }
        }

        log.info("Some new logic");
    }
}
