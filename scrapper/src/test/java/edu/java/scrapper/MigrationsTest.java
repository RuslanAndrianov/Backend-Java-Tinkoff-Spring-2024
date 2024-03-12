package edu.java.scrapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MigrationsTest extends IntegrationTest {

    @Test
    public void testScenario() {

        long expectedChatId = 10L;
        long expectedLinkId = 20L;
        String expectedUrl = "https://github.com/RuslanAndrianov/Backend-Java-Tinkoff-Spring-2024";

        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSourceBuilder
            .create()
            .url(POSTGRES.getJdbcUrl())
            .username(POSTGRES.getUsername())
            .password(POSTGRES.getPassword())
            .build()
        );

        assertTrue(POSTGRES.isRunning());

        jdbcTemplate.update(
            "INSERT INTO chats VALUES (?)",
            expectedChatId);

        jdbcTemplate.update(
            "INSERT INTO links VALUES (?, ?)",
            expectedLinkId,
            expectedUrl);

        jdbcTemplate.update(
            "INSERT INTO chats_to_links VALUES (?, ?)",
            expectedChatId,
            expectedLinkId);

        Integer actualChatId = jdbcTemplate.queryForObject(
            "SELECT chat_id FROM chats WHERE chat_id = ?",
            Integer.class,
            expectedChatId);

        String actualUrl = jdbcTemplate.queryForObject(
            "SELECT url FROM links WHERE link_id = ?",
            String.class,
            expectedLinkId);

        Integer actualLinkId = jdbcTemplate.queryForObject(
            "SELECT link_id FROM chats_to_links WHERE chat_id = ?",
            Integer.class,
            expectedChatId);

        assertThat(actualChatId).isEqualTo(expectedChatId);
        assertThat(actualUrl).isEqualTo(expectedUrl);
        assertThat(actualLinkId).isEqualTo(expectedLinkId);
    }
}
