package edu.java.scrapper.repositoryTest;

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

    private static final JdbcChatsToLinksRepository JDBC_CHATS_TO_LINKS_REPOSITORY;
    private static final JdbcChatsRepository JDBC_CHATS_REPOSITORY;
    private static final JdbcLinksRepository JDBC_LINKS_REPOSITORY;

    private static final RowMapper<Chat> chatRowMapper = (resultSet, rowNum) ->
        new Chat(resultSet.getLong("chat_id"));

    private static final RowMapper<Link> linkRowMapper = (resultSet, rowNum) ->
        new Link(
            resultSet.getLong("link_id"),
            resultSet.getString("url"),
            timestampToOffsetDate(resultSet.getTimestamp("last_updated")),
            timestampToOffsetDate(resultSet.getTimestamp("last_checked")),
            resultSet.getInt("zone_offset")
        );

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

        JDBC_CHATS_REPOSITORY = new JdbcChatsRepository(jdbcTemplate, chatRowMapper);
        JDBC_LINKS_REPOSITORY = new JdbcLinksRepository(jdbcTemplate, linkRowMapper);
        JDBC_CHATS_TO_LINKS_REPOSITORY = new JdbcChatsToLinksRepository(
            jdbcTemplate, chatLinkRowMapper, linkRowMapper);
    }

    @Test
    @Transactional
    @Rollback
    void addLinkToChatTest() {
        long chat_id = 10L;
        long link_id = 10L;
        String url = "https://github.com/RuslanAndrianov/Backend-Java-Tinkoff-Spring-2024";
        Chat chat = new Chat(chat_id);
        Link link = new Link(link_id, url, OffsetDateTime.now(), OffsetDateTime.now(), 0);
        boolean isLinkAdded;

        JDBC_CHATS_REPOSITORY.addChat(chat);
        JDBC_LINKS_REPOSITORY.addLink(link);

        List<Link> linksBefore = JDBC_CHATS_TO_LINKS_REPOSITORY.getAllLinksByChat(chat);

        isLinkAdded = JDBC_CHATS_TO_LINKS_REPOSITORY.addLinkToChat(chat, link);

        List<Link> linksAfter = JDBC_CHATS_TO_LINKS_REPOSITORY.getAllLinksByChat(chat);

        assertTrue(isLinkAdded);
        assertEquals(linksAfter.size() - linksBefore.size(), 1);

        JDBC_CHATS_TO_LINKS_REPOSITORY.deleteLinkFromChat(chat, link);
        JDBC_CHATS_REPOSITORY.deleteChat(chat);
        JDBC_LINKS_REPOSITORY.deleteLink(link);
    }

    @Test
    @Transactional
    @Rollback
    void deleteLinkFromChatTest() {
        long chat_id = 11L;
        long link_id = 11L;
        String url = "https://github.com/RuslanAndrianov/Backend-Java-Tinkoff-Spring-2024";

        Chat chat = new Chat(chat_id);
        Link link = new Link(link_id, url, OffsetDateTime.now(), OffsetDateTime.now(), 0);
        boolean isLinkDeleted;

        List<Link> linksBefore = JDBC_CHATS_TO_LINKS_REPOSITORY.getAllLinksByChat(chat);

        JDBC_CHATS_REPOSITORY.addChat(chat);
        JDBC_LINKS_REPOSITORY.addLink(link);
        JDBC_CHATS_TO_LINKS_REPOSITORY.addLinkToChat(chat, link);

        isLinkDeleted = JDBC_CHATS_TO_LINKS_REPOSITORY.deleteLinkFromChat(chat, link);
        JDBC_CHATS_REPOSITORY.deleteChat(chat);
        JDBC_LINKS_REPOSITORY.deleteLink(link);

        List<Link> linksAfter = JDBC_CHATS_TO_LINKS_REPOSITORY.getAllLinksByChat(chat);

        assertTrue(isLinkDeleted);
        assertEquals(linksBefore.size(), linksAfter.size());
    }

    @Test
    @Transactional
    @Rollback
    void deleteChatTest() {
        long chat_id = 12L;
        long link_id1 = 12L;
        long link_id2 = 13L;
        String url1 =
            "https://stackoverflow.com/questions/54378414/how-to-fix-cant-infer-the-sql-type-to-use-for-an-instance-of-enum-error-when";
        String url2 =
            "https://stackoverflow.com/questions/50145552/error-org-springframework-jdbc-badsqlgrammarexception-statementcallback-bad-s";
        Chat chat = new Chat(chat_id);
        Link link1 = new Link(link_id1, url1, OffsetDateTime.now(), OffsetDateTime.now(), 0);
        Link link2 = new Link(link_id2, url2, OffsetDateTime.now(), OffsetDateTime.now(), 0);
        boolean isChatDeleted;

        JDBC_CHATS_REPOSITORY.addChat(chat);
        JDBC_LINKS_REPOSITORY.addLink(link1);
        JDBC_LINKS_REPOSITORY.addLink(link2);

        List<Link> linksBefore = JDBC_CHATS_TO_LINKS_REPOSITORY.getAllLinksByChat(chat);
        JDBC_CHATS_TO_LINKS_REPOSITORY.addLinkToChat(chat, link1);
        JDBC_CHATS_TO_LINKS_REPOSITORY.addLinkToChat(chat, link2);

        isChatDeleted = JDBC_CHATS_TO_LINKS_REPOSITORY.deleteChat(chat);
        JDBC_CHATS_REPOSITORY.deleteChat(chat);
        JDBC_LINKS_REPOSITORY.deleteLink(link1);
        JDBC_LINKS_REPOSITORY.deleteLink(link2);
        List<Link> linksAfter = JDBC_CHATS_TO_LINKS_REPOSITORY.getAllLinksByChat(chat);

        assertTrue(isChatDeleted);
        assertEquals(linksBefore.size(), linksAfter.size());
    }

    @Test
    @Transactional
    @Rollback
    void isChatExistTest() {
        long chat_id = 13L;
        long link_id = 14L;
        String url = "https://github.com/RuslanAndrianov/Backend-Java-Tinkoff-Spring-2024";
        Chat chat = new Chat(chat_id);
        Link link = new Link(link_id, url, OffsetDateTime.now(), OffsetDateTime.now(), 0);
        boolean isChatExist;

        JDBC_CHATS_REPOSITORY.addChat(chat);
        JDBC_LINKS_REPOSITORY.addLink(link);
        JDBC_CHATS_TO_LINKS_REPOSITORY.addLinkToChat(chat, link);

        isChatExist = JDBC_CHATS_TO_LINKS_REPOSITORY.isChatExist(chat);

        assertTrue(isChatExist);

        JDBC_CHATS_TO_LINKS_REPOSITORY.deleteChat(chat);
        JDBC_CHATS_REPOSITORY.deleteChat(chat);
        JDBC_LINKS_REPOSITORY.deleteLink(link);

        isChatExist = JDBC_CHATS_TO_LINKS_REPOSITORY.isChatExist(chat);

        assertFalse(isChatExist);
    }

    @Test
    @Transactional
    @Rollback
    void getAllLinksByChatTest() {
        long chat_id1 = 14L;
        long chat_id2 = 15L;
        long link_id1 = 15L;
        long link_id2 = 16L;
        long link_id3 = 17L;
        String url1 =
            "https://stackoverflow.com/questions/54378414/how-to-fix-cant-infer-the-sql-type-to-use-for-an-instance-of-enum-error-when";
        String url2 =
            "https://stackoverflow.com/questions/50145552/error-org-springframework-jdbc-badsqlgrammarexception-statementcallback-bad-s";
        String url3 = "https://github.com/RuslanAndrianov/Backend-Java-Tinkoff-Spring-2024";
        Link link1 = new Link(link_id1, url1, OffsetDateTime.now(), OffsetDateTime.now(), 0);
        Link link2 = new Link(link_id2, url2, OffsetDateTime.now(), OffsetDateTime.now(), 0);
        Link link3 = new Link(link_id3, url3, OffsetDateTime.now(), OffsetDateTime.now(), 0);
        Chat chat1 = new Chat(chat_id1);
        Chat chat2 = new Chat(chat_id2);

        JDBC_CHATS_REPOSITORY.addChat(chat1);
        JDBC_CHATS_REPOSITORY.addChat(chat2);
        JDBC_LINKS_REPOSITORY.addLink(link1);
        JDBC_LINKS_REPOSITORY.addLink(link2);
        JDBC_LINKS_REPOSITORY.addLink(link3);
        JDBC_CHATS_TO_LINKS_REPOSITORY.addLinkToChat(chat1, link1);
        JDBC_CHATS_TO_LINKS_REPOSITORY.addLinkToChat(chat1, link2);
        JDBC_CHATS_TO_LINKS_REPOSITORY.addLinkToChat(chat2, link3);

        List<Link> links1 = JDBC_CHATS_TO_LINKS_REPOSITORY.getAllLinksByChat(chat1);
        List<Link> links2 = JDBC_CHATS_TO_LINKS_REPOSITORY.getAllLinksByChat(chat2);

        assertEquals(links1.size(), 2);
        assertEquals(links1.getFirst().linkId(), link_id1);
        assertEquals(links1.getFirst().url(), url1);
        assertEquals(links1.getLast().linkId(), link_id2);
        assertEquals(links1.getLast().url(), url2);

        assertEquals(links2.size(), 1);
        assertEquals(links2.getFirst().linkId(), link_id3);
        assertEquals(links2.getFirst().url(), url3);

        JDBC_CHATS_TO_LINKS_REPOSITORY.deleteLinkFromChat(chat1, link1);
        JDBC_CHATS_TO_LINKS_REPOSITORY.deleteLinkFromChat(chat1, link2);
        JDBC_CHATS_TO_LINKS_REPOSITORY.deleteLinkFromChat(chat2, link3);
        JDBC_CHATS_REPOSITORY.deleteChat(chat1);
        JDBC_CHATS_REPOSITORY.deleteChat(chat2);
        JDBC_LINKS_REPOSITORY.deleteLink(link1);
        JDBC_LINKS_REPOSITORY.deleteLink(link2);
        JDBC_LINKS_REPOSITORY.deleteLink(link3);
    }

    @Test
    @Transactional
    @Rollback
    void duplicateKeyTest() {
        long chat_id = 16L;
        long link_id = 18L;
        String url = "https://github.com/RuslanAndrianov/Backend-Java-Tinkoff-Spring-2024";
        Chat chat = new Chat(chat_id);
        Link link = new Link(link_id, url, OffsetDateTime.now(), OffsetDateTime.now(), 0);
        boolean isLinkAdded;

        JDBC_CHATS_REPOSITORY.addChat(chat);
        JDBC_LINKS_REPOSITORY.addLink(link);

        isLinkAdded = JDBC_CHATS_TO_LINKS_REPOSITORY.addLinkToChat(chat, link);
        assertTrue(isLinkAdded);
        isLinkAdded = JDBC_CHATS_TO_LINKS_REPOSITORY.addLinkToChat(chat, link);
        assertFalse(isLinkAdded);

        JDBC_CHATS_TO_LINKS_REPOSITORY.deleteChat(chat);
        JDBC_CHATS_REPOSITORY.deleteChat(chat);
        JDBC_LINKS_REPOSITORY.deleteLink(link);
    }
}
