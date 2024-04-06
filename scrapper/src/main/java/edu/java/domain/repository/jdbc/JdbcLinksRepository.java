package edu.java.domain.repository.jdbc;

import edu.java.domain.dto.Link;
import edu.java.domain.repository.LinksRepository;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import static edu.java.utils.TimeCorrecter.getCorrectedTime;

@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
@Repository
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings("MultipleStringLiterals")
public class JdbcLinksRepository implements LinksRepository {

    // ВАЖНО!!! JDBC заносит смещенное время в таблицу (например, московское 13:45 +03:00).
    // В то время как JPA вносит несмещенное время (10:45 +00:00)
    // В БД мы хотим хранить несмещенное время и смещение, поэтому корректируем время

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Link> linksRowMapper;

    @Override
    @Transactional
    public boolean addLink(Link link) {
        String sql = "INSERT INTO links VALUES (?, ?, ?, ?, ?)";
        boolean result = false;
        try {
            OffsetDateTime correctedLastUpdated = getCorrectedTime(link.getLastUpdated(), link.getZoneOffset());
            OffsetDateTime correctedLastChecked = getCorrectedTime(link.getLastChecked(), link.getZoneOffset());
            result = (jdbcTemplate.update(
                sql,
                link.getLinkId(), link.getUrl(), correctedLastUpdated,
                correctedLastChecked, link.getZoneOffset()
            ) != 0);
        } catch (DataAccessException | NullPointerException e) {
            log.error("Link addition error!");
        }
        return result;
    }

    @Override
    @Transactional
    public boolean deleteLink(Link link) {
        String sql = "DELETE FROM links WHERE link_id = ?";
        boolean result = false;
        try {
            result = (jdbcTemplate.update(sql, link.getLinkId()) != 0);
        } catch (DataAccessException | NullPointerException e) {
            log.error("Link deletion error!");
        }
        return result;
    }

    @Override
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

    @Override
    @Transactional
    public Link getLinkByUrl(String url) {
        String sql = "SELECT * FROM links WHERE url = ?";
        Link link = null;
        try {
            link = jdbcTemplate.queryForObject(sql, linksRowMapper, url);
        } catch (DataAccessException | NullPointerException e) {
            log.error("Link " + url + " is not found!");
        }
        return link;
    }

    @Override
    @Transactional
    public List<Link> getAllLinks() {
        String sql = "SELECT * FROM links";
        return jdbcTemplate.query(sql, linksRowMapper);
    }

    @Override
    @Transactional
    public List<Link> getOldestCheckedLinks(String interval) {
        String sql = "SELECT * FROM links WHERE (last_checked "
            + "+ make_time(zone_offset/3600, (zone_offset - (zone_offset/3600) * 3600)/60, 0)"
            + "< now() - interval '" + interval + "')";
        return jdbcTemplate.query(sql, linksRowMapper);
    }

    @Override
    @Transactional
    public boolean setLastCheckedTimeToLink(Link link, OffsetDateTime time) {
        String sql = "UPDATE links SET last_checked = ? WHERE link_id = ?";
        boolean result = false;
        try {
            OffsetDateTime correctedTime = getCorrectedTime(time, link.getZoneOffset());
            result = (jdbcTemplate.update(sql, correctedTime, link.getLinkId()) != 0);
        } catch (DataAccessException | NullPointerException e) {
            log.error("Link's last_checked field update error!");
        }
        return result;
    }

    @Override
    @Transactional
    public boolean setLastUpdatedTimeToLink(Link link, OffsetDateTime time) {
        String sql = "UPDATE links SET last_updated = ? WHERE link_id = ?";
        boolean result = false;
        try {
            OffsetDateTime correctedTime = getCorrectedTime(time, link.getZoneOffset());
            result = (jdbcTemplate.update(sql, correctedTime, link.getLinkId()) != 0);
        } catch (DataAccessException | NullPointerException e) {
            log.error("Link's last_updated field update error!");
        }
        return result;
    }
}
