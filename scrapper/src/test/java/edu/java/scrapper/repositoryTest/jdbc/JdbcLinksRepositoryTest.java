package edu.java.scrapper.repositoryTest.jdbc;

import edu.java.scrapper.domain.dto.Link;
import edu.java.scrapper.domain.repository.jdbc.JdbcLinksRepository;
import edu.java.scrapper.IntegrationEnvironment;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(properties = "app.database-access-type=jdbc")
public class JdbcLinksRepositoryTest extends IntegrationEnvironment {

    @Autowired
    private JdbcLinksRepository jdbcLinksRepository;

    @Test
    @Transactional
    @Rollback
    void addLinkTest() {

        // Arrange
        long link_id = 1L;
        String url = "https://stackoverflow.com/questions/54378414/how-to-fix-cant-infer-the-sql-type-to-use-for-an-instance-of-enum-error-when";

        Link link = new Link();
        link.setLinkId(link_id);
        link.setUrl(url);
        link.setLastUpdated(OffsetDateTime.now());
        link.setLastChecked(OffsetDateTime.now());
        link.setZoneOffset(0);

        boolean isLinkAdded;

        // Act
        List<Link> linksBefore = jdbcLinksRepository.getAllLinks();
        isLinkAdded = jdbcLinksRepository.addLink(link);
        List<Link> linksAfter = jdbcLinksRepository.getAllLinks();

        // Assert
        assertTrue(isLinkAdded);
        assertEquals(linksAfter.size() - linksBefore.size(), 1);
        assertEquals(linksAfter.getLast().getLinkId(), link_id);
        assertEquals(linksAfter.getLast().getUrl(), url);
    }

    @Test
    @Transactional
    @Rollback
    void deleteLinkTest() {

        // Arrange
        long link_id = 2L;
        String url = "https://stackoverflow.com/questions/54378414/how-to-fix-cant-infer-the-sql-type-to-use-for-an-instance-of-enum-error-when";

        Link link = new Link();
        link.setLinkId(link_id);
        link.setUrl(url);
        link.setLastUpdated(OffsetDateTime.now());
        link.setLastChecked(OffsetDateTime.now());
        link.setZoneOffset(0);

        boolean isLinkDeleted;

        // Act
        List<Link> linksBefore = jdbcLinksRepository.getAllLinks();
        jdbcLinksRepository.addLink(link);
        isLinkDeleted = jdbcLinksRepository.deleteLink(link);
        List<Link> linksAfter = jdbcLinksRepository.getAllLinks();

        // Assert
        assertTrue(isLinkDeleted);
        assertEquals(linksBefore.size(), linksAfter.size());
    }


    @Test
    @Transactional
    @Rollback
    void getLinkByIdTest() {

        // Arrange
        long link_id = 3L;
        String url = "https://vk.com";

        Link link = new Link();
        link.setLinkId(link_id);
        link.setUrl(url);
        link.setLastUpdated(OffsetDateTime.now());
        link.setLastChecked(OffsetDateTime.now());
        link.setZoneOffset(0);

        // Act
        jdbcLinksRepository.addLink(link);
        Link foundLink = jdbcLinksRepository.getLinkById(link_id);

        // Assert
        assertEquals(foundLink.getLinkId(), link.getLinkId());
        assertEquals(foundLink.getUrl(), link.getUrl());
    }

    @Test
    @Transactional
    @Rollback
    void getLinkByUrlTest() {

        // Arrange
        long link_id = 4L;
        String url = "https://github.com/";

        Link link = new Link();
        link.setLinkId(link_id);
        link.setUrl(url);
        link.setLastUpdated(OffsetDateTime.now());
        link.setLastChecked(OffsetDateTime.now());
        link.setZoneOffset(0);

        // Act
        jdbcLinksRepository.addLink(link);
        Link foundLink = jdbcLinksRepository.getLinkByUrl(url);

        // Assert
        assertEquals(foundLink.getLinkId(), link.getLinkId());
        assertEquals(foundLink.getUrl(), link.getUrl());
    }


    @Test
    @Transactional
    @Rollback
    void getAllLinksTest() {

        // Arrange
        long link_id1 = 5L;
        long link_id2 = 6L;
        long link_id3 = 7L;
        String url1 = "https://stackoverflow.com/questions/54378414/how-to-fix-cant-infer-the-sql-type-to-use-for-an-instance-of-enum-error-when";
        String url2 = "https://stackoverflow.com/questions/50145552/error-org-springframework-jdbc-badsqlgrammarexception-statementcallback-bad-s";
        String url3 = "https://vk.com";

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

        // Act
        List<Link> linksBefore = jdbcLinksRepository.getAllLinks();
        jdbcLinksRepository.addLink(link1);
        jdbcLinksRepository.addLink(link2);
        jdbcLinksRepository.addLink(link3);
        List<Link> linksAfter = jdbcLinksRepository.getAllLinks();

        // Assert
        assertEquals(linksAfter.size() - linksBefore.size(), 3);
    }

