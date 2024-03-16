package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.shared_dto.ChatState;
import org.springframework.stereotype.Component;
import static edu.java.bot.repository.in_memory.DBUsersLinks.addLink;
import static edu.java.bot.repository.in_memory.DBUsersLinks.isUserHasLink;
import static edu.java.bot.repository.in_memory.DBUsersLinks.isUserRegistered;
import static edu.java.bot.repository.in_memory.DBUsersState.setUserState;
import static edu.java.bot.utils.URLValidator.isValidURL;

@Component
public class TrackCommand implements Command {

    public static final String NAME = "/track";
    public static final String DESCRIPTION = "начать отслеживание ссылки";
    public static final String ALREADY_TRACKING = "Ошибка! Ссылка уже отслеживается! Используйте команду заново!";
    public static final String SUCCESS = "Ссылка отслеживается!";

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
        setUserState(chatId, ChatState.TRACK);
        return new SendMessage(chatId, INPUT_URL);
    }

    public static SendMessage trackURL(Update update) {
        long chatId = update.message().chat().id();
        String text = update.message().text();

        setUserState(chatId, ChatState.REGISTERED);

        if (isUserHasLink(chatId, text)) {
            return new SendMessage(chatId, ALREADY_TRACKING);
        }

        if (isValidURL(text)) {
            addLink(chatId, text);
            return new SendMessage(chatId, SUCCESS);
        }
        return new SendMessage(chatId, INVALID_URL);
    }
}
