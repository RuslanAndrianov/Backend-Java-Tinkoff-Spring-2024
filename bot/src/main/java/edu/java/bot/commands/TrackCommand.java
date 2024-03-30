package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.shared_dto.response_dto.LinkResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import static edu.java.bot.commands.Answers.ALREADY_TRACKING;
import static edu.java.bot.commands.Answers.INAPPROPRIATE_LINK_TRACK;
import static edu.java.bot.commands.Answers.INPUT_URL;
import static edu.java.bot.commands.Answers.INVALID_URL;
import static edu.java.bot.commands.Answers.SOMETHING_WENT_WRONG;
import static edu.java.bot.commands.Answers.SUCCESSFUL_TRACKING;
import static edu.utils.URLValidator.isValidGitHubURL;
import static edu.utils.URLValidator.isValidStackOverflowURL;

@Component
@RequiredArgsConstructor
@Slf4j
public class TrackCommand implements Command {

    public static final String NAME = "/track";
    public static final String DESCRIPTION = "начать отслеживание ссылки";

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
            return new SendMessage(chatId, INAPPROPRIATE_LINK_TRACK);
        }

        if (linkId == 0L) {
            return new SendMessage(chatId, ALREADY_TRACKING);
        }

        if ((isValidGitHubURL(url) || isValidStackOverflowURL(url)) && url.equals(responseUrl)) {
            return new SendMessage(chatId, SUCCESSFUL_TRACKING);
        }
        return new SendMessage(chatId, SOMETHING_WENT_WRONG);
    }

    public SendMessage trackURL(@NotNull Update update) {
        long chatId = update.message().chat().id();
        return new SendMessage(chatId, INPUT_URL);
    }
}
