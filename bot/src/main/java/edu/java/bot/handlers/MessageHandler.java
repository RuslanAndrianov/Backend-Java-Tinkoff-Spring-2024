package edu.java.bot.handlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.TrackCommand;
import edu.java.bot.commands.UntrackCommand;
import edu.java.bot.configuration.CommandsConfig;
import edu.java.bot.model.UserState;
import edu.java.bot.repository.UsersState;
import org.springframework.stereotype.Component;

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

    private void messageHandler(Update update) {
        long chatId = update.message().chat().id();
        String text = update.message().text();

        if (UsersState.getUserState(chatId) == UserState.TRACK) {
            telegramBot.execute(TrackCommand.trackURL(update));
        }

        if (UsersState.getUserState(chatId) == UserState.UNTRACK) {
            telegramBot.execute(UntrackCommand.untrackURL(update));
        }

        for (int i = 0; i < CommandsConfig.COMMANDS.size(); i++) {
            Command command = CommandsConfig.COMMANDS.get(i);
            if (text.equals(command.name())) {
                telegramBot.execute(command.handle(update));
            }
        }
    }
}
