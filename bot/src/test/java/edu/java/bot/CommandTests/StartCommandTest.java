package edu.java.bot.CommandTests;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.BotApplication;
import edu.java.bot.commands.StartCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static edu.java.bot.commands.Answers.ALREADY_REGISTERED;
import static edu.java.bot.commands.Answers.SUCCESSFUL_REGISTRATION;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = BotApplication.class)
class StartCommandTest {

    @Autowired
    StartCommand startCommand;

    @Test
    @DisplayName("Новый пользователь")
    void newRegistration() {
        Update update = mock(Update.class);
        Chat chat = mock(Chat.class);
        Message message = mock(Message.class);
        Long id = 1L;

        when(chat.id()).thenReturn(id);
        when(message.chat()).thenReturn(chat);
        when(update.message()).thenReturn(message);

        SendMessage sendMessage = startCommand.handle(update, id.toString());
        assertEquals(sendMessage.getParameters().get("text"), SUCCESSFUL_REGISTRATION);
    }

    @Test
    @DisplayName("Пользователь пытается зарегистрироваться еще раз")
    void alreadyRegistred() {
        Update update = mock(Update.class);
        Chat chat = mock(Chat.class);
        Message message = mock(Message.class);
        Long id = 2L;

        when(chat.id()).thenReturn(id);
        when(message.chat()).thenReturn(chat);
        when(update.message()).thenReturn(message);

        SendMessage sendMessage1 = startCommand.handle(update, id.toString());
        assertEquals(SUCCESSFUL_REGISTRATION, sendMessage1.getParameters().get("text"));
        SendMessage sendMessage2 = startCommand.handle(update, null);
        assertEquals(sendMessage2.getParameters().get("text"), ALREADY_REGISTERED);
    }
}
