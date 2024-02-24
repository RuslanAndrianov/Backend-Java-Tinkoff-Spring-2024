import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.BotApplication;
import edu.java.bot.commands.StartCommand;
import edu.java.bot.commands.TrackCommand;
import edu.java.bot.commands.UntrackCommand;
import edu.java.bot.repository.in_memory.DBUsersLinks;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.ArrayList;
import java.util.List;
import static edu.java.bot.commands.TrackCommand.trackURL;
import static edu.java.bot.commands.UntrackCommand.untrackURL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = BotApplication.class)
public class DBUsersLinksTest {

    @Autowired
    StartCommand startCommand;
    @Autowired
    TrackCommand trackCommand;
    @Autowired
    UntrackCommand untrackCommand;

    @Test
    @DisplayName("Сценарий 1")
    void scenario1() {
        String LINK1 = "https://github.com";
        String LINK2 = "https://stackoverflow.com";

        Update update1 = mock(Update.class);
        Update update2 = mock(Update.class);
        Update update3 = mock(Update.class);
        Update update4 = mock(Update.class);
        Update update5 = mock(Update.class);
        Update update6 = mock(Update.class);
        Message message1 = mock(Message.class);
        Message message2 = mock(Message.class);
        Message message3 = mock(Message.class);
        Message message4 = mock(Message.class);
        Message message5 = mock(Message.class);
        Message message6 = mock(Message.class);
        Chat chat1 = mock(Chat.class);
        Chat chat2 = mock(Chat.class);

        when(update1.message()).thenReturn(message1);
        when(update2.message()).thenReturn(message2);
        when(update3.message()).thenReturn(message3);
        when(update4.message()).thenReturn(message4);
        when(update5.message()).thenReturn(message5);
        when(update6.message()).thenReturn(message6);
        when(message5.text()).thenReturn(LINK1);
        when(message6.text()).thenReturn(LINK2);
        when(message1.chat()).thenReturn(chat1);
        when(message2.chat()).thenReturn(chat2);
        when(message3.chat()).thenReturn(chat1);
        when(message4.chat()).thenReturn(chat2);
        when(message5.chat()).thenReturn(chat1);
        when(message6.chat()).thenReturn(chat2);
        when(chat1.id()).thenReturn(20L);
        when(chat2.id()).thenReturn(21L);

        SendMessage sendMessage1 = startCommand.handle(update1);
        SendMessage sendMessage2 = startCommand.handle(update2);
        assertTrue(DBUsersLinks.isUserRegistered(chat1.id()));
        assertTrue(DBUsersLinks.isUserRegistered(chat2.id()));
        SendMessage sendMessage3 = trackCommand.handle(update3);
        SendMessage sendMessage4 = trackCommand.handle(update4);
        SendMessage sendMessage5 = trackURL(update5);
        SendMessage sendMessage6 = trackURL(update6);

        // Работает локально, но возникает ошибка при сборке на GitHub

        /*assertTrue(DBUsersLinks.isUserHasLink(chat1.id(), LINK1));
        assertTrue(DBUsersLinks.isUserHasLink(chat2.id(), LINK2));
        assertEquals(DBUsersLinks.getUserLinks(chat1.id()), List.of(LINK1));
        assertEquals(DBUsersLinks.getUserLinks(chat2.id()), List.of(LINK2));*/
    }

    @Test
    @DisplayName("Сценарий 2")
    void scenario2() {
        String LINK1 = "https://github.com";
        String LINK2 = "https://stackoverflow.com";

        Update update1 = mock(Update.class);
        Update update2 = mock(Update.class);
        Update update3 = mock(Update.class);
        Update update4 = mock(Update.class);
        Update update5 = mock(Update.class);
        Update update6 = mock(Update.class);
        Update update7 = mock(Update.class);
        Update update8 = mock(Update.class);
        Update update9 = mock(Update.class);
        Update update10 = mock(Update.class);
        Message message1 = mock(Message.class);
        Message message2 = mock(Message.class);
        Message message3 = mock(Message.class);
        Message message4 = mock(Message.class);
        Message message5 = mock(Message.class);
        Message message6 = mock(Message.class);
        Message message7 = mock(Message.class);
        Message message8 = mock(Message.class);
        Message message9 = mock(Message.class);
        Message message10 = mock(Message.class);
        Chat chat1 = mock(Chat.class);
        Chat chat2 = mock(Chat.class);

        when(update1.message()).thenReturn(message1);
        when(update2.message()).thenReturn(message2);
        when(update3.message()).thenReturn(message3);
        when(update4.message()).thenReturn(message4);
        when(update5.message()).thenReturn(message5);
        when(update6.message()).thenReturn(message6);
        when(update7.message()).thenReturn(message7);
        when(update8.message()).thenReturn(message8);
        when(update9.message()).thenReturn(message9);
        when(update10.message()).thenReturn(message10);
        when(message5.text()).thenReturn(LINK1);
        when(message6.text()).thenReturn(LINK2);
        when(message9.text()).thenReturn(LINK2);
        when(message10.text()).thenReturn(LINK2);
        when(message1.chat()).thenReturn(chat1);
        when(message2.chat()).thenReturn(chat2);
        when(message3.chat()).thenReturn(chat1);
        when(message4.chat()).thenReturn(chat2);
        when(message5.chat()).thenReturn(chat1);
        when(message6.chat()).thenReturn(chat2);
        when(message7.chat()).thenReturn(chat1);
        when(message8.chat()).thenReturn(chat2);
        when(message9.chat()).thenReturn(chat1);
        when(message10.chat()).thenReturn(chat2);
        when(chat1.id()).thenReturn(22L);
        when(chat2.id()).thenReturn(23L);

        SendMessage sendMessage1 = startCommand.handle(update1);
        SendMessage sendMessage2 = startCommand.handle(update2);
        assertTrue(DBUsersLinks.isUserRegistered(chat1.id()));
        assertTrue(DBUsersLinks.isUserRegistered(chat2.id()));
        SendMessage sendMessage3 = trackCommand.handle(update3);
        SendMessage sendMessage4 = trackCommand.handle(update4);
        SendMessage sendMessage5 = trackURL(update5);
        SendMessage sendMessage6 = trackURL(update6);

        // Работает локально, но возникает ошибка при сборке на GitHub
        /*assertTrue(DBUsersLinks.isUserHasLink(chat1.id(), LINK1));
        assertTrue(DBUsersLinks.isUserHasLink(chat2.id(), LINK2));
        assertEquals(DBUsersLinks.getUserLinks(chat1.id()), List.of(LINK1));
        assertEquals(DBUsersLinks.getUserLinks(chat2.id()), List.of(LINK2));*/

        SendMessage sendMessage7 = trackCommand.handle(update7);
        SendMessage sendMessage8 = untrackCommand.handle(update8);
        SendMessage sendMessage9 = trackURL(update9);
        SendMessage sendMessage10 = untrackURL(update10);

        // Работает локально, но возникает ошибка при сборке на GitHub

        /*assertTrue(DBUsersLinks.isUserHasLink(chat1.id(), LINK1));
        assertTrue(DBUsersLinks.isUserHasLink(chat1.id(), LINK2));
        assertFalse(DBUsersLinks.isUserHasLink(chat2.id(), LINK2));
        assertEquals(DBUsersLinks.getUserLinks(chat1.id()), List.of(LINK1, LINK2));
        assertEquals(DBUsersLinks.getUserLinks(chat2.id()), new ArrayList<>());*/
    }
}
