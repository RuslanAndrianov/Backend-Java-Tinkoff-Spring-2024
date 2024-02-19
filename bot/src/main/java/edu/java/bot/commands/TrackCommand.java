package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.model.UserState;
import edu.java.bot.repository.in_memory.DBUsers;
import edu.java.bot.repository.in_memory.DBUsersState;
import static edu.java.bot.utils.URLValidator.isValidURL;

public class TrackCommand implements Command {

    public static final String NAME = "/track";
    public static final String DESCRIPTION = "начать отслеживание ссылки";
    public static final String ALREADY_TRACKING = "Ссылка уже отслеживается!";
    public static final String SUCCESS = "Ссылка зарегистрирована!";

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
        if (!DBUsers.isUserRegistered(chatId)) {
            return new SendMessage(chatId, ANSWER_TO_UNREGISTERED_USER);
        }
        DBUsersState.setUserState(chatId, UserState.TRACK);
        return new SendMessage(chatId, INPUT_URL);
    }

    public static SendMessage trackURL(Update update) {
        long chatId = update.message().chat().id();
        String text = update.message().text();

        DBUsersState.setUserState(chatId, UserState.REGISTERED);

        if (DBUsers.isUserHasLink(chatId, text)) {
            return new SendMessage(chatId, ALREADY_TRACKING);
        }

        if (isValidURL(text)) {
            DBUsers.addLink(chatId, text);
            return new SendMessage(chatId, SUCCESS);
        }
        return new SendMessage(chatId, INVALID_URL);
    }
}
