package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.model.UserState;
import org.springframework.stereotype.Component;
import static edu.java.bot.repository.in_memory.DBUsersLinks.deleteLink;
import static edu.java.bot.repository.in_memory.DBUsersLinks.isUserHasLink;
import static edu.java.bot.repository.in_memory.DBUsersLinks.isUserRegistered;
import static edu.java.bot.repository.in_memory.DBUsersState.setUserState;
import static edu.java.bot.utils.URLValidator.isValidURL;

@Component
public class UntrackCommand implements Command {

    public static final String NAME = "/untrack";
    public static final String DESCRIPTION = "прекратить отслеживание ссылки";
    public static final String NON_TRACKING =
        "Ошибка! Такой ссылки нет в списке отслеживаемых! Используйте команду заново!";
    public static final String SUCCESS = "Ссылка не отслеживается!";

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
        if (!isUserRegistered(chatId)) {
            return new SendMessage(chatId, ANSWER_TO_UNREGISTERED_USER);
        }
        setUserState(chatId, UserState.UNTRACKED);
        return new SendMessage(chatId, INPUT_URL);
    }

    public static SendMessage untrackURL(Update update) {
        long chatId = update.message().chat().id();
        String text = update.message().text();

        setUserState(chatId, UserState.REGISTERED);

        if (!isUserHasLink(chatId, text)) {
            return new SendMessage(chatId, NON_TRACKING);
        }

        if (isValidURL(text)) {
            deleteLink(chatId, text);
            return new SendMessage(chatId, SUCCESS);
        }
        return new SendMessage(chatId, INVALID_URL);
    }
}
