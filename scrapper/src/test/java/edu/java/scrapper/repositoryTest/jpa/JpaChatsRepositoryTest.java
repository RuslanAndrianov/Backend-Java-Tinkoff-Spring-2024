package edu.java.scrapper.repositoryTest.jpa;

import edu.java.domain.dto.Chat;
import edu.java.scrapper.repositoryTest.JpaIntegrationTest;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JpaChatsRepositoryTest extends JpaIntegrationTest {

    @Test
    @Transactional
    void addChatTest() {

        // Arrange
        long chat_id = 1L;
        Chat chat = new Chat();
        chat.setChatId(chat_id);
        boolean isChatAdded;

        // Act
        List<Chat> chatsBefore = jpaChatsRepository.getAllChats();
        isChatAdded = jpaChatsRepository.addChat(chat);
        List<Chat> chatsAfter = jpaChatsRepository.getAllChats();

        // Assert
        assertTrue(isChatAdded);
        assertEquals(chatsAfter.size() - chatsBefore.size(), 1);

        // After
        // TODO : @Rollback почему-то не работает во всех JPA-тестах
        jpaChatsRepository.deleteChat(chat);
    }

    @Test
    @Transactional
    void deleteChatTest() {

        // Arrange
        long chat_id = 2L;
        Chat chat = new Chat();
        chat.setChatId(chat_id);
        boolean isChatDeleted;

        // Act
        List<Chat> chatsBefore = jpaChatsRepository.getAllChats();
        jpaChatsRepository.addChat(chat);
        isChatDeleted = jpaChatsRepository.deleteChat(chat);
        List<Chat> chatsAfter = jpaChatsRepository.getAllChats();

        // Assert
        assertTrue(isChatDeleted);
        assertEquals(chatsBefore.size(), chatsAfter.size());
    }

    @Test
    @Transactional
    void getChatByIdTest() {

        // Arrange
        long chat_id = 3L;
        Chat chat = new Chat();
        chat.setChatId(chat_id);

        // Act
        jpaChatsRepository.addChat(chat);
        Chat foundChat = jpaChatsRepository.getChatById(chat_id);

        // Assert
        assertEquals(foundChat.getChatId(), chat.getChatId());

        // After
        jpaChatsRepository.deleteChat(chat);
    }

    @Test
    @Transactional
    @Rollback
    void getAllChatsTest() {

        // Arrange
        long chat_id1 = 4L;
        long chat_id2 = 5L;
        long chat_id3 = 6L;

        Chat chat1 = new Chat();
        chat1.setChatId(chat_id1);
        Chat chat2 = new Chat();
        chat2.setChatId(chat_id2);
        Chat chat3 = new Chat();
        chat3.setChatId(chat_id3);

        // Act
        List<Chat> chatsBefore = jpaChatsRepository.getAllChats();
        jpaChatsRepository.addChat(chat1);
        jpaChatsRepository.addChat(chat2);
        jpaChatsRepository.addChat(chat3);
        List<Chat> chatsAfter = jpaChatsRepository.getAllChats();

        // Assert
        assertEquals(chatsAfter.size() - chatsBefore.size(), 3);

        // After
        jpaChatsRepository.deleteChat(chat1);
        jpaChatsRepository.deleteChat(chat2);
        jpaChatsRepository.deleteChat(chat3);
    }

    @Test
    @Transactional
    @Rollback
    void duplicateKeyTest() {

        // Arrange
        long chat_id = 7L;
        Chat chat = new Chat();
        chat.setChatId(chat_id);
        boolean isChatAdded;

        // Act && Assert
        isChatAdded = jpaChatsRepository.addChat(chat);
        assertTrue(isChatAdded);
        isChatAdded = jpaChatsRepository.addChat(chat);
        assertFalse(isChatAdded);

        // After
        jpaChatsRepository.deleteChat(chat);
    }
}
