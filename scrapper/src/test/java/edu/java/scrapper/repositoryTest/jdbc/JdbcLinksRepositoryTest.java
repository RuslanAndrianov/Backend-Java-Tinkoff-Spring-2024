package edu.java.scrapper.repositoryTest.jdbc;

import edu.java.domain.dto.Link;
import edu.java.domain.repository.jdbc.JdbcLinksRepository;
import edu.java.scrapper.IntegrationTest;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JdbcLinksRepositoryTest extends IntegrationTest {

    private static final JdbcLinksRepository JDBC_LINKS_REPOSITORY;
    private static final RowMapper<Link> linkRowMapper = (resultSet, rowNum) -> {
        Link link = new Link();
        link.setLinkId(resultSet.getLong("link_id"));
        link.setUrl(resultSet.getString("url"));
        link.setLastUpdated(
            timestampToOffsetDate(resultSet.getTimestamp("last_updated")));
        link.setLastChecked(
            timestampToOffsetDate(resultSet.getTimestamp("last_checked")));
        link.setZoneOffset(resultSet.getInt("zone_offset"));
        return link;
    };

    private static OffsetDateTime timestampToOffsetDate(@NotNull Timestamp timestamp) {
        return OffsetDateTime.of(timestamp.toLocalDateTime(), ZoneOffset.of("Z"));
    }

    static {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSourceBuilder
            .create()
            .url(POSTGRES.getJdbcUrl())
            .username(POSTGRES.getUsername())
            .password(POSTGRES.getPassword())
            .build()
        );

        JDBC_LINKS_REPOSITORY = new JdbcLinksRepository(jdbcTemplate, linkRowMapper);
    }

    @Test
    @Transactional
    @Rollback
    void addLinkTest() {
        long link_id = 1L;
        String url = "https://stackoverflow.com/questions/54378414/how-to-fix-cant-infer-the-sql-type-to-use-for-an-instance-of-enum-error-when";

        Link link = new Link();
        link.setLinkId(link_id);
        link.setUrl(url);
        link.setLastUpdated(OffsetDateTime.now());
        link.setLastChecked(OffsetDateTime.now());
        link.setZoneOffset(0);

        boolean isLinkAdded;

        List<Link> linksBefore = JDBC_LINKS_REPOSITORY.getAllLinks();
        isLinkAdded = JDBC_LINKS_REPOSITORY.addLink(link);
        List<Link> linksAfter = JDBC_LINKS_REPOSITORY.getAllLinks();

        assertTrue(isLinkAdded);
        assertEquals(linksAfter.size() - linksBefore.size(), 1);
        assertEquals(linksAfter.getLast().getLinkId(), link_id);
        assertEquals(linksAfter.getLast().getUrl(), url);

        JDBC_LINKS_REPOSITORY.deleteLink(link);
    }

    @Test
    @Transactional
    @Rollback
    void deleteLinkTest() {
        long link_id = 2L;
        String url = "https://stackoverflow.com/questions/54378414/how-to-fix-cant-infer-the-sql-type-to-use-for-an-instance-of-enum-error-when";

        Link link = new Link();
        link.setLinkId(link_id);
        link.setUrl(url);
        link.setLastUpdated(OffsetDateTime.now());
        link.setLastChecked(OffsetDateTime.now());
        link.setZoneOffset(0);

        boolean isLinkDeleted;

        List<Link> linksBefore = JDBC_LINKS_REPOSITORY.getAllLinks();
        JDBC_LINKS_REPOSITORY.addLink(link);
        isLinkDeleted = JDBC_LINKS_REPOSITORY.deleteLink(link);
        List<Link> linksAfter = JDBC_LINKS_REPOSITORY.getAllLinks();

        assertTrue(isLinkDeleted);
        assertEquals(linksBefore.size(), linksAfter.size());
    }

    @Test
    @Transactional
    @Rollback
    void getLinkByIdTest() {
        long link_id = 3L;
        String url = "https://github.com/RuslanAndrianov/Backend-Java-Tinkoff-Spring-2024";

        Link link = new Link();
        link.setLinkId(link_id);
        link.setUrl(url);
        link.setLastUpdated(OffsetDateTime.now());
        link.setLastChecked(OffsetDateTime.now());
        link.setZoneOffset(0);

        JDBC_LINKS_REPOSITORY.addLink(link);

        Link foundLink = JDBC_LINKS_REPOSITORY.getLinkById(link_id);
        assertEquals(foundLink.getLinkId(), link.getLinkId());
        assertEquals(foundLink.getUrl(), link.getUrl());

        JDBC_LINKS_REPOSITORY.deleteLink(link);
    }

    @Test
    @Transactional
    @Rollback
    void getLinkByURLTest() {
        long link_id = 4L;
        String url = "https://github.com/RuslanAndrianov/Backend-Java-Tinkoff-Spring-2024";

        Link link = new Link();
        link.setLinkId(link_id);
        link.setUrl(url);
        link.setLastUpdated(OffsetDateTime.now());
        link.setLastChecked(OffsetDateTime.now());
        link.setZoneOffset(0);

        JDBC_LINKS_REPOSITORY.addLink(link);

        Link foundLink = JDBC_LINKS_REPOSITORY.getLinkByUrl(url);
        assertEquals(foundLink.getLinkId(), link.getLinkId());
        assertEquals(foundLink.getUrl(), link.getUrl());

        JDBC_LINKS_REPOSITORY.deleteLink(link);
    }

    @Test
    @Transactional
    @Rollback
    void getAllLinksTest() {
        long link_id1 = 5L;
        long link_id2 = 6L;
        long link_id3 = 7L;
        String url1 = "https://stackoverflow.com/questions/54378414/how-to-fix-cant-infer-the-sql-type-to-use-for-an-instance-of-enum-error-when";
        String url2 = "https://stackoverflow.com/questions/50145552/error-org-springframework-jdbc-badsqlgrammarexception-statementcallback-bad-s";
        String url3 = "https://github.com/RuslanAndrianov/Backend-Java-Tinkoff-Spring-2024";

        Link link1 = new Link();
        link1.setLinkId(link_id1);
        link1.setUrl(url1);
        link1.setLastUpdated(OffsetDateTime.now());
        link1.setLastChecked(OffsetDateTime.now());
        link1.setZoneOffset(0);

        Link link2 = new Link();
        link2.setLinkId(link_id2);
        link2.setUrl(url2);
        link2.setLastUpdated(OffsetDateTime.now());
        link2.setLastChecked(OffsetDateTime.now());
        link2.setZoneOffset(0);

        Link link3 = new Link();
        link3.setLinkId(link_id3);
        link3.setUrl(url3);
        link3.setLastUpdated(OffsetDateTime.now());
        link3.setLastChecked(OffsetDateTime.now());
        link3.setZoneOffset(0);

        List<Link> linksBefore = JDBC_LINKS_REPOSITORY.getAllLinks();
        JDBC_LINKS_REPOSITORY.addLink(link1);
        JDBC_LINKS_REPOSITORY.addLink(link2);
        JDBC_LINKS_REPOSITORY.addLink(link3);
        List<Link> linksAfter = JDBC_LINKS_REPOSITORY.getAllLinks();

        assertEquals(linksAfter.size() - linksBefore.size(), 3);

        JDBC_LINKS_REPOSITORY.deleteLink(link1);
        JDBC_LINKS_REPOSITORY.deleteLink(link2);
        JDBC_LINKS_REPOSITORY.deleteLink(link3);
    }

    @Test
    @Transactional
    @Rollback
    void duplicateKeyTest() {
        long link_id = 8L;
        String url = "https://github.com/RuslanAndrianov/Backend-Java-Tinkoff-Spring-2024";

        Link link = new Link();
        link.setLinkId(link_id);
        link.setUrl(url);
        link.setLastUpdated(OffsetDateTime.now());
        link.setLastChecked(OffsetDateTime.now());
        link.setZoneOffset(0);

        boolean isLinkAdded;

        isLinkAdded = JDBC_LINKS_REPOSITORY.addLink(link);
        assertTrue(isLinkAdded);
        isLinkAdded = JDBC_LINKS_REPOSITORY.addLink(link);
        assertFalse(isLinkAdded);

        JDBC_LINKS_REPOSITORY.deleteLink(link);
    }
}
