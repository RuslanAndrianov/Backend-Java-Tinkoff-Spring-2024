package edu.java.scrapper.repositoryTest;

import edu.java.domain.dto.Link;
import edu.java.domain.repository.LinksRepository;
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

public class LinksRepositoryTest extends IntegrationTest {

    private static final LinksRepository linksRepository;
    private static final RowMapper<Link> linkRowMapper = (resultSet, rowNum) ->
            new Link(
                resultSet.getLong("link_id"),
                resultSet.getString("url"),
                timestampToOffsetDate(resultSet.getTimestamp("last_updated")),
                timestampToOffsetDate(resultSet.getTimestamp("last_checked"))
            );

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

        linksRepository = new LinksRepository(jdbcTemplate, linkRowMapper);
    }

    @Test
    @Transactional
    @Rollback
    void addLinkTest() {
        long link_id = 1L;
        String url = "https://stackoverflow.com/questions/54378414/how-to-fix-cant-infer-the-sql-type-to-use-for-an-instance-of-enum-error-when";
        Link link = new Link(link_id, url, OffsetDateTime.now(), OffsetDateTime.now());
        boolean isLinkAdded;

        List<Link> linksBefore = linksRepository.getAllLinks();
        isLinkAdded = linksRepository.addLink(link);
        List<Link> linksAfter = linksRepository.getAllLinks();

        assertTrue(isLinkAdded);
        assertEquals(linksAfter.size() - linksBefore.size(), 1);
        assertEquals(linksAfter.getLast().linkId(), link_id);
        assertEquals(linksAfter.getLast().url(), url);

        linksRepository.deleteLink(link);
    }

    @Test
    @Transactional
    @Rollback
    void deleteLinkTest() {
        long link_id = 2L;
        String url = "https://stackoverflow.com/questions/54378414/how-to-fix-cant-infer-the-sql-type-to-use-for-an-instance-of-enum-error-when";
        Link link  = new Link(link_id, url, OffsetDateTime.now(), OffsetDateTime.now());
        boolean isLinkDeleted;

        List<Link> linksBefore = linksRepository.getAllLinks();
        linksRepository.addLink(link);
        isLinkDeleted = linksRepository.deleteLink(link);
        List<Link> linksAfter = linksRepository.getAllLinks();

        assertTrue(isLinkDeleted);
        assertEquals(linksBefore.size(), linksAfter.size());
    }

    @Test
    @Transactional
    @Rollback
    void getLinkByIdTest() {
        long link_id = 3L;
        String url = "https://github.com/RuslanAndrianov/Backend-Java-Tinkoff-Spring-2024";
        Link link = new Link(link_id, url, OffsetDateTime.now(), OffsetDateTime.now());

        linksRepository.addLink(link);

        Link foundLink = linksRepository.getLinkById(link_id);
        assertEquals(foundLink.linkId(), link.linkId());
        assertEquals(foundLink.url(), link.url());

        linksRepository.deleteLink(link);
    }

    @Test
    @Transactional
    @Rollback
    void getLinkByURLTest() {
        long link_id = 4L;
        String url = "https://github.com/RuslanAndrianov/Backend-Java-Tinkoff-Spring-2024";
        Link link = new Link(link_id, url, OffsetDateTime.now(), OffsetDateTime.now());

        linksRepository.addLink(link);

        Link foundLink = linksRepository.getLinkByURL(url);
        assertEquals(foundLink.linkId(), link.linkId());
        assertEquals(foundLink.url(), link.url());

        linksRepository.deleteLink(link);
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
        Link link1 = new Link(link_id1, url1, OffsetDateTime.now(), OffsetDateTime.now());
        Link link2 = new Link(link_id2, url2, OffsetDateTime.now(), OffsetDateTime.now());
        Link link3 = new Link(link_id3, url3, OffsetDateTime.now(), OffsetDateTime.now());

        List<Link> linksBefore = linksRepository.getAllLinks();
        linksRepository.addLink(link1);
        linksRepository.addLink(link2);
        linksRepository.addLink(link3);
        List<Link> linksAfter = linksRepository.getAllLinks();

        assertEquals(linksAfter.size() - linksBefore.size(), 3);

        linksRepository.deleteLink(link1);
        linksRepository.deleteLink(link2);
        linksRepository.deleteLink(link3);
    }

    @Test
    @Transactional
    @Rollback
    void duplicateKeyTest() {
        long link_id = 8L;
        String url = "https://github.com/RuslanAndrianov/Backend-Java-Tinkoff-Spring-2024";
        Link link = new Link(link_id, url, OffsetDateTime.now(), OffsetDateTime.now());
        boolean isLinkAdded;

        isLinkAdded = linksRepository.addLink(link);
        assertTrue(isLinkAdded);
        isLinkAdded = linksRepository.addLink(link);
        assertFalse(isLinkAdded);

        linksRepository.deleteLink(link);
    }
}
