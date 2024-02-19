package edu.java.bot.service;

import com.pengrad.telegrambot.TelegramBot;
import edu.java.bot.configuration.CommandsConfig;
import edu.java.bot.handlers.MessageHandler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
public class MessageService {

    public MessageService(TelegramBot telegramBot, MessageHandler messageHandler, CommandsConfig commandsConfig) {

        telegramBot.execute(commandsConfig.createCommandMenu());
        telegramBot.setUpdatesListener(messageHandler.updatesListener());
    }
}
