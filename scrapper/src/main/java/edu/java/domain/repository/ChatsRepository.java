package edu.java.domain.repository;

import edu.java.domain.dto.Chat;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ChatsRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Chat> chatsRowMapper;

    @Transactional
    public boolean addChat(Chat chat) {
        String sql = "INSERT INTO chats (chat_id, chat_state) VALUES (?, ?)";
        boolean result = false;
        try {
            result = (jdbcTemplate.update(sql, chat.chatId(), chat.chatState()) != 0);
        } catch (DataAccessException | NullPointerException e) {
            log.error("Chat addition error!");
        }
        return result;
    }

    @Transactional
    public boolean deleteChat(Chat chat) {
        String sql = "DELETE FROM chats WHERE chat_id = ?";
        boolean result = false;
        try {
            result = (jdbcTemplate.update(sql, chat.chatId()) != 0);
        } catch (DataAccessException | NullPointerException e) {
            log.error("Chat deletion error!");
        }
        return result;
    }

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

    @Transactional
    public List<Chat> getAllChats() {
        String sql = "SELECT * FROM chats";
        return jdbcTemplate.query(sql, chatsRowMapper);
    }
}
