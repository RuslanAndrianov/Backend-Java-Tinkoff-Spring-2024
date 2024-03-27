package edu.java.bot.configs;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.request.SetMyCommands;
import edu.java.bot.clients.ScrapperClient;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.HelpCommand;
import edu.java.bot.commands.ListCommand;
import edu.java.bot.commands.StartCommand;
import edu.java.bot.commands.TrackCommand;
import edu.java.bot.commands.UntrackCommand;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommandsConfig {

    public static List<Command> commands = new ArrayList<>();

    public CommandsConfig(@NotNull TelegramBot telegramBot, ScrapperClient scrapperClient) {
        commands.add(new HelpCommand());
        commands.add(new ListCommand());
        commands.add(new StartCommand());
        commands.add(new TrackCommand());
        commands.add(new UntrackCommand());
        telegramBot.execute(this.createCommandMenu());
    }

    public SetMyCommands createCommandMenu() {
        return new SetMyCommands(commands.stream().map(command -> new BotCommand(
            command.name(),
            command.description()
        )).toArray(BotCommand[]::new));
    }
}
