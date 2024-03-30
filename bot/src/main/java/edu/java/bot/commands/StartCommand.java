package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import static edu.java.bot.commands.Answers.ALREADY_REGISTERED;
import static edu.java.bot.commands.Answers.SOMETHING_WENT_WRONG;
import static edu.java.bot.commands.Answers.SUCCESSFUL_REGISTRATION;

@Component
public class StartCommand implements Command {

    public static final String NAME = "/start";
    public static final String DESCRIPTION = "зарегистрироваться в боте";

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
        long parsedResponse;
        try {
            parsedResponse = Long.parseLong((String) scrapperResponse);
            if (chatId == parsedResponse) {
                return new SendMessage(chatId, SUCCESSFUL_REGISTRATION);
            } else {
                return new SendMessage(chatId, SOMETHING_WENT_WRONG);
            }
        } catch (NumberFormatException e) {
            return new SendMessage(chatId, ALREADY_REGISTERED);
        }
    }
}
