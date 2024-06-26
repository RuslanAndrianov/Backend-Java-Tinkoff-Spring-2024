package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public interface Command {
    String name();

    String description();

    SendMessage handle(Update update, Object scrapperResponse);
}
