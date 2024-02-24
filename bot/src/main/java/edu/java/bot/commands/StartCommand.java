package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.repository.in_memory.DBUsersLinks;
import edu.java.bot.repository.in_memory.DBUsersState;
import org.springframework.stereotype.Component;
import static edu.java.bot.repository.in_memory.DBUsersLinks.isUserRegistered;

@Component
public class StartCommand implements Command {

    public static final String NAME = "/start";
    public static final String DESCRIPTION = "зарегистрировать пользователя";
    public static final String SUCCESS = "Вы зарегистрировались в боте!";
    public static final String ALREADY_REGISTERED = "Вы уже зарегистрированы!";

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public String description() {
        return DESCRIPTION;
    }

    @Override
    public SendMessage handle(Update update) {

        long chatId = update.message().chat().id();
        if (isUserRegistered(chatId)) {
            return new SendMessage(chatId, ALREADY_REGISTERED);
        } else {
            DBUsersLinks.addUser(chatId);
            DBUsersState.addUser(chatId);
            return new SendMessage(chatId, SUCCESS);
        }
    }
}
