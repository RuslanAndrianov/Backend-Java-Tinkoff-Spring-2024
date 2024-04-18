package edu.java.scrapper.repositoryTest.jpa;

import edu.java.domain.dto.Link;
import edu.java.domain.repository.jpa.JpaLinksRepository;
import edu.java.scrapper.IntegrationEnvironment;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static edu.java.utils.TimeCorrecter.getCorrectedTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(properties = "app.database-access-type=jpa")
public class JpaLinksRepositoryTest extends IntegrationEnvironment {

    @Autowired
    private JpaLinksRepository jpaLinksRepository;

    @Test
    @Transactional
    @Rollback
    void addLinkTest() {

        // Arrange
        long link_id = 30L;
        String url = "https://stackoverflow.com/questions/54378414/how-to-fix-cant-infer-the-sql-type-to-use-for-an-instance-of-enum-error-when";

        Link link = new Link();
        link.setLinkId(link_id);
        link.setUrl(url);
        link.setLastUpdated(OffsetDateTime.now());
        link.setLastChecked(OffsetDateTime.now());
        link.setZoneOffset(0);

        boolean isLinkAdded;

        // Act
        List<Link> linksBefore = jpaLinksRepository.getAllLinks();
        isLinkAdded = jpaLinksRepository.addLink(link);
        List<Link> linksAfter = jpaLinksRepository.getAllLinks();

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
        long link_id = 31L;
        String url = "https://stackoverflow.com/questions/54378414/how-to-fix-cant-infer-the-sql-type-to-use-for-an-instance-of-enum-error-when";

        Link link = new Link();
        link.setLinkId(link_id);
        link.setUrl(url);
        link.setLastUpdated(OffsetDateTime.now());
        link.setLastChecked(OffsetDateTime.now());
        link.setZoneOffset(0);

        boolean isLinkDeleted;

        // Act
        List<Link> linksBefore = jpaLinksRepository.getAllLinks();
        jpaLinksRepository.addLink(link);
        isLinkDeleted = jpaLinksRepository.deleteLink(link);
        List<Link> linksAfter = jpaLinksRepository.getAllLinks();

        // Assert
        assertTrue(isLinkDeleted);
        assertEquals(linksBefore.size(), linksAfter.size());
    }

    @Test
    @Transactional
    @Rollback
    void getLinkByIdTest() {

        // Arrange
        long link_id = 32L;
        String url = "https://github.com/RuslanAndrianov/Backend-Java-Tinkoff-Spring-2024";

        Link link = new Link();
        link.setLinkId(link_id);
        link.setUrl(url);
        link.setLastUpdated(OffsetDateTime.now());
        link.setLastChecked(OffsetDateTime.now());
        link.setZoneOffset(0);

        // Act
        jpaLinksRepository.addLink(link);
        Link foundLink = jpaLinksRepository.getLinkById(link_id);

        // Assert
        assertEquals(foundLink.getLinkId(), link.getLinkId());
        assertEquals(foundLink.getUrl(), link.getUrl());
    }

    @Test
    @Transactional
    @Rollback
    void getLinkByUrlTest() {

        // Arrange
        long link_id = 33L;
        String url = "https://github.com/arhostcode/linktracker";

        Link link = new Link();
        link.setLinkId(link_id);
        link.setUrl(url);
        link.setLastUpdated(OffsetDateTime.now());
        link.setLastChecked(OffsetDateTime.now());
        link.setZoneOffset(0);

        // Act
        jpaLinksRepository.addLink(link);
        Link foundLink = jpaLinksRepository.getLinkByUrl(url);

        // Assert
        assertEquals(foundLink.getLinkId(), link.getLinkId());
        assertEquals(foundLink.getUrl(), link.getUrl());
    }

    @Test
    @Transactional
    @Rollback
    void getAllLinksTest() {

        // Arrange
        long link_id1 = 34L;
        long link_id2 = 35L;
        long link_id3 = 36L;
        String url1 = "https://stackoverflow.com/questions/54378414/how-to-fix-cant-infer-the-sql-type-to-use-for-an-instance-of-enum-error-when";
        String url2 = "https://stackoverflow.com/questions/50145552/error-org-springframework-jpa-badsqlgrammarexception-statementcallback-bad-s";
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

        // Act
        List<Link> linksBefore = jpaLinksRepository.getAllLinks();
        jpaLinksRepository.addLink(link1);
        jpaLinksRepository.addLink(link2);
        jpaLinksRepository.addLink(link3);
        List<Link> linksAfter = jpaLinksRepository.getAllLinks();

        // Assert
        assertEquals(linksAfter.size() - linksBefore.size(), 3);
    }

