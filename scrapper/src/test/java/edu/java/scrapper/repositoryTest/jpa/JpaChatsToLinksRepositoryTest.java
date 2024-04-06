package edu.java.scrapper.repositoryTest.jpa;

import edu.java.domain.dto.Chat;
import edu.java.domain.dto.Link;
import edu.java.scrapper.repositoryTest.JpaIntegrationTest;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JpaChatsToLinksRepositoryTest extends JpaIntegrationTest {

    @Test
    @Transactional
    void addLinkToChatTest() {

        // Arrange
        long chat_id = 20L;
        long link_id = 20L;
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
        List<Link> linksBefore = jpaChatsToLinksRepository.getAllLinksByChat(chat);
        isLinkAdded = jpaChatsToLinksRepository.addLinkToChat(chat, link);
        List<Link> linksAfter = jpaChatsToLinksRepository.getAllLinksByChat(chat);

        // Assert
        assertTrue(isLinkAdded);
        assertEquals(linksAfter.size() - linksBefore.size(), 1);

        // After
        jpaChatsToLinksRepository.deleteLinkFromChat(chat, link);
        jpaChatsRepository.deleteChat(chat);
        jpaLinksRepository.deleteLink(link);
    }

    @Test
    @Transactional
    void deleteLinkFromChatTest() {

        // Arrange
        long chat_id = 21L;
        long link_id = 21L;
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
        List<Link> linksBefore = jpaChatsToLinksRepository.getAllLinksByChat(chat);

        jpaChatsRepository.addChat(chat);
        jpaLinksRepository.addLink(link);
        jpaChatsToLinksRepository.addLinkToChat(chat, link);

        isLinkDeleted = jpaChatsToLinksRepository.deleteLinkFromChat(chat, link);
        jpaChatsRepository.deleteChat(chat);
        jpaLinksRepository.deleteLink(link);

        List<Link> linksAfter = jpaChatsToLinksRepository.getAllLinksByChat(chat);

        // Assert
        assertTrue(isLinkDeleted);
        assertEquals(linksBefore.size(), linksAfter.size());
    }

    @Test
    @Transactional
    void deleteChatTest() {

        // Arrange
        long chat_id = 22L;
        long link_id1 = 22L;
        long link_id2 = 23L;
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

        List<Link> linksBefore = jpaChatsToLinksRepository.getAllLinksByChat(chat);
        jpaChatsToLinksRepository.addLinkToChat(chat, link1);
        jpaChatsToLinksRepository.addLinkToChat(chat, link2);

        isChatDeleted = jpaChatsToLinksRepository.deleteChat(chat);
        jpaChatsRepository.deleteChat(chat);
        jpaLinksRepository.deleteLink(link1);
        jpaLinksRepository.deleteLink(link2);
        List<Link> linksAfter = jpaChatsToLinksRepository.getAllLinksByChat(chat);

        // Assert
        assertTrue(isChatDeleted);
        assertEquals(linksBefore.size(), linksAfter.size());
    }

    @Test
    @Transactional
    void isChatExistTest() {

        // Arrange
        long chat_id = 23L;
        long link_id = 24L;
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

        isChatExist = jpaChatsToLinksRepository.isChatExist(chat);

        assertTrue(isChatExist);

        jpaChatsToLinksRepository.deleteChat(chat);
        jpaChatsRepository.deleteChat(chat);
        jpaLinksRepository.deleteLink(link);

        isChatExist = jpaChatsToLinksRepository.isChatExist(chat);

        assertFalse(isChatExist);
    }

    @Test
    @Transactional
    void getAllLinksByChatTest() {

        // Arrange
        long chat_id1 = 24L;
        long chat_id2 = 25L;
        long link_id1 = 25L;
        long link_id2 = 26L;
        long link_id3 = 27L;
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

        List<Link> links1 = jpaChatsToLinksRepository.getAllLinksByChat(chat1);
        List<Link> links2 = jpaChatsToLinksRepository.getAllLinksByChat(chat2);

        // Assert
        assertEquals(links1.size(), 2);
        assertEquals(links1.getFirst().getLinkId(), link_id1);
        assertEquals(links1.getFirst().getUrl(), url1);
        assertEquals(links1.getLast().getLinkId(), link_id2);
        assertEquals(links1.getLast().getUrl(), url2);

        assertEquals(links2.size(), 1);
        assertEquals(links2.getFirst().getLinkId(), link_id3);
        assertEquals(links2.getFirst().getUrl(), url3);

        // After
        jpaChatsToLinksRepository.deleteLinkFromChat(chat1, link1);
        jpaChatsToLinksRepository.deleteLinkFromChat(chat1, link2);
        jpaChatsToLinksRepository.deleteLinkFromChat(chat2, link3);
        jpaChatsRepository.deleteChat(chat1);
        jpaChatsRepository.deleteChat(chat2);
        jpaLinksRepository.deleteLink(link1);
        jpaLinksRepository.deleteLink(link2);
        jpaLinksRepository.deleteLink(link3);
    }

    @Test
    @Transactional
    @Rollback
    void getAllChatsByLinkTest() {

        // Arrange
        long chat_id1 = 26L;
        long chat_id2 = 27L;
        long chat_id3 = 28L;
        long link_id = 28L;
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

        List<Long> chatIds = jpaChatsToLinksRepository.getAllChatsByLink(link);

        // Assert
        assertEquals(chatIds.size(), 2);
        assertTrue(chatIds.containsAll(List.of(
            chat1.getChatId(),
            chat2.getChatId())));

        // After
        jpaChatsToLinksRepository.deleteLinkFromChat(chat1, link);
        jpaChatsToLinksRepository.deleteLinkFromChat(chat2, link);
        jpaChatsRepository.deleteChat(chat1);
        jpaChatsRepository.deleteChat(chat2);
        jpaChatsRepository.deleteChat(chat3);
        jpaLinksRepository.deleteLink(link);
    }

    @Test
    @Transactional
    void duplicateKeyTest() {

        // Arrange
        long chat_id = 29L;
        long link_id = 29L;
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

        // After
        jpaChatsToLinksRepository.deleteLinkFromChat(chat, link);
        jpaChatsRepository.deleteChat(chat);
        jpaLinksRepository.deleteLink(link);
    }
}
