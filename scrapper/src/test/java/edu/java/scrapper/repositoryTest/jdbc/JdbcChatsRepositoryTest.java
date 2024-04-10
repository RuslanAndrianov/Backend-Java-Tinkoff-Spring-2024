package edu.java.scrapper.repositoryTest.jdbc;

import edu.java.domain.dto.Chat;
import edu.java.domain.repository.jdbc.JdbcChatsRepository;
import edu.java.scrapper.IntegrationEnvironment;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class JdbcChatsRepositoryTest extends IntegrationEnvironment {

    @Autowired
    private JdbcChatsRepository jdbcChatsRepository;

//    private static final JdbcChatsRepository jdbcChatsRepository;
//    private static final RowMapper<Chat> chatRowMapper = (resultSet, rowNum) -> {
//        Chat chat = new Chat();
//        chat.setChatId(resultSet.getLong("chat_id"));
//        return chat;
//    };
//
//    static {
//        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSourceBuilder
//            .create()
//            .url(POSTGRES.getJdbcUrl())
//            .username(POSTGRES.getUsername())
//            .password(POSTGRES.getPassword())
//            .build()
//        );
//
//        jdbcChatsRepository = new JdbcChatsRepository(jdbcTemplate, chatRowMapper);
//    }

    @Test
    @Transactional
    @Rollback
    void addChatTest() {

        // Arrange
        long chat_id = 1L;
        Chat chat = new Chat();
        chat.setChatId(chat_id);
        boolean isChatAdded;

        // Act
        List<Chat> chatsBefore = jdbcChatsRepository.getAllChats();
        isChatAdded = jdbcChatsRepository.addChat(chat);
        List<Chat> chatsAfter = jdbcChatsRepository.getAllChats();

        // Assert
        assertTrue(isChatAdded);
        assertEquals(chatsAfter.size() - chatsBefore.size(), 1);
    }

    @Test
    @Transactional
    @Rollback
    void deleteChatTest() {

        // Arrange
        long chat_id = 2L;
        Chat chat = new Chat();
        chat.setChatId(chat_id);
        boolean isChatDeleted;

        // Act
        List<Chat> chatsBefore = jdbcChatsRepository.getAllChats();
        jdbcChatsRepository.addChat(chat);
        isChatDeleted = jdbcChatsRepository.deleteChat(chat);
        List<Chat> chatsAfter = jdbcChatsRepository.getAllChats();

        // Assert
        assertTrue(isChatDeleted);
        assertEquals(chatsBefore.size(), chatsAfter.size());
    }

    @Test
    @Transactional
    @Rollback
    void getChatByIdTest() {

        // Arrange
        long chat_id = 3L;
        Chat chat = new Chat();
        chat.setChatId(chat_id);

        // Act
        jdbcChatsRepository.addChat(chat);
        Chat foundChat = jdbcChatsRepository.getChatById(chat_id);

        // Assert
        assertEquals(foundChat.getChatId(), chat.getChatId());
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
        List<Chat> chatsBefore = jdbcChatsRepository.getAllChats();
        jdbcChatsRepository.addChat(chat1);
        jdbcChatsRepository.addChat(chat2);
        jdbcChatsRepository.addChat(chat3);
        List<Chat> chatsAfter = jdbcChatsRepository.getAllChats();

        // Assert
        assertEquals(chatsAfter.size() - chatsBefore.size(), 3);
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
        isChatAdded = jdbcChatsRepository.addChat(chat);
        assertTrue(isChatAdded);
        isChatAdded = jdbcChatsRepository.addChat(chat);
        assertFalse(isChatAdded);
    }
}
