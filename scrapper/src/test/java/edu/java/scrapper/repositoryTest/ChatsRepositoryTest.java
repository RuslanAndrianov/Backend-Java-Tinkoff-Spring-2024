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

        chatsRepository.add(chat1);
        List<Chat> chats = chatsRepository.findAllChats();

        assertEquals(chats.size(), 1);
        assertEquals(chats.getFirst().chatId(), chat_id1);
        assertEquals(chats.getFirst().chatState(), REGISTERED.toString());

        chatsRepository.remove(chat1);
    }

    @Test
    @Transactional
    @Rollback
    void removeTest() {
        long chat_id2 = 2L;
        Chat chat2 = new Chat(chat_id2, REGISTERED.toString());

        chatsRepository.add(chat2);
        chatsRepository.remove(chat2);
        List<Chat> chats = chatsRepository.findAllChats();

        assertEquals(chats.size(), 0);
    }

    @Test
    @Transactional
    @Rollback
    void findAllTest() {
        long chat_id3 = 3L;
        long chat_id4 = 4L;
        long chat_id5 = 5L;

        Chat chat3 = new Chat(chat_id3, REGISTERED.toString());
        Chat chat4 = new Chat(chat_id4, REGISTERED.toString());
        Chat chat5 = new Chat(chat_id5, REGISTERED.toString());

        chatsRepository.add(chat3);
        chatsRepository.add(chat4);
        chatsRepository.add(chat5);

        List<Chat> chats = chatsRepository.findAllChats();
        assertEquals(chats.size(), 3);

        chatsRepository.remove(chat3);
        chatsRepository.remove(chat4);
        chatsRepository.remove(chat5);
    }

    @Test
    @Transactional
    @Rollback
    void duplicateKeyTest() {
        long chat_id6 = 6L;
        Chat chat6 = new Chat(chat_id6, REGISTERED.toString());
        boolean isError = false;

        chatsRepository.add(chat6);
        try {
            chatsRepository.add(chat6);
        } catch (DuplicateKeyException e) {
            isError = true;
        }

        assertTrue(isError);

        chatsRepository.remove(chat6);
    }
}
