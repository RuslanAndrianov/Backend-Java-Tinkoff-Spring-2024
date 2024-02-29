import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.BotApplication;
import edu.java.bot.commands.HelpCommand;
import edu.java.bot.commands.ListCommand;
import edu.java.bot.commands.StartCommand;
import edu.java.bot.commands.TrackCommand;
import edu.java.bot.commands.UntrackCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static edu.java.bot.commands.TrackCommand.trackURL;
import static edu.java.bot.commands.UntrackCommand.untrackURL;
import static edu.java.bot.repository.in_memory.UserState.REGISTERED;
import static edu.java.bot.repository.in_memory.UserState.TRACK;
import static edu.java.bot.repository.in_memory.UserState.UNREGISTERED;
import static edu.java.bot.repository.in_memory.UserState.UNTRACKED;
import static edu.java.bot.repository.in_memory.DBUsersState.getUserState;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = BotApplication.class)
public class UserStateChangeTest {

    @Autowired
    HelpCommand helpCommand;
    @Autowired
    ListCommand listCommand;
    @Autowired
    StartCommand startCommand;
    @Autowired
    TrackCommand trackCommand;
    @Autowired
    UntrackCommand untrackCommand;

    @Test
    @DisplayName("Сценарий 1")
    void scenario1() {
        String LINK = "https://github.com";

        Update update1 = mock(Update.class);
        Update update2 = mock(Update.class);
        Update update3 = mock(Update.class);
        Update update4 = mock(Update.class);
        Update update5 = mock(Update.class);
        Message message1 = mock(Message.class);
        Message message2 = mock(Message.class);
        Message message3 = mock(Message.class);
        Message message4 = mock(Message.class);
        Message message5 = mock(Message.class);
        Chat chat = mock(Chat.class);

        when(update1.message()).thenReturn(message1);
        when(update2.message()).thenReturn(message2);
        when(update3.message()).thenReturn(message3);
        when(update4.message()).thenReturn(message4);
        when(update5.message()).thenReturn(message5);
        when(message3.text()).thenReturn(LINK);
        when(message5.text()).thenReturn(LINK);
        when(message1.chat()).thenReturn(chat);
        when(message2.chat()).thenReturn(chat);
        when(message3.chat()).thenReturn(chat);
        when(message4.chat()).thenReturn(chat);
        when(message5.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(15L);

        assertEquals(getUserState(chat.id()), UNREGISTERED);
        SendMessage sendMessage1 = startCommand.handle(update1);
        assertEquals(getUserState(chat.id()), REGISTERED);
        SendMessage sendMessage2 = trackCommand.handle(update2);
        assertEquals(getUserState(chat.id()), TRACK);
        SendMessage sendMessage3 = trackURL(update3);
        assertEquals(getUserState(chat.id()), REGISTERED);
        SendMessage sendMessage4 = untrackCommand.handle(update4);
        assertEquals(getUserState(chat.id()), UNTRACKED);
        SendMessage sendMessage5 = untrackURL(update5);
        assertEquals(getUserState(chat.id()), REGISTERED);
    }

    @Test
    @DisplayName("Сценарий 2")
    void scenario2() {

        Update update1 = mock(Update.class);
        Update update2 = mock(Update.class);
        Update update3 = mock(Update.class);
        Update update4 = mock(Update.class);
        Update update5 = mock(Update.class);
        Message message1 = mock(Message.class);
        Message message2 = mock(Message.class);
        Message message3 = mock(Message.class);
        Message message4 = mock(Message.class);
        Message message5 = mock(Message.class);
        Chat chat = mock(Chat.class);

        when(update1.message()).thenReturn(message1);
        when(update2.message()).thenReturn(message2);
        when(update3.message()).thenReturn(message3);
        when(update4.message()).thenReturn(message4);
        when(update5.message()).thenReturn(message5);
        when(message1.chat()).thenReturn(chat);
        when(message2.chat()).thenReturn(chat);
        when(message3.chat()).thenReturn(chat);
        when(message4.chat()).thenReturn(chat);
        when(message5.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(16L);

        assertEquals(getUserState(chat.id()), UNREGISTERED);
        SendMessage sendMessage1 = helpCommand.handle(update1);
        assertEquals(getUserState(chat.id()), UNREGISTERED);
        SendMessage sendMessage2 = trackCommand.handle(update1);
        assertEquals(getUserState(chat.id()), UNREGISTERED);
        SendMessage sendMessage3 = untrackCommand.handle(update1);
        assertEquals(getUserState(chat.id()), UNREGISTERED);
        SendMessage sendMessage4 = listCommand.handle(update1);
        assertEquals(getUserState(chat.id()), UNREGISTERED);
        SendMessage sendMessage5 = startCommand.handle(update2);
        assertEquals(getUserState(chat.id()), REGISTERED);
    }
}
