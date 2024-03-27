package edu.java.scrapper.repositoryTest;

import edu.java.domain.dto.Chat;
import edu.java.domain.repository.ChatsRepository;
import edu.java.scrapper.IntegrationTest;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static edu.shared_dto.ChatState.REGISTERED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ChatsRepositoryTest extends IntegrationTest {

    private static final ChatsRepository chatsRepository;
    private static final RowMapper<Chat> chatRowMapper = (resultSet, rowNum) ->
        new Chat(
            resultSet.getLong("chat_id"),
            resultSet.getString("chat_state"));

    static {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSourceBuilder
            .create()
            .url(POSTGRES.getJdbcUrl())
            .username(POSTGRES.getUsername())
            .password(POSTGRES.getPassword())
            .build()
        );

        chatsRepository = new ChatsRepository(jdbcTemplate, chatRowMapper);
    }

    @Test
    @Transactional
    @Rollback
    void addChatTest() {
        long chat_id = 1L;
        Chat chat = new Chat(chat_id, REGISTERED.toString());
        boolean isChatAdded;

        List<Chat> chatsBefore = chatsRepository.getAllChats();
        isChatAdded = chatsRepository.addChat(chat);
        List<Chat> chatsAfter = chatsRepository.getAllChats();

        assertTrue(isChatAdded);
        assertEquals(chatsAfter.size() - chatsBefore.size(), 1);

        chatsRepository.deleteChat(chat);
    }

    @Test
    @Transactional
    @Rollback
    void deleteChatTest() {
        long chat_id = 2L;
        Chat chat = new Chat(chat_id, REGISTERED.toString());
        boolean isChatDeleted;

        List<Chat> chatsBefore = chatsRepository.getAllChats();
        chatsRepository.addChat(chat);
        isChatDeleted = chatsRepository.deleteChat(chat);
        List<Chat> chatsAfter = chatsRepository.getAllChats();

        assertTrue(isChatDeleted);
        assertEquals(chatsBefore.size(), chatsAfter.size());
    }

    @Test
    @Transactional
    @Rollback
    void getChatByIdTest() {
        long chat_id = 3L;
        Chat chat = new Chat(chat_id, REGISTERED.toString());

        chatsRepository.addChat(chat);

        Chat foundChat = chatsRepository.getChatById(chat_id);
        assertEquals(foundChat, chat);

        chatsRepository.deleteChat(chat);
    }

    @Test
    @Transactional
    @Rollback
    void getAllChatsTest() {
        long chat_id1 = 4L;
        long chat_id2 = 5L;
        long chat_id3 = 6L;
        Chat chat1 = new Chat(chat_id1, REGISTERED.toString());
        Chat chat2 = new Chat(chat_id2, REGISTERED.toString());
        Chat chat3 = new Chat(chat_id3, REGISTERED.toString());

        List<Chat> chatsBefore = chatsRepository.getAllChats();
        chatsRepository.addChat(chat1);
        chatsRepository.addChat(chat2);
        chatsRepository.addChat(chat3);
        List<Chat> chatsAfter = chatsRepository.getAllChats();

        assertEquals(chatsAfter.size() - chatsBefore.size(), 3);

        chatsRepository.deleteChat(chat1);
        chatsRepository.deleteChat(chat2);
        chatsRepository.deleteChat(chat3);
    }

    @Test
    @Transactional
    @Rollback
    void duplicateKeyTest() {
        long chat_id = 7L;
        Chat chat = new Chat(chat_id, REGISTERED.toString());
        boolean isChatAdded;

        isChatAdded = chatsRepository.addChat(chat);
        assertTrue(isChatAdded);
        isChatAdded = chatsRepository.addChat(chat);
        assertFalse(isChatAdded);

        chatsRepository.deleteChat(chat);
    }
}
