package edu.java.bot.CommandTests;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.BotApplication;
import edu.java.bot.commands.ListCommand;
import edu.shared_dto.response_dto.LinkResponse;
import edu.shared_dto.response_dto.ListLinksResponse;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static edu.java.bot.commands.Answers.ANSWER_TO_UNREGISTERED_USER;
import static edu.java.bot.commands.Answers.EMPTY_LINKS_LIST;
import static edu.java.bot.commands.Answers.YOUR_LIST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = BotApplication.class)
public class ListCommandTest {

    @Autowired
    ListCommand listCommand;

    @Test
    @DisplayName("Ответ незарегистрированному пользователю")
    void unregisteredUser() {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        Long id = 4L;

        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(id);

        SendMessage sendMessage = listCommand.handle(update, null);

        assertEquals(sendMessage.getParameters().get("text"), ANSWER_TO_UNREGISTERED_USER);
    }

    @Test
    @DisplayName("Ответ зарегистрированному пользователю с пустым списком ссылок")
    void registeredAndEmptyList() {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        Long id = 5L;

        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(id);

        SendMessage sendMessage = listCommand.handle(
            update,
            new ListLinksResponse(new ArrayList<>(), 0));

        assertEquals(sendMessage.getParameters().get("text"), EMPTY_LINKS_LIST);
    }

    @Test
    @DisplayName("Ответ зарегистрированному пользователю, который отслеживает ссылки")
    void registeredAndNonEmptyList() throws URISyntaxException {
        String LINK = "https://github.com";

        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        Long id = 6L;

        LinkResponse linkResponse = new LinkResponse(1L, new URI(LINK));
        List<LinkResponse> linkResponseList = new ArrayList<>();
        linkResponseList.add(linkResponse);

        ListLinksResponse scrapperResponse = new ListLinksResponse(
            linkResponseList,
            linkResponseList.size()
        );

        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(id);

        SendMessage sendMessage4 = listCommand.handle(update, scrapperResponse);

        assertEquals(sendMessage4.getParameters().get("text"), YOUR_LIST + LINK + "\n\n");
    }
}
