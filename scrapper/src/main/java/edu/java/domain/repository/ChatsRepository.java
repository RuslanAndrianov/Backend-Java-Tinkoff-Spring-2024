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
        String sql = "INSERT INTO chats (chat_id, state) VALUES (?, ?)";
        jdbcTemplate.update(sql, chat.chatId(), chat.chatState());
    }

    @Transactional
    public void remove(@NotNull Chat chat) {
        String sql = "DELETE FROM chats WHERE id = ?";
        jdbcTemplate.update(sql, chat.chatId());
    }

    @Transactional
    public List<Chat> findAllChats() {
        String sql = "SELECT * FROM chats";
        return jdbcTemplate.query(sql, chatsRowMapper);
    }
}
