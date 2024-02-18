package edu.java.bot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import edu.java.bot.configuration.CommandsConfig;
import edu.java.bot.handlers.MessageHandler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
public class MessageService {

    public MessageService(TelegramBot telegramBot, MessageHandler messageHandler, CommandsConfig commandsConfig) {

        UpdatesListener updatesListener = messageHandler.updatesListener();
        telegramBot.execute(commandsConfig.createCommandMenu());
        telegramBot.setUpdatesListener(updatesListener);
    }
}
