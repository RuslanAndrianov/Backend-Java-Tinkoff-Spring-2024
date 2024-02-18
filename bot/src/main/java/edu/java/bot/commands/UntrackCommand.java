package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.model.UserState;
import edu.java.bot.repository.DatabaseUsers;
import edu.java.bot.repository.UsersState;
import static edu.java.bot.utils.URLValidator.isValidURL;

public class UntrackCommand implements Command {

    @Override
    public String name() {
        return "/untrack";
    }

    @Override
    public String description() {
        return "прекратить отслеживание ссылки";
    }

    @Override
    public SendMessage handle(Update update) {

        long chatId = update.message().chat().id();
        if (!DatabaseUsers.isUserRegistered(chatId)) {
            return new SendMessage(chatId, "Сначала зарегистрируйтесь с помощью команды /start");
        }
        UsersState.setUserState(chatId, UserState.UNTRACK);
        return new SendMessage(chatId, "Введите URL для прекращения отслеживания:");
    }

    public static SendMessage untrackURL(Update update) {
        long chatId = update.message().chat().id();
        String text = update.message().text();

        UsersState.setUserState(chatId, UserState.REGISTERED);

        if (!DatabaseUsers.isUserHasLink(chatId, text)) {
            return new SendMessage(chatId, "Такая ссылка не отслеживается!");
        }

        if (isValidURL(text)) {
            DatabaseUsers.deleteLink(chatId, text);
            return new SendMessage(chatId, "Ссылка удалена!");
        }
        return new SendMessage(chatId, "Ошибка при вводе URL!");
    }
}
