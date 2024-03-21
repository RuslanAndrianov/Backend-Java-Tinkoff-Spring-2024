package edu.java.scrapper.repositoryTest;

import edu.java.domain.dto.Chat;
import edu.java.domain.repository.ChatsRepository;
import edu.java.scrapper.IntegrationTest;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static edu.shared_dto.ChatState.REGISTERED;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
    void addTest() {
        long chat_id1 = 1L;
        Chat chat1 = new Chat(chat_id1, REGISTERED.toString());

        chatsRepository.addChat(chat1);
        List<Chat> chats = chatsRepository.getAllChats();

        assertEquals(chats.size(), 1);
        assertEquals(chats.getFirst().chatId(), chat_id1);
        assertEquals(chats.getFirst().chatState(), REGISTERED.toString());

        chatsRepository.deleteChat(chat1);
    }

    @Test
    @Transactional
    @Rollback
    void removeTest() {
        long chat_id2 = 2L;
        Chat chat2 = new Chat(chat_id2, REGISTERED.toString());

        chatsRepository.addChat(chat2);
        chatsRepository.deleteChat(chat2);
        List<Chat> chats = chatsRepository.getAllChats();

        assertEquals(chats.size(), 0);
    }

    @Test
    @Transactional
    @Rollback
    void findChatTest() {
        long chat_id99 = 99L;

        Chat chat99 = new Chat(chat_id99, REGISTERED.toString());

        chatsRepository.addChat(chat99);

        Chat foundChat = chatsRepository.getChatById(chat_id99);
        assertEquals(foundChat, chat99);

        chatsRepository.deleteChat(chat99);
    }

    @Test
    @Transactional
    @Rollback
    void findAllChatsTest() {
        long chat_id3 = 3L;
        long chat_id4 = 4L;
        long chat_id5 = 5L;

        Chat chat3 = new Chat(chat_id3, REGISTERED.toString());
        Chat chat4 = new Chat(chat_id4, REGISTERED.toString());
        Chat chat5 = new Chat(chat_id5, REGISTERED.toString());

        chatsRepository.addChat(chat3);
        chatsRepository.addChat(chat4);
        chatsRepository.addChat(chat5);

        List<Chat> chats = chatsRepository.getAllChats();
        assertEquals(chats.size(), 3);

        chatsRepository.deleteChat(chat3);
        chatsRepository.deleteChat(chat4);
        chatsRepository.deleteChat(chat5);
    }

    @Test
    @Transactional
    @Rollback
    void duplicateKeyTest() {
        long chat_id6 = 6L;
        Chat chat6 = new Chat(chat_id6, REGISTERED.toString());
        boolean isError = false;

        chatsRepository.addChat(chat6);
        try {
            chatsRepository.addChat(chat6);
        } catch (DuplicateKeyException e) {
            isError = true;
        }

        assertTrue(isError);

        chatsRepository.deleteChat(chat6);
    }
}
