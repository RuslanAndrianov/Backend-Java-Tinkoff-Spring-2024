package edu.java.scrapper.repositoryTest;

import edu.java.domain.dto.Chat;
import edu.java.domain.dto.Link;
import edu.java.domain.repository.ChatsRepository;
import edu.java.domain.repository.ChatsToLinksRepository;
import edu.java.domain.repository.LinksRepository;
import edu.java.scrapper.IntegrationTest;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import static edu.shared_dto.ChatState.REGISTERED;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
        chatsToLinksRepository = new ChatsToLinksRepository(jdbcTemplate, linkRowMapper);
    }

    @Test
    @Transactional
    @Rollback
    void addLinkTest() {
        long chat_id7 = 7L;
        long link_id7 = 77L;
        String url1 = "https://github.com/RuslanAndrianov/Backend-Java-Tinkoff-Spring-2024";

        Chat chat1 = new Chat(chat_id7, REGISTERED.toString());
        Link link1 = new Link(link_id7, url1, OffsetDateTime.now());

        chatsRepository.add(chat1);
        linksRepository.add(link1);
        chatsToLinksRepository.addLink(chat1, link1);

        List<Link> links = chatsToLinksRepository.findAllLinksByChat(chat1);

        assertEquals(links.size(), 1);
        assertEquals(links.getFirst().linkId(), link_id7);
        assertEquals(links.getFirst().url(), url1);

        chatsToLinksRepository.removeLink(chat1, link1);
        chatsRepository.remove(chat1);
        linksRepository.remove(link1);
    }

    @Test
    @Transactional
    @Rollback
    void removeLinkTest() {
        long chat_id8 = 8L;
        long link_id8 = 8L;
        String url1 = "https://github.com/RuslanAndrianov/Backend-Java-Tinkoff-Spring-2024";

        Chat chat1 = new Chat(chat_id8, REGISTERED.toString());
        Link link1 = new Link(link_id8, url1, OffsetDateTime.now());

        chatsRepository.add(chat1);
        linksRepository.add(link1);
        chatsToLinksRepository.addLink(chat1, link1);

        chatsToLinksRepository.removeLink(chat1, link1);
        chatsRepository.remove(chat1);
        linksRepository.remove(link1);

        List<Link> links = chatsToLinksRepository.findAllLinksByChat(chat1);

        assertEquals(links.size(), 0);
    }

    @Test
    @Transactional
    @Rollback
    void removeChatTest() {
        String url1 =
            "https://stackoverflow.com/questions/54378414/how-to-fix-cant-infer-the-sql-type-to-use-for-an-instance-of-enum-error-when";
        String url2 =
            "https://stackoverflow.com/questions/50145552/error-org-springframework-jdbc-badsqlgrammarexception-statementcallback-bad-s";

        long chat_id9 = 9L;
        long link_id9 = 9L;
        long link_id10 = 10L;

        Link link1 = new Link(link_id9, url1, OffsetDateTime.now());
        Link link2 = new Link(link_id10, url2, OffsetDateTime.now());

        Chat chat1 = new Chat(chat_id9, REGISTERED.toString());

        chatsRepository.add(chat1);
        linksRepository.add(link1);
        linksRepository.add(link2);
        chatsToLinksRepository.addLink(chat1, link1);
        chatsToLinksRepository.addLink(chat1, link2);

        chatsToLinksRepository.removeChat(chat1);
        chatsRepository.remove(chat1);
        linksRepository.remove(link1);
        linksRepository.remove(link2);
        List<Link> links = chatsToLinksRepository.findAllLinksByChat(chat1);

        assertEquals(links.size(), 0);
    }

    @Test
    @Transactional
    @Rollback
    void findAllLinksByChatTest() {
        String url1 =
            "https://stackoverflow.com/questions/54378414/how-to-fix-cant-infer-the-sql-type-to-use-for-an-instance-of-enum-error-when";
        String url2 =
            "https://stackoverflow.com/questions/50145552/error-org-springframework-jdbc-badsqlgrammarexception-statementcallback-bad-s";
        String url3 = "https://github.com/RuslanAndrianov/Backend-Java-Tinkoff-Spring-2024";

        long chat_id10 = 10L;
        long chat_id11 = 11L;
        long link_id11 = 11L;
        long link_id12 = 12L;
        long link_id13 = 13L;

        Link link1 = new Link(link_id11, url1, OffsetDateTime.now());
        Link link2 = new Link(link_id12, url2, OffsetDateTime.now());
        Link link3 = new Link(link_id13, url3, OffsetDateTime.now());

        Chat chat1 = new Chat(chat_id10, REGISTERED.toString());
        Chat chat2 = new Chat(chat_id11, REGISTERED.toString());

        chatsRepository.add(chat1);
        chatsRepository.add(chat2);
        linksRepository.add(link1);
        linksRepository.add(link2);
        linksRepository.add(link3);
        chatsToLinksRepository.addLink(chat1, link1);
        chatsToLinksRepository.addLink(chat1, link2);
        chatsToLinksRepository.addLink(chat2, link3);

        List<Link> links1 = chatsToLinksRepository.findAllLinksByChat(chat1);
        List<Link> links2 = chatsToLinksRepository.findAllLinksByChat(chat2);

        assertEquals(links1.size(), 2);
        assertEquals(links1.getFirst().linkId(), link_id11);
        assertEquals(links1.getFirst().url(), url1);
        assertEquals(links1.get(1).linkId(), link_id12);
        assertEquals(links1.get(1).url(), url2);

        assertEquals(links2.size(), 1);
        assertEquals(links2.getFirst().linkId(), link_id13);
        assertEquals(links2.getFirst().url(), url3);

        chatsToLinksRepository.removeLink(chat1, link1);
        chatsToLinksRepository.removeLink(chat1, link2);
        chatsToLinksRepository.removeLink(chat2, link3);
        chatsRepository.remove(chat1);
        chatsRepository.remove(chat2);
        linksRepository.remove(link1);
        linksRepository.remove(link2);
        linksRepository.remove(link3);
    }

    @Test
    @Transactional
    @Rollback
    void duplicateKeyTest() {
        String url1 = "https://github.com/RuslanAndrianov/Backend-Java-Tinkoff-Spring-2024";

        long chat_id12 = 12L;
        long link_id14 = 14L;

        Chat chat1 = new Chat(chat_id12, REGISTERED.toString());
        Link link1 = new Link(link_id14, url1, OffsetDateTime.now());
        boolean isError = false;

        chatsRepository.add(chat1);
        linksRepository.add(link1);
        chatsToLinksRepository.addLink(chat1, link1);

        try {
            chatsToLinksRepository.addLink(chat1, link1);
        } catch (DuplicateKeyException e) {
            isError = true;
        }

        assertTrue(isError);

        chatsToLinksRepository.removeChat(chat1);
        chatsRepository.remove(chat1);
        linksRepository.remove(link1);
    }
}
