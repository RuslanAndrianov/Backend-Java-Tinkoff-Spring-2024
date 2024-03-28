package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.shared_dto.ChatState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import static edu.java.bot.repository.in_memory.UserLinks.deleteLink;
import static edu.java.bot.repository.in_memory.UserLinks.isUserHasLink;
import static edu.java.bot.repository.in_memory.UserLinks.isUserRegistered;
import static edu.java.bot.repository.in_memory.Users.setUserState;
import static edu.java.bot.utils.URLValidator.isValidURL;

@Component
@RequiredArgsConstructor
@Slf4j
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

        setUserState(chatId, ChatState.UNTRACKED);

        return new SendMessage(chatId, INPUT_URL);
    }

    public SendMessage untrackURL(@NotNull Update update) {
        long chatId = update.message().chat().id();
        String text = update.message().text();

        setUserState(chatId, ChatState.REGISTERED);

        if (!isValidURL(text)) {
            return new SendMessage(chatId, INVALID_URL);
        }

        if (!isUserHasLink(chatId, text)) {
            return new SendMessage(chatId, NON_TRACKING);
        }

        deleteLink(chatId, text);
        return new SendMessage(chatId, SUCCESS);
    }
}
