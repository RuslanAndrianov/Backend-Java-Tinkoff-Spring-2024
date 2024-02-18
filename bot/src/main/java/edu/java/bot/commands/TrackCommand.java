package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.model.UserState;
import edu.java.bot.repository.DatabaseUsers;
import edu.java.bot.repository.UsersState;
import static edu.java.bot.utils.URLValidator.isValidURL;

public class TrackCommand implements Command {

    @Override
    public String name() {
        return "/track";
    }

    @Override
    public String description() {
        return "начать отслеживание ссылки";
    }

    @Override
    public SendMessage handle(Update update) {

        long chatId = update.message().chat().id();
        if (!DatabaseUsers.isUserRegistered(chatId)) {
            return new SendMessage(chatId, "Сначала зарегистрируйтесь с помощью команды /start");
        }
        UsersState.setUserState(chatId, UserState.TRACK);
        return new SendMessage(chatId, "Введите URL для отслеживания:");
    }

    public static SendMessage trackURL(Update update) {
        long chatId = update.message().chat().id();
        String text = update.message().text();

        UsersState.setUserState(chatId, UserState.REGISTERED);

        if (DatabaseUsers.isUserHasLink(chatId, text)) {
            return new SendMessage(chatId, "Ссылка уже отслеживается!");
        }

        if (isValidURL(text)) {
            DatabaseUsers.addLink(chatId, text);
            return new SendMessage(chatId, "Ссылка зарегистрирована!");
        }
        return new SendMessage(chatId, "Ошибка при вводе URL!");
    }
}
