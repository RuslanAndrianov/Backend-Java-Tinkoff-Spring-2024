package CommandTests;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.BotApplication;
import edu.java.bot.commands.ListCommand;
import edu.java.bot.commands.StartCommand;
import edu.java.bot.commands.TrackCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static edu.java.bot.commands.Command.ANSWER_TO_UNREGISTERED_USER;
import static edu.java.bot.commands.ListCommand.EMPTY_LIST;
import static edu.java.bot.commands.ListCommand.YOUR_LIST;
import static edu.java.bot.commands.StartCommand.SUCCESS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = BotApplication.class)
public class ListCommandTest {

    @Autowired
    StartCommand startCommand;
    @Autowired
    ListCommand listCommand;
    @Autowired
    TrackCommand trackCommand;

    @Test
    @DisplayName("Ответ НЕЗАРЕГИСТРИРОВАННОМУ пользователю")
    void unregisteredUser() {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);

        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(4L);

        SendMessage sendMessage = listCommand.handle(update);
        assertEquals(sendMessage.getParameters().get("text"), ANSWER_TO_UNREGISTERED_USER);
    }

    @Test
    @DisplayName("Ответ ЗАРЕГИСТРИРОВАННОМУ пользователю с пустым списком ссылок")
    void registeredAndEmptyList() {
        Update update1 = mock(Update.class);
        Update update2 = mock(Update.class);
        Message message1 = mock(Message.class);
        Message message2 = mock(Message.class);
        Chat chat = mock(Chat.class);

        when(update1.message()).thenReturn(message1);
        when(update2.message()).thenReturn(message2);
        when(message1.chat()).thenReturn(chat);
        when(message2.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(5L);

        SendMessage sendMessage1 = startCommand.handle(update1);
        assertEquals(sendMessage1.getParameters().get("text"), SUCCESS);
        SendMessage sendMessage2 = listCommand.handle(update2);
        assertEquals(sendMessage2.getParameters().get("text"), EMPTY_LIST);
    }

    @Test
    @DisplayName("Ответ ЗАРЕГИСТРИРОВАННОМУ пользователю, который отслеживает ссылки")
    void registeredAndNonEmptyList() {
        String LINK = "https://github.com";

        Update update1 = mock(Update.class);
        Update update2 = mock(Update.class);
        Update update3 = mock(Update.class);
        Update update4 = mock(Update.class);
        Message message1 = mock(Message.class);
        Message message2 = mock(Message.class);
        Message message3 = mock(Message.class);
        Message message4 = mock(Message.class);
        Chat chat = mock(Chat.class);

        when(update1.message()).thenReturn(message1);
        when(update2.message()).thenReturn(message2);
        when(update3.message()).thenReturn(message3);
        when(message3.text()).thenReturn(LINK);
        when(update4.message()).thenReturn(message4);
        when(message1.chat()).thenReturn(chat);
        when(message2.chat()).thenReturn(chat);
        when(message3.chat()).thenReturn(chat);
        when(message4.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(6L);

        SendMessage sendMessage1 = startCommand.handle(update1);
        assertEquals(sendMessage1.getParameters().get("text"), SUCCESS);
        SendMessage sendMessage2 = trackCommand.handle(update2);
        SendMessage sendMessage3 = trackCommand.trackURL(update3);
        SendMessage sendMessage4 = listCommand.handle(update4);

        assertEquals(sendMessage4.getParameters().get("text"), YOUR_LIST + LINK + "\n\n");
    }
}
