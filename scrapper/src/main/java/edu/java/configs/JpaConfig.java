package edu.java.configs;

import edu.java.domain.repository.jpa.JpaChatsRepository;
import edu.java.domain.repository.jpa.JpaChatsToLinksRepository;
import edu.java.domain.repository.jpa.JpaLinksRepository;
import edu.java.services.ChatService;
import edu.java.services.LinkService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JpaConfig {

    @Bean
    @ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
    ChatService chatService(
        JpaChatsRepository jpaChatsRepository,
        JpaChatsToLinksRepository jpaChatsToLinksRepository
    ) {
        return new ChatService(
            jpaChatsRepository,
            jpaChatsToLinksRepository
        );
    }

    @Bean
    @ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
    LinkService linkService(
        JpaLinksRepository jpaLinksRepository,
        JpaChatsRepository jpaChatsRepository,
        JpaChatsToLinksRepository jpaChatsToLinksRepository
    ) {
        return new LinkService(
            jpaLinksRepository,
            jpaChatsRepository,
            jpaChatsToLinksRepository
        );
    }
}
