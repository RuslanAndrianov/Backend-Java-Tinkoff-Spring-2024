package CommandTests;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.BotApplication;
import edu.java.bot.commands.StartCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

        when(chat.id()).thenReturn(1L);
        when(message.chat()).thenReturn(chat);
        when(update.message()).thenReturn(message);

//        SendMessage sendMessage = startCommand.handle(update);
//        assertEquals(sendMessage.getParameters().get("text"), SUCCESS);
    }

    @Test
    @DisplayName("Пользователь пытается зарегистрироваться еще раз")
    void alreadyRegistred() {
        Update update = mock(Update.class);
        Chat chat = mock(Chat.class);
        Message message = mock(Message.class);

        when(chat.id()).thenReturn(2L);
        when(message.chat()).thenReturn(chat);
        when(update.message()).thenReturn(message);

//        SendMessage sendMessage1 = startCommand.handle(update);
//        assertEquals(SUCCESS, sendMessage1.getParameters().get("text"));
//        SendMessage sendMessage2 = startCommand.handle(update);
//        assertEquals(sendMessage2.getParameters().get("text"), ALREADY_REGISTERED);
    }
}
