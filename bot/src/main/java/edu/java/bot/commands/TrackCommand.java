package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.clients.ScrapperClient;
import edu.shared_dto.ChatState;
import edu.shared_dto.request_dto.AddLinkRequest;
import java.net.URI;
import java.net.URISyntaxException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import static edu.java.bot.repository.in_memory.Links.addLink;
import static edu.java.bot.repository.in_memory.Links.isUserHasLink;
import static edu.java.bot.repository.in_memory.Links.isUserRegistered;
import static edu.java.bot.repository.in_memory.Users.setUserState;
import static edu.java.bot.utils.URLValidator.isValidURL;

@Component
@RequiredArgsConstructor
@Slf4j
public class TrackCommand implements Command {

    public static final String NAME = "/track";
    public static final String DESCRIPTION = "начать отслеживание ссылки";
    public static final String ALREADY_TRACKING = "Ошибка! Ссылка уже отслеживается! Используйте команду заново!";
    public static final String SUCCESS = "Ссылка успешно отслеживается!";

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
    public SendMessage handle(@NotNull Update update) {
        long chatId = update.message().chat().id();

        if (!isUserRegistered(chatId)) {
            return new SendMessage(chatId, ANSWER_TO_UNREGISTERED_USER);
        }

        setUserState(chatId, ChatState.TRACK);

        return new SendMessage(chatId, INPUT_URL);
    }

    public SendMessage trackURL(@NotNull Update update) {
        long chatId = update.message().chat().id();
        String text = update.message().text();

        setUserState(chatId, ChatState.REGISTERED);

        try {
            scrapperClient.addLink(chatId, new AddLinkRequest(new URI(text)));
            if (isUserHasLink(chatId, text)) {
                return new SendMessage(chatId, ALREADY_TRACKING);
            }

            if (isValidURL(text)) {
                addLink(chatId, text);
                return new SendMessage(chatId, SUCCESS);
            }
        } catch (URISyntaxException e) {
            log.error("Tracking error!");
        }

        return new SendMessage(chatId, INVALID_URL);
    }
}
