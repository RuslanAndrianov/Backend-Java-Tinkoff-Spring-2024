package edu.java.configs;

import edu.java.domain.repository.jdbc.JdbcChatsRepository;
import edu.java.domain.repository.jdbc.JdbcChatsToLinksRepository;
import edu.java.domain.repository.jdbc.JdbcLinksRepository;
import edu.java.services.ChatService;
import edu.java.services.LinkService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JdbcConfig {

    @Bean
    @ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
    ChatService chatService(
        JdbcChatsRepository jdbcChatsRepository,
        JdbcChatsToLinksRepository jdbcChatsToLinksRepository
        ) {
        return new ChatService(
            jdbcChatsRepository,
            jdbcChatsToLinksRepository
        );
    }

    @Bean
    @ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
    LinkService linkService(
        JdbcLinksRepository jdbcLinksRepository,
        JdbcChatsRepository jdbcChatsRepository,
        JdbcChatsToLinksRepository jdbcChatsToLinksRepository
    ) {
        return new LinkService(
            jdbcLinksRepository,
            jdbcChatsRepository,
            jdbcChatsToLinksRepository
        );
    }
}
