package edu.java.scrapper.repositoryTest.jooq;

import edu.java.scrapper.IntegrationEnvironment;
import edu.java.scrapper.domain.dto.Chat;
import edu.java.scrapper.domain.repository.jooq.JooqChatsRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(properties = "app.database-access-type=jooq")
public class JooqChatsRepositoryTest extends IntegrationEnvironment {

    @Autowired
    private JooqChatsRepository jooqChatsRepository;

    @Test
    @Transactional
    @Rollback
    void addChatTest() {

        // Arrange
        long chat_id = 101L;
        Chat chat = new Chat();
        chat.setChatId(chat_id);
        boolean isChatAdded;

        // Act
        List<Chat> chatsBefore = jooqChatsRepository.getAllChats();
        isChatAdded = jooqChatsRepository.addChat(chat);
        List<Chat> chatsAfter = jooqChatsRepository.getAllChats();

        // Assert
        assertTrue(isChatAdded);
        assertEquals(chatsAfter.size() - chatsBefore.size(), 1);
    }

    @Test
    @Transactional
    @Rollback
    void deleteChatTest() {

        // Arrange
        long chat_id = 102L;
        Chat chat = new Chat();
        chat.setChatId(chat_id);
        boolean isChatDeleted;

        // Act
        List<Chat> chatsBefore = jooqChatsRepository.getAllChats();
        jooqChatsRepository.addChat(chat);
        isChatDeleted = jooqChatsRepository.deleteChat(chat);
        List<Chat> chatsAfter = jooqChatsRepository.getAllChats();

        // Assert
        assertTrue(isChatDeleted);
        assertEquals(chatsBefore.size(), chatsAfter.size());
    }

    @Test
    @Transactional
    @Rollback
    void getChatByIdTest() {

        // Arrange
        long chat_id = 103L;
        Chat chat = new Chat();
        chat.setChatId(chat_id);

        // Act
        jooqChatsRepository.addChat(chat);
        Chat foundChat = jooqChatsRepository.getChatById(chat_id);

        // Assert
        assertEquals(foundChat.getChatId(), chat.getChatId());
    }

    @Test
    @Transactional
    @Rollback
    void getAllChatsTest() {

        // Arrange
        long chat_id1 = 104L;
        long chat_id2 = 105L;
        long chat_id3 = 106L;

        Chat chat1 = new Chat();
        chat1.setChatId(chat_id1);
        Chat chat2 = new Chat();
        chat2.setChatId(chat_id2);
        Chat chat3 = new Chat();
        chat3.setChatId(chat_id3);

        // Act
        List<Chat> chatsBefore = jooqChatsRepository.getAllChats();
        jooqChatsRepository.addChat(chat1);
        jooqChatsRepository.addChat(chat2);
        jooqChatsRepository.addChat(chat3);
        List<Chat> chatsAfter = jooqChatsRepository.getAllChats();

        // Assert
        assertEquals(chatsAfter.size() - chatsBefore.size(), 3);
    }

    @Test
    @Transactional
    @Rollback
    void duplicateKeyTest() {

        // Arrange
        long chat_id = 107L;
        Chat chat = new Chat();
        chat.setChatId(chat_id);
        boolean isChatAdded;

        // Act && Assert
        isChatAdded = jooqChatsRepository.addChat(chat);
        assertTrue(isChatAdded);
        isChatAdded = jooqChatsRepository.addChat(chat);
        assertFalse(isChatAdded);
    }
}
