package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public interface Command {

    String ANSWER_TO_UNREGISTERED_USER = "Сначала зарегистрируйтесь с помощью команды /start";
    String INPUT_URL = "Введите URL:";
    String INVALID_URL = "Ошибка при вводе URL! Используйте команду заново!";

    String name();

    String description();

    SendMessage handle(Update update);
}
