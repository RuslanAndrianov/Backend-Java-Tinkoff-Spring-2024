package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import static edu.java.bot.configuration.CommandsConfig.COMMANDS;

public class HelpCommand implements Command {

    public static final String NAME = "/help";
    public static final String DESCRIPTION = "вывести окно с командами";

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
        StringBuilder response = new StringBuilder();
        response.append("Список доступных команд:\n");
        for (Command command : COMMANDS) {
            response
                .append(command.name())
                .append(" - ")
                .append(command.description())
                .append("\n");
        }
        return new SendMessage(chatId, String.valueOf(response));
    }
}
