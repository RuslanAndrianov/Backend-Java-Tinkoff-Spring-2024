package edu.java.scrapper.repositoryTest.jpa;

import edu.java.scrapper.domain.dto.Chat;
import edu.java.scrapper.domain.dto.Link;
import edu.java.scrapper.domain.repository.jpa.JpaChatsRepository;
import edu.java.scrapper.domain.repository.jpa.JpaChatsToLinksRepository;
import edu.java.scrapper.domain.repository.jpa.JpaLinksRepository;
import edu.java.scrapper.IntegrationEnvironment;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(properties = "app.database-access-type=jpa")
public class JpaChatsToLinksRepositoryTest extends IntegrationEnvironment {

    @Autowired
    private JpaChatsRepository jpaChatsRepository;
    @Autowired
    private JpaLinksRepository jpaLinksRepository;
    @Autowired
    private JpaChatsToLinksRepository jpaChatsToLinksRepository;

    @Test
    @Transactional
    @Rollback
    void addLinkToChatTest() {

        // Arrange
        long chat_id = 50L;
        long link_id = 50L;
        String url = "https://github.com/RuslanAndrianov/Backend-Java-Tinkoff-Spring-2024";

        Chat chat = new Chat();
        chat.setChatId(chat_id);

        Link link = new Link();
        link.setLinkId(link_id);
        link.setUrl(url);
        link.setLastUpdated(OffsetDateTime.now());
        link.setLastChecked(OffsetDateTime.now());
        link.setZoneOffset(0);

        boolean isLinkAdded;

        // Act
        jpaChatsRepository.addChat(chat);
        jpaLinksRepository.addLink(link);
        List<Long> linkIdsBefore = jpaChatsToLinksRepository.getAllLinkIdsByChat(chat);
        isLinkAdded = jpaChatsToLinksRepository.addLinkToChat(chat, link);
        List<Long> linkIdsAfter = jpaChatsToLinksRepository.getAllLinkIdsByChat(chat);

        // Assert
        assertTrue(isLinkAdded);
        assertEquals(linkIdsAfter.size() - linkIdsBefore.size(), 1);
    }

    @Test
    @Transactional
    @Rollback
    void deleteLinkFromChatTest() {

        // Arrange
        long chat_id = 51L;
        long link_id = 51L;
        String url = "https://github.com/RuslanAndrianov/Backend-Java-Tinkoff-Spring-2024";

        Chat chat = new Chat();
        chat.setChatId(chat_id);

        Link link = new Link();
        link.setLinkId(link_id);
        link.setUrl(url);
        link.setLastUpdated(OffsetDateTime.now());
        link.setLastChecked(OffsetDateTime.now());
        link.setZoneOffset(0);

        boolean isLinkDeleted;

        // Act
        List<Long> linkIdsBefore = jpaChatsToLinksRepository.getAllLinkIdsByChat(chat);

        jpaChatsRepository.addChat(chat);
        jpaLinksRepository.addLink(link);
        jpaChatsToLinksRepository.addLinkToChat(chat, link);

        isLinkDeleted = jpaChatsToLinksRepository.deleteLinkFromChat(chat, link);
        jpaChatsRepository.deleteChat(chat);
        jpaLinksRepository.deleteLink(link);

        List<Long> linkIdsAfter = jpaChatsToLinksRepository.getAllLinkIdsByChat(chat);
    }

    @Test
    @Transactional
    @Rollback
    void deleteChatTest() {

        // Arrange
        long chat_id = 52L;
        long link_id1 = 52L;
        long link_id2 = 53L;
        String url1 =
            "https://stackoverflow.com/questions/54378414/how-to-fix-cant-infer-the-sql-type-to-use-for-an-instance-of-enum-error-when";
        String url2 =
            "https://stackoverflow.com/questions/50145552/error-org-springframework-jpa-badsqlgrammarexception-statementcallback-bad-s";
        Chat chat = new Chat();
        chat.setChatId(chat_id);

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

        boolean isChatDeleted;

        // Act
        jpaChatsRepository.addChat(chat);
        jpaLinksRepository.addLink(link1);
        jpaLinksRepository.addLink(link2);

        List<Long> linkIdsBefore = jpaChatsToLinksRepository.getAllLinkIdsByChat(chat);
        jpaChatsToLinksRepository.addLinkToChat(chat, link1);
        jpaChatsToLinksRepository.addLinkToChat(chat, link2);

        isChatDeleted = jpaChatsToLinksRepository.deleteChat(chat);
        jpaChatsRepository.deleteChat(chat);
        jpaLinksRepository.deleteLink(link1);
        jpaLinksRepository.deleteLink(link2);
        List<Long> linkIdsAfter = jpaChatsToLinksRepository.getAllLinkIdsByChat(chat);

        // Assert
        assertTrue(isChatDeleted);
        assertEquals(linkIdsBefore.size(), linkIdsAfter.size());
    }

