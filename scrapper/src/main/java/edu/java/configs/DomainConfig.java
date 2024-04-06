package edu.java.configs;

import edu.java.domain.dto.Chat;
import edu.java.domain.dto.Link;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import javax.sql.DataSource;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

@Configuration
@SuppressWarnings("MultipleStringLiterals")
public class DomainConfig {

    @Bean
    public DataSource dataSource() {
        String postgres = "postgres";
        return DataSourceBuilder
            .create()
            .driverClassName("org.postgresql.Driver")
            .url("jdbc:postgresql://localhost:5437/scrapper")
            .username(postgres)
            .password(postgres)
            .build();
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource source) {
        return new JdbcTemplate(source);
    }

    @Bean
    public RowMapper<Chat> chatRowMapper() {
        return (resultSet, rowNum) -> {
            Chat chat = new Chat();
            chat.setChatId(resultSet.getLong("chat_id"));
            return chat;
        };
    }

    @Bean
    public RowMapper<Link> linkRowMapper() {
        return (resultSet, rowNum) -> {
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
    }

    @Bean
    public RowMapper<Long> chatLinkRowMapper() {
        return (resultSet, rowNum) -> resultSet.getLong("chat_id");
    }

    private @NotNull OffsetDateTime timestampToOffsetDate(@NotNull Timestamp timestamp) {
        return OffsetDateTime.of(timestamp.toLocalDateTime(), ZoneOffset.of("Z"));
    }
}
