package edu.java.domain.repository;

import edu.java.domain.dto.Link;
import java.time.OffsetDateTime;
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
@SuppressWarnings("MultipleStringLiterals")
public class LinksRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Link> linksRowMapper;

    @Transactional
    public boolean addLink(Link link) {
        String sql = "INSERT INTO links VALUES (?, ?, DEFAULT)";
        boolean result = false;
        try {
            result = (jdbcTemplate.update(sql, link.linkId(), link.url()) != 0);
        } catch (DataAccessException | NullPointerException e) {
            log.error("Link addition error!");
        }
        return result;
    }

    @Transactional
    public boolean deleteLink(Link link) {
        String sql = "DELETE FROM links WHERE link_id = ?";
        boolean result = false;
        try {
            result = (jdbcTemplate.update(sql, link.linkId()) != 0);
        } catch (DataAccessException | NullPointerException e) {
            log.error("Link deletion error!");
        }
        return result;
    }

    @Transactional
    public Link getLinkById(long linkId) {
        String sql = "SELECT * FROM links WHERE link_id = ?";
        Link link = null;
        try {
            link = jdbcTemplate.queryForObject(sql, linksRowMapper, linkId);
        } catch (DataAccessException | NullPointerException e) {
            log.error("Link with id " + linkId + " is not found!");
        }
        return link;
    }

    @Transactional
    public Link getLinkByURL(String url) {
        String sql = "SELECT * FROM links WHERE url = ?";
        Link link = null;
        try {
            link = jdbcTemplate.queryForObject(sql, linksRowMapper, url);
        } catch (DataAccessException | NullPointerException e) {
            log.error("Link " + url + " is not found!");
        }
        return link;
    }

    @Transactional
    public List<Link> getAllLinks() {
        String sql = "SELECT * FROM links";
        return jdbcTemplate.query(sql, linksRowMapper);
    }

    @Transactional
    public List<Link> getOldestCheckedLinks(String interval) {
        String sql =
            "SELECT * FROM links WHERE (last_checked < now() - interval '" + interval + "')";
        return jdbcTemplate.query(sql, linksRowMapper);
    }

    @Transactional
    public boolean setLastCheckedTimeToLink(Link link, OffsetDateTime time) {
        String sql = "UPDATE links SET last_checked = ? WHERE link_id = ?";
        boolean result = false;
        try {
            result = (jdbcTemplate.update(sql, time, link.linkId()) != 0);
        } catch (DataAccessException | NullPointerException e) {
            log.error("Link's last_checked field update error!");
        }
        return result;
    }

    @Transactional
    public boolean setLastUpdatedTimeToLink(Link link, OffsetDateTime time) {
        String sql = "UPDATE links SET last_updated = ? WHERE link_id = ?";
        boolean result = false;
        try {
            result = (jdbcTemplate.update(sql, time, link.linkId()) != 0);
        } catch (DataAccessException | NullPointerException e) {
            log.error("Link's last_updated field update error!");
        }
        return result;
    }
}
