package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.repository.DatabaseUsers;
import java.util.List;
import java.util.Objects;

public class ListCommand implements Command {

    @Override
    public String name() {
        return "/list";
    }

    @Override
    public String description() {
        return "показать список отслеживаемых ссылок";
    }

    @Override
    public SendMessage handle(Update update) {

        long chatId = update.message().chat().id();
        if (!DatabaseUsers.isUserRegistered(chatId)) {
            return new SendMessage(chatId, "Сначала зарегистрируйтесь с помощью команды /start");
        }

        List<String> userLinks = DatabaseUsers.getUserLinks(chatId);
        if (userLinks.isEmpty()) {
            return new SendMessage(chatId, "Список ваших отслеживаемых ссылок пустой!");
        }

        StringBuilder links = new StringBuilder();
        links.append("Список ваших отслеживаемых ссылок:\n");
        for (int i = 0; i < Objects.requireNonNull(userLinks).size(); i++) {
            links.append(userLinks.get(i)).append("\n\n");
        }
        return new SendMessage(chatId, String.valueOf(links));
    }
}