    @Test
    @Transactional
    @Rollback
    void getOldestCheckedLinksTest() {

        // Arrange
        long link_id1 = 37L;
        long link_id2 = 38L;
        long link_id3 = 39L;
        String url1 =
            "https://stackoverflow.com/questions/54378414/how-to-fix-cant-infer-the-sql-type-to-use-for-an-instance-of-enum-error-when";
        String url2 =
            "https://stackoverflow.com/questions/50145552/error-org-springframework-jpa-badsqlgrammarexception-statementcallback-bad-s";
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
        link2.setLastChecked(OffsetDateTime.now().minusMinutes(10));
        link2.setZoneOffset(0);

        Link link3 = new Link();
        link3.setLinkId(link_id3);
        link3.setUrl(url3);
        link3.setLastUpdated(OffsetDateTime.now());
        link3.setLastChecked(OffsetDateTime.now().minusHours(2));
        link3.setZoneOffset(0);

        // Act
        jpaLinksRepository.addLink(link1);
        jpaLinksRepository.addLink(link2);
        jpaLinksRepository.addLink(link3);

        List<Long> linkIds5min = jpaLinksRepository
            .getOldestCheckedLinks("5 minutes")
            .stream()
            .map(Link::getLinkId)
            .toList();
        List<Long> linkIds1hour = jpaLinksRepository
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
        long link_id = 40L;
        String url =
            "https://stackoverflow.com/questions/54378414/how-to-fix-cant-infer-the-sql-type-to-use-for-an-instance-of-enum-error-when";
        OffsetDateTime initTime = getCorrectedTime(OffsetDateTime.now(), ZoneOffset.UTC.getTotalSeconds());

        Link link = new Link();
        link.setLinkId(link_id);
        link.setUrl(url);
        link.setLastUpdated(OffsetDateTime.now());
        link.setLastChecked(initTime);
        link.setZoneOffset(0);

        // Act && Assert
        jpaLinksRepository.addLink(link);
        Link initLink = jpaLinksRepository.getLinkById(link_id);

        assertTrue(Math.abs(
            initLink.getLastChecked().toEpochSecond() - initTime.toEpochSecond()) <= 1);

        jpaLinksRepository.setLastCheckedTimeToLink(
            link, initTime.plusMinutes(10));

        Link modifiedLink = jpaLinksRepository.getLinkById(link_id);

        assertTrue(Math.abs(
            modifiedLink.getLastChecked().toEpochSecond() - initTime.toEpochSecond()) <= 1 + 60 * 10);
    }

    @Test
    @Transactional
    @Rollback
    void setLastUpdatedTimeToLinkTest() {

        // Arrange
        long link_id = 41L;
        String url =
            "https://stackoverflow.com/questions/54378414/how-to-fix-cant-infer-the-sql-type-to-use-for-an-instance-of-enum-error-when";
        OffsetDateTime initTime = getCorrectedTime(OffsetDateTime.now(), ZoneOffset.UTC.getTotalSeconds());

        Link link = new Link();
        link.setLinkId(link_id);
        link.setUrl(url);
        link.setLastUpdated(initTime);
        link.setLastChecked(OffsetDateTime.now());
        link.setZoneOffset(0);

        // Act && Assert
        jpaLinksRepository.addLink(link);
        Link initLink = jpaLinksRepository.getLinkById(link_id);

        assertTrue(Math.abs(
            initLink.getLastChecked().toEpochSecond() - initTime.toEpochSecond()) <= 1);


        jpaLinksRepository.setLastUpdatedTimeToLink(
            link, initTime.plusMinutes(10));

        Link modifiedLink = jpaLinksRepository.getLinkById(link_id);

        assertTrue(Math.abs(
            modifiedLink.getLastChecked().toEpochSecond() - initTime.toEpochSecond()) <= 1 + 60 * 10);
    }

    @Test
    @Transactional
    @Rollback
    void duplicateKeyTest() {
        long link_id = 42L;
        String url = "https://github.com/RuslanAndrianov/Backend-Java-Tinkoff-Spring-2024";

        Link link = new Link();
        link.setLinkId(link_id);
        link.setUrl(url);
        link.setLastUpdated(OffsetDateTime.now());
        link.setLastChecked(OffsetDateTime.now());
        link.setZoneOffset(0);

        boolean isLinkAdded;

        isLinkAdded = jpaLinksRepository.addLink(link);
        assertTrue(isLinkAdded);
        isLinkAdded = jpaLinksRepository.addLink(link);
        assertFalse(isLinkAdded);
    }
}
