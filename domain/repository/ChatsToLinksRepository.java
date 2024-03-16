package edu.java.domain.repository;

import edu.java.domain.dto.LinkChat;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class ChatsToLinksRepository {

    private final JdbcTemplate jdbcTemplate;

    public ChatsToLinksRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void add(@NotNull LinkChat linkChat) {
        String sql = "INSERT INTO chats_to_links (chat_id, link_id) VALUES (?, ?)";
        jdbcTemplate.queryForRowSet(sql, linkChat.chatId(), linkChat.linkId());
    }

    public void delete(@NotNull LinkChat linkChat) {
        String sql = "DELETE FROM chats_to_links WHERE chat_id = ? AND link_id = ?";
        jdbcTemplate.update(sql, linkChat.chatId(), linkChat.linkId());
    }

    public List<?> findAll() {
        return null;
    }
}
