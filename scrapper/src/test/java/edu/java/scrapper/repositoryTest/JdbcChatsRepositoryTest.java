package edu.java.scrapper.repositoryTest;

import edu.java.domain.dto.Chat;
import edu.java.domain.repository.jdbc.JdbcChatsRepository;
import edu.java.scrapper.IntegrationTest;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JdbcChatsRepositoryTest extends IntegrationTest {

    private static final JdbcChatsRepository JDBC_CHATS_REPOSITORY;
    private static final RowMapper<Chat> chatRowMapper = (resultSet, rowNum) ->
        new Chat(resultSet.getLong("chat_id"));

    static {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSourceBuilder
            .create()
            .url(POSTGRES.getJdbcUrl())
            .username(POSTGRES.getUsername())
            .password(POSTGRES.getPassword())
            .build()
        );

        JDBC_CHATS_REPOSITORY = new JdbcChatsRepository(jdbcTemplate, chatRowMapper);
    }

    @Test
    @Transactional
    @Rollback
    void addChatTest() {
        long chat_id = 1L;
        Chat chat = new Chat(chat_id);
        boolean isChatAdded;

        List<Chat> chatsBefore = JDBC_CHATS_REPOSITORY.getAllChats();
        isChatAdded = JDBC_CHATS_REPOSITORY.addChat(chat);
        List<Chat> chatsAfter = JDBC_CHATS_REPOSITORY.getAllChats();

        assertTrue(isChatAdded);
        assertEquals(chatsAfter.size() - chatsBefore.size(), 1);

        JDBC_CHATS_REPOSITORY.deleteChat(chat);
    }

    @Test
    @Transactional
    @Rollback
    void deleteChatTest() {
        long chat_id = 2L;
        Chat chat = new Chat(chat_id);
        boolean isChatDeleted;

        List<Chat> chatsBefore = JDBC_CHATS_REPOSITORY.getAllChats();
        JDBC_CHATS_REPOSITORY.addChat(chat);
        isChatDeleted = JDBC_CHATS_REPOSITORY.deleteChat(chat);
        List<Chat> chatsAfter = JDBC_CHATS_REPOSITORY.getAllChats();

        assertTrue(isChatDeleted);
        assertEquals(chatsBefore.size(), chatsAfter.size());
    }

    @Test
    @Transactional
    @Rollback
    void getChatByIdTest() {
        long chat_id = 3L;
        Chat chat = new Chat(chat_id);

        JDBC_CHATS_REPOSITORY.addChat(chat);

        Chat foundChat = JDBC_CHATS_REPOSITORY.getChatById(chat_id);
        assertEquals(foundChat, chat);

        JDBC_CHATS_REPOSITORY.deleteChat(chat);
    }

    @Test
    @Transactional
    @Rollback
    void getAllChatsTest() {
        long chat_id1 = 4L;
        long chat_id2 = 5L;
        long chat_id3 = 6L;
        Chat chat1 = new Chat(chat_id1);
        Chat chat2 = new Chat(chat_id2);
        Chat chat3 = new Chat(chat_id3);

        List<Chat> chatsBefore = JDBC_CHATS_REPOSITORY.getAllChats();
        JDBC_CHATS_REPOSITORY.addChat(chat1);
        JDBC_CHATS_REPOSITORY.addChat(chat2);
        JDBC_CHATS_REPOSITORY.addChat(chat3);
        List<Chat> chatsAfter = JDBC_CHATS_REPOSITORY.getAllChats();

        assertEquals(chatsAfter.size() - chatsBefore.size(), 3);

        JDBC_CHATS_REPOSITORY.deleteChat(chat1);
        JDBC_CHATS_REPOSITORY.deleteChat(chat2);
        JDBC_CHATS_REPOSITORY.deleteChat(chat3);
    }

    @Test
    @Transactional
    @Rollback
    void duplicateKeyTest() {
        long chat_id = 7L;
        Chat chat = new Chat(chat_id);
        boolean isChatAdded;

        isChatAdded = JDBC_CHATS_REPOSITORY.addChat(chat);
        assertTrue(isChatAdded);
        isChatAdded = JDBC_CHATS_REPOSITORY.addChat(chat);
        assertFalse(isChatAdded);

        JDBC_CHATS_REPOSITORY.deleteChat(chat);
    }
}
