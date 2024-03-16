package edu.java.domain.repository;

import java.time.OffsetDateTime;
import java.util.List;
import edu.java.domain.dto.Link;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class LinksRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public LinksRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void add(@NotNull Link link) {
        String sql = "INSERT INTO links (link_id, url, last_updated) VALUES (?, ?)";
        jdbcTemplate.update(sql, link.linkId(), link.url());
    }

    public void remove(@NotNull Link link) {
        String sql = "DELETE FROM links WHERE id = ?";
        jdbcTemplate.update(sql, link.linkId());
    }

    public List<Link> findAll() {
        String sql = "SELECT link_id, url, link_id FROM links";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
            new Link(
                rs.getLong("link_id"),
                rs.getURL("url"),
                rs.getInt(1)//rs.getTimestamp("link_id")
            )
        );
    }
}
