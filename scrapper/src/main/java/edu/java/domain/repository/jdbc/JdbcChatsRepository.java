package edu.java.domain.repository.jdbc;

import edu.java.domain.dto.Chat;
import edu.java.domain.repository.ChatsRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
public class JdbcChatsRepository implements ChatsRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Chat> chatsRowMapper;

    @Override
    @Transactional
    public boolean addChat(Chat chat) {
        String sql = "INSERT INTO chats VALUES (?)";
        boolean result = false;
        try {
            result = (jdbcTemplate.update(sql, chat.getChatId()) != 0);
        } catch (DataAccessException | NullPointerException e) {
            log.error("Chat addition error!");
        }
        return result;
    }

    @Override
    @Transactional
    public boolean deleteChat(Chat chat) {
        String sql = "DELETE FROM chats WHERE chat_id = ?";
        boolean result = false;
        try {
            result = (jdbcTemplate.update(sql, chat.getChatId()) != 0);
        } catch (DataAccessException | NullPointerException e) {
            log.error("Chat deletion error!");
        }
        return result;
    }

    @Override
    @Transactional
    public Chat getChatById(long chatId) {
        String sql = "SELECT * FROM chats WHERE chat_id = ?";
        Chat chat = null;
        try {
            chat = jdbcTemplate.queryForObject(sql, chatsRowMapper, chatId);
        } catch (DataAccessException | NullPointerException e) {
            log.error("Chat with id " + chatId + " is not found!");
        }
        return chat;
    }

    @Override
    @Transactional
    public List<Chat> getAllChats() {
        String sql = "SELECT * FROM chats";
        return jdbcTemplate.query(sql, chatsRowMapper);
    }
}
