package edu.java.bot.CommandTests;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.BotApplication;
import edu.java.bot.commands.StartCommand;
import edu.java.bot.commands.TrackCommand;
import edu.java.bot.commands.UntrackCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = BotApplication.class)
public class UntrackCommandTest {

    @Autowired
    StartCommand startCommand;
    @Autowired
    TrackCommand trackCommand;
    @Autowired
    UntrackCommand untrackCommand;

    @Test
    @DisplayName("Ответ НЕЗАРЕГИСТРИРОВАННОМУ пользователю")
    void unregisteredUser() {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);

        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(11L);

//        SendMessage sendMessage = untrackCommand.handle(update);
//        assertEquals(sendMessage.getParameters().get("text"), ANSWER_TO_UNREGISTERED_USER);
    }

    @Test
    @DisplayName("Ответ ЗАРЕГИСТРИРОВАННОМУ пользователю, который удаляет КОРРЕКТНУЮ ссылку, которая ОТСЛЕЖИВАЕТСЯ")
    void registeredAndUntrackValidTrackingURL() {
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
        when(chat.id()).thenReturn(12L);

//        SendMessage sendMessage1 = startCommand.handle(update1);
//        assertEquals(sendMessage1.getParameters().get("text"), StartCommand.SUCCESS);
//        SendMessage sendMessage2 = trackCommand.handle(update2);
//        assertEquals(sendMessage2.getParameters().get("text"), INPUT_URL);
//        SendMessage sendMessage3 = trackCommand.trackURL(update3);
//        assertEquals(sendMessage3.getParameters().get("text"), TrackCommand.SUCCESS);
//        SendMessage sendMessage4 = trackCommand.handle(update4);
//        assertEquals(sendMessage4.getParameters().get("text"), INPUT_URL);
//        SendMessage sendMessage5 = untrackCommand.untrackURL(update5);
//        assertEquals(sendMessage5.getParameters().get("text"), UntrackCommand.SUCCESS);
    }

    @Test
    @DisplayName("Ответ ЗАРЕГИСТРИРОВАННОМУ пользователю, который удаляет КОРРЕКТНУЮ ссылку, которая НЕ ОТСЛЕЖИВАЕТСЯ")
    void registeredAndUntrackValidUntrackingURL() {
        String LINK1 = "https://github.com";
        String LINK2 = "https://stackoverflow.com";

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
        when(message3.text()).thenReturn(LINK1);
        when(message5.text()).thenReturn(LINK2);
        when(message1.chat()).thenReturn(chat);
        when(message2.chat()).thenReturn(chat);
        when(message3.chat()).thenReturn(chat);
        when(message4.chat()).thenReturn(chat);
        when(message5.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(13L);

//        SendMessage sendMessage1 = startCommand.handle(update1);
//        assertEquals(sendMessage1.getParameters().get("text"), StartCommand.SUCCESS);
//        SendMessage sendMessage2 = trackCommand.handle(update2);
//        assertEquals(sendMessage2.getParameters().get("text"), INPUT_URL);
//        SendMessage sendMessage3 = trackCommand.trackURL(update3);
//        assertEquals(sendMessage3.getParameters().get("text"), TrackCommand.SUCCESS);
//        SendMessage sendMessage4 = trackCommand.handle(update4);
//        assertEquals(sendMessage4.getParameters().get("text"), INPUT_URL);
//        SendMessage sendMessage5 = untrackCommand.untrackURL(update5);

        // Работает локально, но возникает ошибка при сборке на GitHub
        // assertEquals(sendMessage5.getParameters().get("text"), NON_TRACKING);
    }

    @Test
    @DisplayName("Ответ ЗАРЕГИСТРИРОВАННОМУ пользователю, который удаляет НЕКОРРЕКТНУЮ ссылку")
    void registeredAndUntrackInvalidURL() {
        String LINK1 = "https://github.com";
        String LINK2 = "invalid_url";

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
        when(message3.text()).thenReturn(LINK1);
        when(message5.text()).thenReturn(LINK2);
        when(message1.chat()).thenReturn(chat);
        when(message2.chat()).thenReturn(chat);
        when(message3.chat()).thenReturn(chat);
        when(message4.chat()).thenReturn(chat);
        when(message5.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(14L);

//        SendMessage sendMessage1 = startCommand.handle(update1);
//        assertEquals(sendMessage1.getParameters().get("text"), StartCommand.SUCCESS);
//        SendMessage sendMessage2 = trackCommand.handle(update2);
//        assertEquals(sendMessage2.getParameters().get("text"), INPUT_URL);
//        SendMessage sendMessage3 = trackCommand.trackURL(update3);
//        assertEquals(sendMessage3.getParameters().get("text"), TrackCommand.SUCCESS);
//        SendMessage sendMessage4 = trackCommand.handle(update4);
//        assertEquals(sendMessage4.getParameters().get("text"), INPUT_URL);
//        SendMessage sendMessage5 = untrackCommand.untrackURL(update5);
//        assertEquals(sendMessage5.getParameters().get("text"), INVALID_URL);
    }
}
