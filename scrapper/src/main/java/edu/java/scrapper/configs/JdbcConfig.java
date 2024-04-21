package edu.java.scrapper.configs;

import edu.java.scrapper.domain.repository.jdbc.JdbcChatsRepository;
import edu.java.scrapper.domain.repository.jdbc.JdbcChatsToLinksRepository;
import edu.java.scrapper.domain.repository.jdbc.JdbcLinksRepository;
import edu.java.scrapper.services.ChatService;
import edu.java.scrapper.services.LinkService;
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
