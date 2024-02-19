package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.model.UserState;
import edu.java.bot.repository.in_memory.DBUsers;
import edu.java.bot.repository.in_memory.DBUsersState;
import static edu.java.bot.utils.URLValidator.isValidURL;

public class UntrackCommand implements Command {

    public static final String NAME = "/untrack";
    public static final String DESCRIPTION = "прекратить отслеживание ссылки";
    public static final String NON_TRACKING = "Ссылка уже отслеживается!";
    public static final String SUCCESS = "Ссылка удалена!";
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
        DBUsersState.setUserState(chatId, UserState.UNTRACK);
        return new SendMessage(chatId, INPUT_URL);
    }

    public static SendMessage untrackURL(Update update) {
        long chatId = update.message().chat().id();
        String text = update.message().text();

        DBUsersState.setUserState(chatId, UserState.REGISTERED);

        if (!DBUsers.isUserHasLink(chatId, text)) {
            return new SendMessage(chatId, NON_TRACKING);
        }

        if (isValidURL(text)) {
            DBUsers.deleteLink(chatId, text);
            return new SendMessage(chatId, SUCCESS);
        }
        return new SendMessage(chatId, INVALID_URL);
    }
}
