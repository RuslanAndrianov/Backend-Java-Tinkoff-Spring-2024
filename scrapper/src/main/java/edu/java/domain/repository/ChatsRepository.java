package edu.java.domain.repository;

import edu.java.domain.dto.Chat;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ChatsRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ChatsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void add(@NotNull Chat chat) {
        String sql = "INSERT INTO chats (chat_id, state) VALUES (?, ?)";
        jdbcTemplate.queryForRowSet(sql, chat.chatId());//, chat.chatState());
    }

    public void delete(@NotNull Chat chat) {
        String sql = "DELETE FROM chats WHERE id = ?";
        jdbcTemplate.update(sql, chat.chatId());
    }

    public List<Chat> findAll() {
        return null;
    }
}
