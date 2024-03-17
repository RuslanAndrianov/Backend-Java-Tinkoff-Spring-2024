package edu.java.domain.repository;

import edu.java.domain.dto.Chat;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class ChatsRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Chat> chatsRowMapper;

    @Transactional
    public void add(@NotNull Chat chat) {
        String sql = "INSERT INTO chats (chat_id, chat_state) VALUES (?, ?)";
        jdbcTemplate.update(sql, chat.chatId(), chat.chatState());
    }

    @Transactional
    public void remove(@NotNull Chat chat) {
        String sql = "DELETE FROM chats WHERE chat_id = ?";
        jdbcTemplate.update(sql, chat.chatId());
    }

    @Transactional
    public Chat findChat(long chatId) {
        String sql = "SELECT chat_id, chat_state FROM chats WHERE chat_id = ?";
        return jdbcTemplate.queryForObject(sql, chatsRowMapper, chatId);
    }

    @Transactional
    public List<Chat> findAllChats() {
        String sql = "SELECT chat_id, chat_state FROM chats";
        return jdbcTemplate.query(sql, chatsRowMapper);
    }
}
