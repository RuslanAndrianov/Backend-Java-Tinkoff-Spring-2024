package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.repository.DatabaseUsers;
import edu.java.bot.repository.UsersState;

public class StartCommand implements Command {

    @Override
    public String name() {
        return "/start";
    }

    @Override
    public String description() {
        return "зарегистрировать пользователя";
    }

    @Override
    public SendMessage handle(Update update) {

        long chatId = update.message().chat().id();
        if (DatabaseUsers.isUserRegistered(chatId)) {
            return new SendMessage(chatId, "Вы уже зарегистрированы!");
        } else {
            DatabaseUsers.addUser(chatId);
            UsersState.addUser(chatId);
            return new SendMessage(chatId, "Вы зарегистрировались в боте!");
        }
    }
}
