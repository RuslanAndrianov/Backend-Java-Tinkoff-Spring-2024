package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.shared_dto.response_dto.LinkResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import static edu.java.bot.commands.Answers.INPUT_URL;
import static edu.java.bot.commands.Answers.INVALID_URL;
import static edu.java.bot.commands.Answers.NO_TRACKING;
import static edu.java.bot.commands.Answers.SOMETHING_WENT_WRONG;
import static edu.java.bot.commands.Answers.SUCCESSFUL_UNTRACKING;
import static edu.utils.URLValidator.isValidGitHubURL;
import static edu.utils.URLValidator.isValidStackOverflowURL;

@Component
@RequiredArgsConstructor
@Slf4j
public class UntrackCommand implements Command {
    public static final String NAME = "/untrack";
    public static final String DESCRIPTION = "прекратить отслеживание ссылки";

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public String description() {
        return DESCRIPTION;
    }

    @Override
    public SendMessage handle(@NotNull Update update, Object scrapperResponse) {
        long chatId = update.message().chat().id();
        String url = update.message().text();
        String responseUrl;
        Long linkId;
        try {
            responseUrl = ((LinkResponse) scrapperResponse).url() + "";
            linkId = ((LinkResponse) scrapperResponse).id();
        } catch (NullPointerException e) {
            return new SendMessage(chatId, INVALID_URL);
        }

        if (linkId == 0L) {
            return new SendMessage(chatId, NO_TRACKING);
        }

        if ((isValidGitHubURL(url) || isValidStackOverflowURL(url)) && url.equals(responseUrl)) {
            return new SendMessage(chatId, SUCCESSFUL_UNTRACKING);
        }
        return new SendMessage(chatId, SOMETHING_WENT_WRONG);
    }

    public SendMessage untrackURL(@NotNull Update update) {
        long chatId = update.message().chat().id();
        return new SendMessage(chatId, INPUT_URL);
    }
}
