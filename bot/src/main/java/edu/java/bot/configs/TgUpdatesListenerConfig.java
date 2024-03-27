package edu.java.bot.configs;

import com.pengrad.telegrambot.TelegramBot;
import edu.java.bot.listeners.TgUpdatesListener;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TgUpdatesListenerConfig {

    public TgUpdatesListenerConfig(
        @NotNull TelegramBot telegramBot,
        @NotNull TgUpdatesListener tgUpdatesListener) {

        telegramBot.setUpdatesListener(tgUpdatesListener.getTgUpdatesListener());
    }
}
