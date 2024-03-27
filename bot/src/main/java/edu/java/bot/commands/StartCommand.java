package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.clients.ScrapperClient;
import edu.java.bot.repository.in_memory.Links;
import edu.java.bot.repository.in_memory.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import static edu.java.bot.repository.in_memory.Links.isUserRegistered;

@Component
@RequiredArgsConstructor
public class StartCommand implements Command {

    public static final String NAME = "/start";
    public static final String DESCRIPTION = "зарегистрироваться в боте";
    public static final String SUCCESS = "Вы зарегистрировались в боте!";
    public static final String ALREADY_REGISTERED = "Вы уже зарегистрированы!";
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

        scrapperClient.registerChat(chatId);

        if (isUserRegistered(chatId)) {
            return new SendMessage(chatId, ALREADY_REGISTERED);
        } else {
            Links.addUser(chatId);
            Users.addUser(chatId);
            return new SendMessage(chatId, SUCCESS);
        }
    }
}
