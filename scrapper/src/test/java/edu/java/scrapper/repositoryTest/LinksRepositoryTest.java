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
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LinksRepositoryTest extends IntegrationTest {

    private static final LinksRepository linksRepository;
    private static final RowMapper<Link> linkRowMapper = (resultSet, rowNum) ->
            new Link(
                resultSet.getLong("link_id"),
                resultSet.getString("url"),
                timestampToOffsetDate(resultSet.getTimestamp("last_updated"))
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
    void addTest() {
        String url1 = "https://stackoverflow.com/questions/54378414/how-to-fix-cant-infer-the-sql-type-to-use-for-an-instance-of-enum-error-when";

        long link_id1 = 1L;

        Link link1 = new Link(link_id1, url1, OffsetDateTime.now());

        linksRepository.addLink(link1);
        List<Link> links = linksRepository.getAllLinks();

        assertEquals(links.size(), 1);
        assertEquals(links.getFirst().linkId(), link_id1);
        assertEquals(links.getFirst().url(), url1);

        linksRepository.deleteLink(link1);
    }

    @Test
    @Transactional
    @Rollback
    void removeTest() {
        String url2 = "https://stackoverflow.com/questions/54378414/how-to-fix-cant-infer-the-sql-type-to-use-for-an-instance-of-enum-error-when";

        long link_id12 = 2L;

        Link link2  = new Link(link_id12, url2, OffsetDateTime.now());

        linksRepository.addLink(link2);
        linksRepository.deleteLink(link2);
        List<Link> links = linksRepository.getAllLinks();

        assertEquals(links.size(), 0);
    }

    @Test
    @Transactional
    @Rollback
    void findLinkByIdTest() {
        long link_id99 = 99L;
        String url = "https://github.com/RuslanAndrianov/Backend-Java-Tinkoff-Spring-2024";

        Link link99 = new Link(link_id99, url, OffsetDateTime.now());

        linksRepository.addLink(link99);

        Link foundLink = linksRepository.getLinkById(link_id99);
        assertEquals(foundLink.linkId(), link99.linkId());
        assertEquals(foundLink.url(), link99.url());

        linksRepository.deleteLink(link99);
    }

    @Test
    @Transactional
    @Rollback
    void findLinkByURLTest() {
        long link_id999 = 999L;
        String url = "https://github.com/RuslanAndrianov/Backend-Java-Tinkoff-Spring-2024";

        Link link999 = new Link(link_id999, url, OffsetDateTime.now());

        linksRepository.addLink(link999);

        Link foundLink = linksRepository.getLinkByURL(url);
        assertEquals(foundLink.linkId(), link999.linkId());
        assertEquals(foundLink.url(), link999.url());

        linksRepository.deleteLink(link999);
    }

    @Test
    @Transactional
    @Rollback
    void findAllLinksTest() {
        String url3 = "https://stackoverflow.com/questions/54378414/how-to-fix-cant-infer-the-sql-type-to-use-for-an-instance-of-enum-error-when";
        String url4 = "https://stackoverflow.com/questions/50145552/error-org-springframework-jdbc-badsqlgrammarexception-statementcallback-bad-s";
        String url5 = "https://github.com/RuslanAndrianov/Backend-Java-Tinkoff-Spring-2024";

        long link_id3 = 3L;
        long link_id4 = 4L;
        long link_id5 = 5L;

        Link link3 = new Link(link_id3, url3, OffsetDateTime.now());
        Link link4 = new Link(link_id4, url4, OffsetDateTime.now());
        Link link5 = new Link(link_id5, url5, OffsetDateTime.now());

        linksRepository.addLink(link3);
        linksRepository.addLink(link4);
        linksRepository.addLink(link5);

        List<Link> links = linksRepository.getAllLinks();
        assertEquals(links.size(), 3);

        linksRepository.deleteLink(link3);
        linksRepository.deleteLink(link4);
        linksRepository.deleteLink(link5);
    }

    @Test
    @Transactional
    @Rollback
    void duplicateKeyTest() {
        String url6 = "https://github.com/RuslanAndrianov/Backend-Java-Tinkoff-Spring-2024";

        long link_id6 = 6L;

        Link link6 = new Link(link_id6, url6, OffsetDateTime.now());
        boolean isError = false;

        linksRepository.addLink(link6);
        try {
            linksRepository.addLink(link6);
        } catch (DuplicateKeyException e) {
            isError = true;
        }

        assertTrue(isError);

        linksRepository.deleteLink(link6);
    }
}
