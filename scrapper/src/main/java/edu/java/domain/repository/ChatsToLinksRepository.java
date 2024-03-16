package edu.java.domain.repository;

import edu.java.domain.dto.Link;
import edu.java.domain.dto.LinkChat;
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
    public void addLink(@NotNull LinkChat linkChat) {
        String sql = "INSERT INTO chats_to_links (chat_id, link_id) VALUES (?, ?)";
        jdbcTemplate.queryForRowSet(sql, linkChat.chatId(), linkChat.linkId());
    }

    @Transactional
    public void removeLink(@NotNull LinkChat linkChat) {
        String sql = "DELETE FROM chats_to_links WHERE chat_id = ? AND link_id = ?";
        jdbcTemplate.update(sql, linkChat.chatId(), linkChat.linkId());
    }

    @Transactional
    public void removeChat(@NotNull LinkChat linkChat) {
        String sql = "DELETE FROM chats_to_links WHERE chat_id = ?";
        jdbcTemplate.update(sql, linkChat.chatId());
    }

    @Transactional
    public List<Link> findAllLinksByChat(@NotNull LinkChat linkChat) {
        String sql = "SELECT * FROM chats_to_links WHERE chat_id = ?";
        return jdbcTemplate.query(sql, linkRowMapper, linkChat.chatId());
    }
}
