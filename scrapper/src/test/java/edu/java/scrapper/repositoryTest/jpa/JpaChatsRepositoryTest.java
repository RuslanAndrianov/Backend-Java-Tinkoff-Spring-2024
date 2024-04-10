package edu.java.scrapper.repositoryTest.jpa;

import edu.java.domain.dto.Chat;
import java.util.List;
import edu.java.domain.repository.jpa.JpaChatsRepository;
import edu.java.scrapper.IntegrationEnvironment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(properties = "app.database-access-type=jpa")
public class JpaChatsRepositoryTest extends IntegrationEnvironment {

    @Autowired
    private JpaChatsRepository jpaChatsRepository;

    @Test
    @Transactional
    @Rollback
    void addChatTest() {

        // Arrange
        long chat_id = 30L;
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
    }

    @Test
    @Transactional
    @Rollback
    void deleteChatTest() {

        // Arrange
        long chat_id = 31L;
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
    @Rollback
    void getChatByIdTest() {

        // Arrange
        long chat_id = 32L;
        Chat chat = new Chat();
        chat.setChatId(chat_id);

        // Act
        jpaChatsRepository.addChat(chat);
        Chat foundChat = jpaChatsRepository.getChatById(chat_id);

        // Assert
        assertEquals(foundChat.getChatId(), chat.getChatId());
    }

    @Test
    @Transactional
    @Rollback
    void getAllChatsTest() {

        // Arrange
        long chat_id1 = 33L;
        long chat_id2 = 34L;
        long chat_id3 = 35L;

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
    }

    @Test
    @Transactional
    @Rollback
    void duplicateKeyTest() {

        // Arrange
        long chat_id = 36L;
        Chat chat = new Chat();
        chat.setChatId(chat_id);
        boolean isChatAdded;

        // Act && Assert
        isChatAdded = jpaChatsRepository.addChat(chat);
        assertTrue(isChatAdded);
        isChatAdded = jpaChatsRepository.addChat(chat);
        assertFalse(isChatAdded);
    }
}