    @Test
    @Transactional
    @Rollback
    void getOldestCheckedLinksTest() {

        // Arrange
        long link_id1 = 8L;
        long link_id2 = 9L;
        long link_id3 = 10L;
        String url1 =
            "https://stackoverflow.com/questions/54378414/how-to-fix-cant-infer-the-sql-type-to-use-for-an-instance-of-enum-error-when";
        String url2 =
            "https://stackoverflow.com/questions/50145552/error-org-springframework-jdbc-badsqlgrammarexception-statementcallback-bad-s";
        String url3 =
            "https://github.com/unlessiamwrong/Tinkoff-Java-course-2";

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
        link2.setLastChecked(OffsetDateTime.now().minusMinutes(10));
        link2.setZoneOffset(0);

        Link link3 = new Link();
        link3.setLinkId(link_id3);
        link3.setUrl(url3);
        link3.setLastUpdated(OffsetDateTime.now());
        link3.setLastChecked(OffsetDateTime.now().minusHours(2));
        link3.setZoneOffset(0);

        // Act
        jdbcLinksRepository.addLink(link1);
        jdbcLinksRepository.addLink(link2);
        jdbcLinksRepository.addLink(link3);

        List<Long> linkIds5min = jdbcLinksRepository
            .getOldestCheckedLinks("5 minutes")
            .stream()
            .map(Link::getLinkId)
            .toList();
        List<Long> linkIds1hour = jdbcLinksRepository
            .getOldestCheckedLinks("1 hour")
            .stream()
            .map(Link::getLinkId)
            .toList();

        // Assert
        assertTrue(linkIds5min.containsAll(List.of(
            link2.getLinkId(), link3.getLinkId()
        )));
        assertTrue(linkIds1hour.contains(link3.getLinkId()));
    }

    @Test
    @Transactional
    @Rollback
    void setLastCheckedTimeToLinkTest() {

        // Arrange
        long link_id = 11L;
        String url =
            "https://stackoverflow.com/questions/54378414/how-to-fix-cant-infer-the-sql-type-to-use-for-an-instance-of-enum-error-when";
        OffsetDateTime initTime = OffsetDateTime.now();

        Link link = new Link();
        link.setLinkId(link_id);
        link.setUrl(url);
        link.setLastUpdated(OffsetDateTime.now());
        link.setLastChecked(initTime);
        link.setZoneOffset(0);

        // Act && Assert
        jdbcLinksRepository.addLink(link);
        Link initLink = jdbcLinksRepository.getLinkById(link_id);

        assertEquals(
            initLink.getLastChecked()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
            initTime
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );

        jdbcLinksRepository.setLastCheckedTimeToLink(
            link, initTime.plusMinutes(10));

        Link modifiedLink = jdbcLinksRepository.getLinkById(link_id);

        assertEquals(
            modifiedLink.getLastChecked()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
            initTime.plusMinutes(10)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );
    }

    @Test
    @Transactional
    @Rollback
    void setLastUpdatedTimeToLinkTest() {

        // Arrange
        long link_id = 12L;
        String url =
            "https://stackoverflow.com/questions/54378414/how-to-fix-cant-infer-the-sql-type-to-use-for-an-instance-of-enum-error-when";
        OffsetDateTime initTime = OffsetDateTime.now();

        Link link = new Link();
        link.setLinkId(link_id);
        link.setUrl(url);
        link.setLastUpdated(initTime);
        link.setLastChecked(OffsetDateTime.now());
        link.setZoneOffset(0);

        // Act && Assert
        jdbcLinksRepository.addLink(link);
        Link initLink = jdbcLinksRepository.getLinkById(link_id);

        assertEquals(initLink.getLastChecked()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
            initTime
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );

        jdbcLinksRepository.setLastUpdatedTimeToLink(
            link, initTime.plusMinutes(10));

        Link modifiedLink = jdbcLinksRepository.getLinkById(link_id);

        assertEquals(modifiedLink.getLastUpdated()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
            initTime.plusMinutes(10)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    @Test
    @Transactional
    @Rollback
    void duplicateKeyTest() {
        long link_id = 13L;
        String url = "https://github.com/RuslanAndrianov/Backend-Java-Tinkoff-Spring-2024";

        Link link = new Link();
        link.setLinkId(link_id);
        link.setUrl(url);
        link.setLastUpdated(OffsetDateTime.now());
        link.setLastChecked(OffsetDateTime.now());
        link.setZoneOffset(0);

        boolean isLinkAdded;

        isLinkAdded = jdbcLinksRepository.addLink(link);
        assertTrue(isLinkAdded);
        isLinkAdded = jdbcLinksRepository.addLink(link);
        assertFalse(isLinkAdded);
    }
}
