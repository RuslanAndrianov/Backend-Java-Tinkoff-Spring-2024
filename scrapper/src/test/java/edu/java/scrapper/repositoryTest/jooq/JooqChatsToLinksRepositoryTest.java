package edu.java.scrapper.repositoryTest.jooq;

import edu.java.scrapper.IntegrationEnvironment;
import edu.java.scrapper.domain.dto.Chat;
import edu.java.scrapper.domain.dto.Link;
import edu.java.scrapper.domain.repository.jooq.JooqChatsRepository;
import edu.java.scrapper.domain.repository.jooq.JooqChatsToLinksRepository;
import edu.java.scrapper.domain.repository.jooq.JooqLinksRepository;
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

@SpringBootTest(properties = "app.database-access-type=jooq")
public class JooqChatsToLinksRepositoryTest extends IntegrationEnvironment {

    @Autowired
    private JooqChatsRepository jooqChatsRepository;
    @Autowired
    private JooqLinksRepository jooqLinksRepository;
    @Autowired
    private JooqChatsToLinksRepository jooqChatsToLinksRepository;

    @Test
    @Transactional
    @Rollback
    void addLinkToChatTest() {

        // Arrange
        long chat_id = 120L;
        long link_id = 120L;
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
        jooqChatsRepository.addChat(chat);
        jooqLinksRepository.addLink(link);
        List<Long> linkIdsBefore = jooqChatsToLinksRepository.getAllLinkIdsByChat(chat);
        isLinkAdded = jooqChatsToLinksRepository.addLinkToChat(chat, link);
        List<Long> linkIdsAfter = jooqChatsToLinksRepository.getAllLinkIdsByChat(chat);

        // Assert
        assertTrue(isLinkAdded);
        assertEquals(linkIdsAfter.size() - linkIdsBefore.size(), 1);
    }

    @Test
    @Transactional
    @Rollback
    void deleteLinkFromChatTest() {

        // Arrange
        long chat_id = 121L;
        long link_id = 121L;
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
        List<Long> linkIdsBefore = jooqChatsToLinksRepository.getAllLinkIdsByChat(chat);

        jooqChatsRepository.addChat(chat);
        jooqLinksRepository.addLink(link);
        jooqChatsToLinksRepository.addLinkToChat(chat, link);

        isLinkDeleted = jooqChatsToLinksRepository.deleteLinkFromChat(chat, link);
        jooqChatsRepository.deleteChat(chat);
        jooqLinksRepository.deleteLink(link);

        List<Long> linkIdsAfter = jooqChatsToLinksRepository.getAllLinkIdsByChat(chat);

        // Assert
        assertTrue(isLinkDeleted);
        assertEquals(linkIdsBefore.size(), linkIdsAfter.size());
    }

    @Test
    @Transactional
    @Rollback
    void deleteChatTest() {

        // Arrange
        long chat_id = 122L;
        long link_id1 = 122L;
        long link_id2 = 123L;
        String url1 =
            "https://stackoverflow.com/questions/54378414/how-to-fix-cant-infer-the-sql-type-to-use-for-an-instance-of-enum-error-when";
        String url2 =
            "https://stackoverflow.com/questions/50145552/error-org-springframework-jooq-badsqlgrammarexception-statementcallback-bad-s";
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
        jooqChatsRepository.addChat(chat);
        jooqLinksRepository.addLink(link1);
        jooqLinksRepository.addLink(link2);

        List<Long> linkIdsBefore = jooqChatsToLinksRepository.getAllLinkIdsByChat(chat);
        jooqChatsToLinksRepository.addLinkToChat(chat, link1);
        jooqChatsToLinksRepository.addLinkToChat(chat, link2);

        isChatDeleted = jooqChatsToLinksRepository.deleteChat(chat);
        jooqChatsRepository.deleteChat(chat);
        jooqLinksRepository.deleteLink(link1);
        jooqLinksRepository.deleteLink(link2);
        List<Long> linkIdsAfter = jooqChatsToLinksRepository.getAllLinkIdsByChat(chat);

        // Assert
        assertTrue(isChatDeleted);
        assertEquals(linkIdsBefore.size(), linkIdsAfter.size());
    }

    @Test
    @Transactional
    @Rollback
    void isChatExistTest() {

        // Arrange
        long chat_id = 123L;
        long link_id = 124L;
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
        jooqChatsRepository.addChat(chat);
        jooqLinksRepository.addLink(link);
        jooqChatsToLinksRepository.addLinkToChat(chat, link);

        isChatExist = jooqChatsToLinksRepository.isChatHasLinks(chat);

        assertTrue(isChatExist);

        jooqChatsToLinksRepository.deleteChat(chat);
        jooqChatsRepository.deleteChat(chat);
        jooqLinksRepository.deleteLink(link);

        isChatExist = jooqChatsToLinksRepository.isChatHasLinks(chat);

        assertFalse(isChatExist);
    }

    @Test
    @Transactional
    @Rollback
    void getAllLinksByChatTest() {

        // Arrange
        long chat_id1 = 124L;
        long chat_id2 = 125L;
        long link_id1 = 125L;
        long link_id2 = 126L;
        long link_id3 = 127L;
        String url1 =
            "https://stackoverflow.com/questions/54378414/how-to-fix-cant-infer-the-sql-type-to-use-for-an-instance-of-enum-error-when";
        String url2 =
            "https://stackoverflow.com/questions/50145552/error-org-springframework-jooq-badsqlgrammarexception-statementcallback-bad-s";
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
        jooqChatsRepository.addChat(chat1);
        jooqChatsRepository.addChat(chat2);
        jooqLinksRepository.addLink(link1);
        jooqLinksRepository.addLink(link2);
        jooqLinksRepository.addLink(link3);
        jooqChatsToLinksRepository.addLinkToChat(chat1, link1);
        jooqChatsToLinksRepository.addLinkToChat(chat1, link2);
        jooqChatsToLinksRepository.addLinkToChat(chat2, link3);

        List<Long> linkIds1 = jooqChatsToLinksRepository.getAllLinkIdsByChat(chat1);
        List<Long> linkIds2 = jooqChatsToLinksRepository.getAllLinkIdsByChat(chat2);

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
        long chat_id1 = 126L;
        long chat_id2 = 127L;
        long chat_id3 = 128L;
        long link_id = 128L;
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
        jooqChatsRepository.addChat(chat1);
        jooqChatsRepository.addChat(chat2);
        jooqChatsRepository.addChat(chat3);
        jooqLinksRepository.addLink(link);
        jooqChatsToLinksRepository.addLinkToChat(chat1, link);
        jooqChatsToLinksRepository.addLinkToChat(chat2, link);

        List<Long> chatIds = jooqChatsToLinksRepository.getAllChatIdsByLink(link);

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
        long chat_id = 129L;
        long link_id = 129L;
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
        jooqChatsRepository.addChat(chat);
        jooqLinksRepository.addLink(link);

        isLinkAdded = jooqChatsToLinksRepository.addLinkToChat(chat, link);
        assertTrue(isLinkAdded);
        isLinkAdded = jooqChatsToLinksRepository.addLinkToChat(chat, link);
        assertFalse(isLinkAdded);
    }
}
