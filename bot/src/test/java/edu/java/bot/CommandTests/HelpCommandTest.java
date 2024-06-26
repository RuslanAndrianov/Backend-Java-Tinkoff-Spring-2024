package edu.java.bot.CommandTests;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.BotApplication;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.HelpCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static edu.java.bot.configs.CommandsConfig.commands;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = BotApplication.class)
public class HelpCommandTest {

    @Autowired
    HelpCommand helpCommand;

    @Test
    @DisplayName("Показать список команд")
    void showHelpMessage() {
        Update update = mock(Update.class);
        Chat chat = mock(Chat.class);
        Message message = mock(Message.class);

        when(chat.id()).thenReturn(3L);
        when(message.chat()).thenReturn(chat);
        when(update.message()).thenReturn(message);

        StringBuilder response = new StringBuilder();
        response.append("Список доступных команд:\n");
        for (Command command : commands) {
            response
                .append(command.name())
                .append(" - ")
                .append(command.description())
                .append("\n");
        }

//        SendMessage sendMessage = helpCommand.handle(update);
//        assertEquals(sendMessage.getParameters().get("text"), String.valueOf(response));
    }
}
