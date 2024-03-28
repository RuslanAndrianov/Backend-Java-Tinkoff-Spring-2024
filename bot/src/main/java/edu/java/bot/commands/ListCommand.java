package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import static edu.java.bot.repository.in_memory.UserLinks.getUserLinks;
import static edu.java.bot.repository.in_memory.UserLinks.isUserRegistered;

@Component
@RequiredArgsConstructor
public class ListCommand implements Command {

    public static final String NAME = "/list";
    public static final String DESCRIPTION = "показать список отслеживаемых ссылок";
    public static final String EMPTY_LIST = "Список ваших отслеживаемых ссылок пустой!";
    public static final String YOUR_LIST = "Список ваших отслеживаемых ссылок:\n";

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


        List<String> userLinks = getUserLinks(chatId);
        if (userLinks.isEmpty()) {
            return new SendMessage(chatId, EMPTY_LIST);
        }

        StringBuilder links = new StringBuilder();
        links.append(YOUR_LIST);
        for (int i = 0; i < Objects.requireNonNull(userLinks).size(); i++) {
            links.append(userLinks.get(i)).append("\n\n");
        }
        return new SendMessage(chatId, String.valueOf(links));
    }
}
