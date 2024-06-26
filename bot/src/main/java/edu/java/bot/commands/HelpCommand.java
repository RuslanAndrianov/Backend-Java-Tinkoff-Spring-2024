package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import static edu.java.bot.configs.CommandsConfig.commands;

@Component
public class HelpCommand implements Command {

    public static final String NAME = "/help";
    public static final String DESCRIPTION = "вывести сообщение с командами";

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public String description() {
        return DESCRIPTION;
    }

    @Override
    public SendMessage handle(@NotNull Update update, Object ignored) {
        long chatId = update.message().chat().id();
        StringBuilder answer = new StringBuilder();
        answer.append("Список доступных команд:\n");
        for (Command command : commands) {
            answer
                .append(command.name())
                .append(" - ")
                .append(command.description())
                .append("\n");
        }
        return new SendMessage(chatId, String.valueOf(answer));
    }
}
