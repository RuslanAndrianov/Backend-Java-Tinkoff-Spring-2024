package edu.java.configs;

import edu.java.domain.repository.jdbc.JdbcChatsRepository;
import edu.java.domain.repository.jdbc.JdbcChatsToLinksRepository;
import edu.java.domain.repository.jdbc.JdbcLinksRepository;
import edu.java.services.ChatService;
import edu.java.services.LinkService;
import edu.java.services.jdbc.JdbcChatService;
import edu.java.services.jdbc.JdbcLinkService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JdbcConfig {
    @Bean
    ChatService chatService(
        JdbcChatsRepository jdbcChatsRepository,
        JdbcChatsToLinksRepository jdbcChatsToLinksRepository
        ) {
        return new JdbcChatService(
            jdbcChatsRepository,
            jdbcChatsToLinksRepository
        );
    }

    @Bean
    LinkService linkService(
        JdbcLinksRepository jdbcLinksRepository,
        JdbcChatsRepository jdbcChatsRepository,
        JdbcChatsToLinksRepository jdbcChatsToLinksRepository
    ) {
        return new JdbcLinkService(
            jdbcLinksRepository,
            jdbcChatsRepository,
            jdbcChatsToLinksRepository
        );
    }
}
