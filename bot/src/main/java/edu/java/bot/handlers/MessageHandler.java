package edu.java.bot.handlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.TrackCommand;
import edu.java.bot.commands.UntrackCommand;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import static edu.java.bot.configuration.CommandsConfig.COMMANDS;
import static edu.java.bot.repository.in_memory.UserState.TRACK;
import static edu.java.bot.repository.in_memory.UserState.UNTRACKED;
import static edu.java.bot.repository.in_memory.DBUsersState.getUserState;

@Component
public class MessageHandler {

    private final TelegramBot telegramBot;

    MessageHandler(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public UpdatesListener updatesListener() {
        return list -> {
            list.forEach(MessageHandler.this::messageHandler);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        };
    }

    private void messageHandler(@NotNull Update update) {
        long chatId = update.message().chat().id();
        String text = update.message().text();

        if (getUserState(chatId) == TRACK) {
            telegramBot.execute(TrackCommand.trackURL(update));
        }

        if (getUserState(chatId) == UNTRACKED) {
            telegramBot.execute(UntrackCommand.untrackURL(update));
        }

        for (Command command : COMMANDS) {
            if (text.equals(command.name())) {
                telegramBot.execute(command.handle(update));
            }
        }
    }
}
