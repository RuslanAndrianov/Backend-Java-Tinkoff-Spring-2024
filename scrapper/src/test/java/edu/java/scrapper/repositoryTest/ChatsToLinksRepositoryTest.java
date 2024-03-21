package edu.java.scrapper.repositoryTest;

import edu.java.domain.dto.Chat;
import edu.java.domain.dto.Link;
import edu.java.domain.repository.ChatsRepository;
import edu.java.domain.repository.ChatsToLinksRepository;
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
import static edu.shared_dto.ChatState.REGISTERED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ChatsToLinksRepositoryTest extends IntegrationTest {

    private static final ChatsToLinksRepository chatsToLinksRepository;
    private static final ChatsRepository chatsRepository;
    private static final LinksRepository linksRepository;

    private static final RowMapper<Chat> chatRowMapper = (resultSet, rowNum) ->
        new Chat(
            resultSet.getLong("chat_id"),
            resultSet.getString("chat_state")
        );

    private static final RowMapper<Link> linkRowMapper = (resultSet, rowNum) ->
        new Link(
            resultSet.getLong("link_id"),
            resultSet.getString("url"),
            timestampToOffsetDate(resultSet.getTimestamp("last_updated"))
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

        chatsRepository = new ChatsRepository(jdbcTemplate, chatRowMapper);
        linksRepository = new LinksRepository(jdbcTemplate, linkRowMapper);
        chatsToLinksRepository = new ChatsToLinksRepository(
            jdbcTemplate, linkRowMapper, chatLinkRowMapper);
    }

    @Test
    @Transactional
    @Rollback
    void addLinkToChatTest() {
        long chat_id = 10L;
        long link_id = 10L;
        String url = "https://github.com/RuslanAndrianov/Backend-Java-Tinkoff-Spring-2024";
        Chat chat = new Chat(chat_id, REGISTERED.toString());
        Link link = new Link(link_id, url, OffsetDateTime.now());
        boolean isLinkAdded;

        chatsRepository.addChat(chat);
        linksRepository.addLink(link);

        List<Link> linksBefore = chatsToLinksRepository.getAllLinksByChat(chat);

        isLinkAdded = chatsToLinksRepository.addLinkToChat(chat, link);

        List<Link> linksAfter = chatsToLinksRepository.getAllLinksByChat(chat);

        assertTrue(isLinkAdded);
        assertEquals(linksAfter.size() - linksBefore.size(), 1);

        chatsToLinksRepository.deleteLinkFromChat(chat, link);
        chatsRepository.deleteChat(chat);
        linksRepository.deleteLink(link);
    }

    @Test
    @Transactional
    @Rollback
    void deleteLinkFromChatTest() {
        long chat_id = 11L;
        long link_id = 11L;
        String url = "https://github.com/RuslanAndrianov/Backend-Java-Tinkoff-Spring-2024";

        Chat chat = new Chat(chat_id, REGISTERED.toString());
        Link link = new Link(link_id, url, OffsetDateTime.now());
        boolean isLinkDeleted;

        List<Link> linksBefore = chatsToLinksRepository.getAllLinksByChat(chat);

        chatsRepository.addChat(chat);
        linksRepository.addLink(link);
        chatsToLinksRepository.addLinkToChat(chat, link);

        isLinkDeleted = chatsToLinksRepository.deleteLinkFromChat(chat, link);
        chatsRepository.deleteChat(chat);
        linksRepository.deleteLink(link);

        List<Link> linksAfter = chatsToLinksRepository.getAllLinksByChat(chat);

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
        Chat chat = new Chat(chat_id, REGISTERED.toString());
        Link link1 = new Link(link_id1, url1, OffsetDateTime.now());
        Link link2 = new Link(link_id2, url2, OffsetDateTime.now());
        boolean isChatDeleted;

        chatsRepository.addChat(chat);
        linksRepository.addLink(link1);
        linksRepository.addLink(link2);

        List<Link> linksBefore = chatsToLinksRepository.getAllLinksByChat(chat);
        chatsToLinksRepository.addLinkToChat(chat, link1);
        chatsToLinksRepository.addLinkToChat(chat, link2);

        isChatDeleted = chatsToLinksRepository.deleteChat(chat);
        chatsRepository.deleteChat(chat);
        linksRepository.deleteLink(link1);
        linksRepository.deleteLink(link2);
        List<Link> linksAfter = chatsToLinksRepository.getAllLinksByChat(chat);

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
        Chat chat = new Chat(chat_id, REGISTERED.toString());
        Link link = new Link(link_id, url, OffsetDateTime.now());
        boolean isChatExist;

        chatsRepository.addChat(chat);
        linksRepository.addLink(link);
        chatsToLinksRepository.addLinkToChat(chat, link);

        isChatExist = chatsToLinksRepository.isChatExist(chat);

        assertTrue(isChatExist);

        chatsToLinksRepository.deleteChat(chat);
        chatsRepository.deleteChat(chat);
        linksRepository.deleteLink(link);

        isChatExist = chatsToLinksRepository.isChatExist(chat);

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
        Link link1 = new Link(link_id1, url1, OffsetDateTime.now());
        Link link2 = new Link(link_id2, url2, OffsetDateTime.now());
        Link link3 = new Link(link_id3, url3, OffsetDateTime.now());
        Chat chat1 = new Chat(chat_id1, REGISTERED.toString());
        Chat chat2 = new Chat(chat_id2, REGISTERED.toString());

        chatsRepository.addChat(chat1);
        chatsRepository.addChat(chat2);
        linksRepository.addLink(link1);
        linksRepository.addLink(link2);
        linksRepository.addLink(link3);
        chatsToLinksRepository.addLinkToChat(chat1, link1);
        chatsToLinksRepository.addLinkToChat(chat1, link2);
        chatsToLinksRepository.addLinkToChat(chat2, link3);

        List<Link> links1 = chatsToLinksRepository.getAllLinksByChat(chat1);
        List<Link> links2 = chatsToLinksRepository.getAllLinksByChat(chat2);

        assertEquals(links1.size(), 2);
        assertEquals(links1.getFirst().linkId(), link_id1);
        assertEquals(links1.getFirst().url(), url1);
        assertEquals(links1.getLast().linkId(), link_id2);
        assertEquals(links1.getLast().url(), url2);

        assertEquals(links2.size(), 1);
        assertEquals(links2.getFirst().linkId(), link_id3);
        assertEquals(links2.getFirst().url(), url3);

        chatsToLinksRepository.deleteLinkFromChat(chat1, link1);
        chatsToLinksRepository.deleteLinkFromChat(chat1, link2);
        chatsToLinksRepository.deleteLinkFromChat(chat2, link3);
        chatsRepository.deleteChat(chat1);
        chatsRepository.deleteChat(chat2);
        linksRepository.deleteLink(link1);
        linksRepository.deleteLink(link2);
        linksRepository.deleteLink(link3);
    }

    @Test
    @Transactional
    @Rollback
    void duplicateKeyTest() {
        long chat_id = 16L;
        long link_id = 18L;
        String url = "https://github.com/RuslanAndrianov/Backend-Java-Tinkoff-Spring-2024";
        Chat chat = new Chat(chat_id, REGISTERED.toString());
        Link link = new Link(link_id, url, OffsetDateTime.now());
        boolean isLinkAdded;

        chatsRepository.addChat(chat);
        linksRepository.addLink(link);

        isLinkAdded = chatsToLinksRepository.addLinkToChat(chat, link);
        assertTrue(isLinkAdded);
        isLinkAdded = chatsToLinksRepository.addLinkToChat(chat, link);
        assertFalse(isLinkAdded);

        chatsToLinksRepository.deleteChat(chat);
        chatsRepository.deleteChat(chat);
        linksRepository.deleteLink(link);
    }
}
