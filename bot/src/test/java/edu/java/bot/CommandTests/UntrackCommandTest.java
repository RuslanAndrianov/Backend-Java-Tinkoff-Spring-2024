package edu.java.bot.CommandTests;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.BotApplication;
import edu.java.bot.commands.UntrackCommand;
import edu.shared_dto.response_dto.LinkResponse;
import java.net.URI;
import java.net.URISyntaxException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static edu.java.bot.commands.Answers.INPUT_URL;
import static edu.java.bot.commands.Answers.NO_TRACKING;
import static edu.java.bot.commands.Answers.SUCCESSFUL_UNTRACKING;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = BotApplication.class)
public class UntrackCommandTest {

    @Autowired
    UntrackCommand untrackCommand;

    @Test
    @DisplayName("Ответ пользователю, который удаляет отслеживаемую ссылку")
    void registeredAndUntrackValidTrackingURL() throws URISyntaxException {
        String LINK = "https://github.com/RuslanAndrianov/Backend-Java-Tinkoff-Spring-2024";

        Update update1 = mock(Update.class);
        Update update2 = mock(Update.class);
        Message message1 = mock(Message.class);
        Message message2 = mock(Message.class);
        Chat chat = mock(Chat.class);
        Long id = 10L;
        LinkResponse linkResponse = new LinkResponse(1L, new URI(LINK));

        when(update1.message()).thenReturn(message1);
        when(update2.message()).thenReturn(message2);
        when(message2.text()).thenReturn(LINK);
        when(message1.chat()).thenReturn(chat);
        when(message2.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(id);

        SendMessage sendMessage1 = untrackCommand.untrackURL(update1);
        assertEquals(sendMessage1.getParameters().get("text"), INPUT_URL);
        SendMessage sendMessage2 = untrackCommand.handle(update2, linkResponse);
        assertEquals(sendMessage2.getParameters().get("text"), SUCCESSFUL_UNTRACKING);
    }

    @Test
    @DisplayName("Ответ пользователю, который удаляет не отслеживаемую ссылку")
    void registeredAndUntrackValidUntrackingURL() throws URISyntaxException {
        String LINK = "https://github.com";

        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        Long id = 11L;
        LinkResponse linkResponse = new LinkResponse(0L, new URI(LINK));

        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn(LINK);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(id);

        SendMessage sendMessage = untrackCommand.handle(update, linkResponse);

        assertEquals(sendMessage.getParameters().get("text"), NO_TRACKING);
    }
}
