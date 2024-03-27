package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.clients.ScrapperClient;
import edu.shared_dto.ChatState;
import edu.shared_dto.request_dto.RemoveLinkRequest;
import java.net.URI;
import java.net.URISyntaxException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import static edu.java.bot.repository.in_memory.Links.deleteLink;
import static edu.java.bot.repository.in_memory.Links.isUserHasLink;
import static edu.java.bot.repository.in_memory.Links.isUserRegistered;
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
    public static final String SOMETHING_WRONG = "Что-то пошло не так!";
    private final ScrapperClient scrapperClient;

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

    public SendMessage untrackURL(Update update) {
        long chatId = update.message().chat().id();
        String text = update.message().text();

        setUserState(chatId, ChatState.REGISTERED);

        if (!isValidURL(text)) {
            return new SendMessage(chatId, INVALID_URL);
        }

        if (!isUserHasLink(chatId, text)) {
            return new SendMessage(chatId, NON_TRACKING);
        }

        try {
            scrapperClient.deleteLink(chatId, new RemoveLinkRequest(new URI(text)));
            deleteLink(chatId, text);
            return new SendMessage(chatId, SUCCESS);
        } catch (URISyntaxException e) {
            log.info("Untracking error!");
        }

        return new SendMessage(chatId, SOMETHING_WRONG);
    }
}
