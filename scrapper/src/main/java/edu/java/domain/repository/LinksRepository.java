package edu.java.domain.repository;

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
public class LinksRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Link> linksRowMapper;

    @Transactional
    public void add(@NotNull Link link) {
        String sql = "INSERT INTO links (link_id, url, last_updated) VALUES (?, ?, DEFAULT)";
        jdbcTemplate.update(sql, link.linkId(), link.url());
    }

    @Transactional
    public void remove(@NotNull Link link) {
        String sql = "DELETE FROM links WHERE link_id = ?";
        jdbcTemplate.update(sql, link.linkId());
    }

    @Transactional
    public Link findLinkById(long linkId) {
        String sql = "SELECT link_id, url, last_updated FROM links WHERE link_id = ?";
        return jdbcTemplate.queryForObject(sql, linksRowMapper, linkId);
    }

    @Transactional
    public Link findLinkByURL(String url) {
        String sql = "SELECT link_id, url, last_updated FROM links WHERE url = ?";
        return jdbcTemplate.queryForObject(sql, linksRowMapper, url);
    }

    @Transactional
    public List<Link> findAllLinks() {
        String sql = "SELECT * FROM links";
        return jdbcTemplate.query(sql, linksRowMapper);
    }
}
