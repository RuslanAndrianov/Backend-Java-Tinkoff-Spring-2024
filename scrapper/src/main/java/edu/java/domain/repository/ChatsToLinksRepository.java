package edu.java.domain.repository;

import edu.java.domain.dto.Chat;
import edu.java.domain.dto.Link;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class ChatsToLinksRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Link> linkRowMapper;

    @Transactional
    public void addLink(@NotNull Chat chat, Link link) {
        String sql = "INSERT INTO chats_to_links VALUES (?, ?)";
        jdbcTemplate.update(sql, chat.chatId(), link.linkId());
    }

    @Transactional
    public void removeLink(@NotNull Chat chat, Link link) {
        String sql = "DELETE FROM chats_to_links WHERE chat_id = ? AND link_id = ?";
        jdbcTemplate.update(sql, chat.chatId(), link.linkId());
    }

    @Transactional
    public void removeChat(@NotNull Chat chat) {
        String sql = "DELETE FROM chats_to_links WHERE chat_id = ?";
        jdbcTemplate.update(sql, chat.chatId());
    }

    @Transactional
    public List<Link> findAllLinksByChat(@NotNull Chat chat) {
        String sql = "select * from chats_to_links join links using (link_id) where chat_id = ?";
        return jdbcTemplate.query(sql, linkRowMapper, chat.chatId());
    }
}
