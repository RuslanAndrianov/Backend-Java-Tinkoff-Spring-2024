package edu.java.configs;

import edu.java.domain.dto.Chat;
import edu.java.domain.dto.Link;
import edu.shared_dto.ChatState;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Configuration
public class DomainConfig {

    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create()
            .driverClassName("org.postgresql.Driver")
            .url("jdbc:postgresql://postgresql:5432/scrapper")
            .username("postgres")
            .password("postgres")
            .build();
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource source) {
        return new JdbcTemplate(source);
    }

    @Bean
    public RowMapper<Chat> chatRowMapper() {
        return (resultSet, rowNum) ->
            new Chat(
                resultSet.getLong("chat_id"),
                resultSet.getObject("state", ChatState.class));
    }

    @Bean
    public RowMapper<Link> linkRowMapper() {
        return (resultSet, rowNum) ->
            new Link(
                resultSet.getLong("link_id"),
                resultSet.getURL("url"),
                timestampToOffsetDate(resultSet.getTimestamp("last_updated"))
            );

    }

    private OffsetDateTime timestampToOffsetDate(@NotNull Timestamp timestamp) {
        return OffsetDateTime.of(timestamp.toLocalDateTime(), ZoneOffset.of("Z"));
    }
}
