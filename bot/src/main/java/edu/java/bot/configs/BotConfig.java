package edu.java.bot.configs;

import com.pengrad.telegrambot.TelegramBot;
import edu.java.bot.listeners.TgUpdatesListener;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BotConfig {

    @Bean
    public TelegramBot telegramBot(@NotNull ApplicationConfig applicationConfig) {
        return new TelegramBot(applicationConfig.telegramToken());
    }

    @Bean
    public CommandsConfig commandsConfig(TelegramBot telegramBot) {
        return new CommandsConfig(telegramBot);
    }

    @Bean
    public TgUpdatesListenerConfig tgUpdatesListenerConfig(
        TelegramBot telegramBot,
        TgUpdatesListener tgUpdatesListener) {

        return new TgUpdatesListenerConfig(telegramBot, tgUpdatesListener);
    }
}
