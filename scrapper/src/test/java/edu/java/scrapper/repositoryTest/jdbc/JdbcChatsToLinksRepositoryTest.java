package edu.java.scrapper.repositoryTest.jdbc;

import edu.java.domain.dto.Chat;
import edu.java.domain.dto.Link;
import edu.java.domain.repository.jdbc.JdbcChatsRepository;
import edu.java.domain.repository.jdbc.JdbcChatsToLinksRepository;
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

public class JdbcChatsToLinksRepositoryTest extends IntegrationTest {

    private static final JdbcChatsToLinksRepository jdbcChatsToLinksRepository;
    private static final JdbcChatsRepository jdbcChatsRepository;
    private static final JdbcLinksRepository jdbcLinksRepository;

    private static final RowMapper<Chat> chatRowMapper = (resultSet, rowNum) -> {
        Chat chat = new Chat();
        chat.setChatId(resultSet.getLong("chat_id"));
        return chat;
    };

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

    private static final RowMapper<Long> chatLinkRowMapper = (resultSet, rowNum) ->
        resultSet.getLong("chat_id");

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

        jdbcChatsRepository = new JdbcChatsRepository(jdbcTemplate, chatRowMapper);
        jdbcLinksRepository = new JdbcLinksRepository(jdbcTemplate, linkRowMapper);
        jdbcChatsToLinksRepository = new JdbcChatsToLinksRepository(
            jdbcTemplate, chatLinkRowMapper, linkRowMapper);
    }

    @Test
    @Transactional
    @Rollback
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
        jdbcChatsRepository.addChat(chat);
        jdbcLinksRepository.addLink(link);
        List<Link> linksBefore = jdbcChatsToLinksRepository.getAllLinksByChat(chat);
        isLinkAdded = jdbcChatsToLinksRepository.addLinkToChat(chat, link);
        List<Link> linksAfter = jdbcChatsToLinksRepository.getAllLinksByChat(chat);

        // Assert
        assertTrue(isLinkAdded);
        assertEquals(linksAfter.size() - linksBefore.size(), 1);
    }

    @Test
    @Transactional
    @Rollback
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
        List<Link> linksBefore = jdbcChatsToLinksRepository.getAllLinksByChat(chat);

        jdbcChatsRepository.addChat(chat);
        jdbcLinksRepository.addLink(link);
        jdbcChatsToLinksRepository.addLinkToChat(chat, link);

        isLinkDeleted = jdbcChatsToLinksRepository.deleteLinkFromChat(chat, link);
        jdbcChatsRepository.deleteChat(chat);
        jdbcLinksRepository.deleteLink(link);

        List<Link> linksAfter = jdbcChatsToLinksRepository.getAllLinksByChat(chat);

        // Assert
        assertTrue(isLinkDeleted);
        assertEquals(linksBefore.size(), linksAfter.size());
    }

    @Test
    @Transactional
    @Rollback
    void deleteChatTest() {

        // Arrange
        long chat_id = 22L;
        long link_id1 = 22L;
        long link_id2 = 23L;
        String url1 =
            "https://stackoverflow.com/questions/54378414/how-to-fix-cant-infer-the-sql-type-to-use-for-an-instance-of-enum-error-when";
        String url2 =
            "https://stackoverflow.com/questions/50145552/error-org-springframework-jdbc-badsqlgrammarexception-statementcallback-bad-s";
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
        jdbcChatsRepository.addChat(chat);
        jdbcLinksRepository.addLink(link1);
        jdbcLinksRepository.addLink(link2);

        List<Link> linksBefore = jdbcChatsToLinksRepository.getAllLinksByChat(chat);
        jdbcChatsToLinksRepository.addLinkToChat(chat, link1);
        jdbcChatsToLinksRepository.addLinkToChat(chat, link2);

        isChatDeleted = jdbcChatsToLinksRepository.deleteChat(chat);
        jdbcChatsRepository.deleteChat(chat);
        jdbcLinksRepository.deleteLink(link1);
        jdbcLinksRepository.deleteLink(link2);
        List<Link> linksAfter = jdbcChatsToLinksRepository.getAllLinksByChat(chat);

        // Assert
        assertTrue(isChatDeleted);
        assertEquals(linksBefore.size(), linksAfter.size());
    }

    @Test
    @Transactional
    @Rollback
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
        jdbcChatsRepository.addChat(chat);
        jdbcLinksRepository.addLink(link);
        jdbcChatsToLinksRepository.addLinkToChat(chat, link);

        isChatExist = jdbcChatsToLinksRepository.isChatExist(chat);

        assertTrue(isChatExist);

        jdbcChatsToLinksRepository.deleteChat(chat);
        jdbcChatsRepository.deleteChat(chat);
        jdbcLinksRepository.deleteLink(link);

        isChatExist = jdbcChatsToLinksRepository.isChatExist(chat);

        assertFalse(isChatExist);
    }

    @Test
    @Transactional
    @Rollback
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
            "https://stackoverflow.com/questions/50145552/error-org-springframework-jdbc-badsqlgrammarexception-statementcallback-bad-s";
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
        jdbcChatsRepository.addChat(chat1);
        jdbcChatsRepository.addChat(chat2);
        jdbcLinksRepository.addLink(link1);
        jdbcLinksRepository.addLink(link2);
        jdbcLinksRepository.addLink(link3);
        jdbcChatsToLinksRepository.addLinkToChat(chat1, link1);
        jdbcChatsToLinksRepository.addLinkToChat(chat1, link2);
        jdbcChatsToLinksRepository.addLinkToChat(chat2, link3);

        List<Link> links1 = jdbcChatsToLinksRepository.getAllLinksByChat(chat1);
        List<Link> links2 = jdbcChatsToLinksRepository.getAllLinksByChat(chat2);

        // Assert
        assertEquals(links1.size(), 2);
        assertEquals(links1.getFirst().getLinkId(), link_id1);
        assertEquals(links1.getFirst().getUrl(), url1);
        assertEquals(links1.getLast().getLinkId(), link_id2);
        assertEquals(links1.getLast().getUrl(), url2);

        assertEquals(links2.size(), 1);
        assertEquals(links2.getFirst().getLinkId(), link_id3);
        assertEquals(links2.getFirst().getUrl(), url3);
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
        jdbcChatsRepository.addChat(chat1);
        jdbcChatsRepository.addChat(chat2);
        jdbcChatsRepository.addChat(chat3);
        jdbcLinksRepository.addLink(link);
        jdbcChatsToLinksRepository.addLinkToChat(chat1, link);
        jdbcChatsToLinksRepository.addLinkToChat(chat2, link);

        List<Long> chatIds = jdbcChatsToLinksRepository.getAllChatsByLink(link);

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
        jdbcChatsRepository.addChat(chat);
        jdbcLinksRepository.addLink(link);

        isLinkAdded = jdbcChatsToLinksRepository.addLinkToChat(chat, link);
        assertTrue(isLinkAdded);
        isLinkAdded = jdbcChatsToLinksRepository.addLinkToChat(chat, link);
        assertFalse(isLinkAdded);
    }
}