    @Test
    @Transactional
    @Rollback
    void isChatExistTest() {

        // Arrange
        long chat_id = 53L;
        long link_id = 54L;
        String url = "https://github.com/RuslanAndrianov/Backend-Java-Tinkoff-Spring-2024";

        Chat chat = new Chat();
        chat.setChatId(chat_id);

        Link link = new Link();
        link.setLinkId(link_id);
        link.setUrl(url);
        link.setLastUpdated(OffsetDateTime.now());
        link.setLastChecked(OffsetDateTime.now());
        link.setZoneOffset(0);

        boolean isChatExist;

        // Act && Assert
        jpaChatsRepository.addChat(chat);
        jpaLinksRepository.addLink(link);
        jpaChatsToLinksRepository.addLinkToChat(chat, link);

        isChatExist = jpaChatsToLinksRepository.isChatHasLinks(chat);

        assertTrue(isChatExist);

        jpaChatsToLinksRepository.deleteChat(chat);
        jpaChatsRepository.deleteChat(chat);
        jpaLinksRepository.deleteLink(link);

        isChatExist = jpaChatsToLinksRepository.isChatHasLinks(chat);

        assertFalse(isChatExist);
    }

    @Test
    @Transactional
    @Rollback
    void getAllLinksByChatTest() {

        // Arrange
        long chat_id1 = 54L;
        long chat_id2 = 55L;
        long link_id1 = 55L;
        long link_id2 = 56L;
        long link_id3 = 57L;
        String url1 =
            "https://stackoverflow.com/questions/54378414/how-to-fix-cant-infer-the-sql-type-to-use-for-an-instance-of-enum-error-when";
        String url2 =
            "https://stackoverflow.com/questions/50145552/error-org-springframework-jpa-badsqlgrammarexception-statementcallback-bad-s";
        String url3 = "https://github.com/RuslanAndrianov/Backend-Java-Tinkoff-Spring-2024";

        Chat chat1 = new Chat();
        chat1.setChatId(chat_id1);

        Chat chat2 = new Chat();
        chat2.setChatId(chat_id2);

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
        jpaChatsRepository.addChat(chat1);
        jpaChatsRepository.addChat(chat2);
        jpaLinksRepository.addLink(link1);
        jpaLinksRepository.addLink(link2);
        jpaLinksRepository.addLink(link3);
        jpaChatsToLinksRepository.addLinkToChat(chat1, link1);
        jpaChatsToLinksRepository.addLinkToChat(chat1, link2);
        jpaChatsToLinksRepository.addLinkToChat(chat2, link3);

        List<Long> linkIds1 = jpaChatsToLinksRepository.getAllLinkIdsByChat(chat1);
        List<Long> linkIds2 = jpaChatsToLinksRepository.getAllLinkIdsByChat(chat2);

        // Assert
        assertEquals(linkIds1.size(), 2);
        assertThat(linkIds1).contains(link_id1, link_id2);

        assertEquals(linkIds2.size(), 1);
        assertThat(linkIds2).contains(link_id3);
    }

    @Test
    @Transactional
    @Rollback
    void getAllChatsByLinkTest() {

        // Arrange
        long chat_id1 = 56L;
        long chat_id2 = 57L;
        long chat_id3 = 58L;
        long link_id = 58L;
        String url =
            "https://stackoverflow.com/questions/54378414/how-to-fix-cant-infer-the-sql-type-to-use-for-an-instance-of-enum-error-when";

        Chat chat1 = new Chat();
        chat1.setChatId(chat_id1);

        Chat chat2 = new Chat();
        chat2.setChatId(chat_id2);

        Chat chat3 = new Chat();
        chat3.setChatId(chat_id3);

        Link link = new Link();
        link.setLinkId(link_id);
        link.setUrl(url);
        link.setLastUpdated(OffsetDateTime.now());
        link.setLastChecked(OffsetDateTime.now());
        link.setZoneOffset(0);

        // Act
        jpaChatsRepository.addChat(chat1);
        jpaChatsRepository.addChat(chat2);
        jpaChatsRepository.addChat(chat3);
        jpaLinksRepository.addLink(link);
        jpaChatsToLinksRepository.addLinkToChat(chat1, link);
        jpaChatsToLinksRepository.addLinkToChat(chat2, link);

        List<Long> chatIds = jpaChatsToLinksRepository.getAllChatIdsByLink(link);

        // Assert
        assertEquals(chatIds.size(), 2);
        assertTrue(chatIds.containsAll(List.of(
            chat1.getChatId(),
            chat2.getChatId())));
    }

    @Test
    @Transactional
    @Rollback
    void duplicateKeyTest() {

        // Arrange
        long chat_id = 59L;
        long link_id = 59L;
        String url = "https://github.com/RuslanAndrianov/Backend-Java-Tinkoff-Spring-2024";

        Chat chat = new Chat();
        chat.setChatId(chat_id);

        Link link = new Link();
        link.setLinkId(link_id);
        link.setUrl(url);
        link.setLastUpdated(OffsetDateTime.now());
        link.setLastChecked(OffsetDateTime.now());
        link.setZoneOffset(0);

        boolean isLinkAdded;

        // Act && Assert
        jpaChatsRepository.addChat(chat);
        jpaLinksRepository.addLink(link);

        isLinkAdded = jpaChatsToLinksRepository.addLinkToChat(chat, link);
        assertTrue(isLinkAdded);
        isLinkAdded = jpaChatsToLinksRepository.addLinkToChat(chat, link);
        assertFalse(isLinkAdded);
    }
}
