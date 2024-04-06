package edu.java.configs;

import edu.java.domain.repository.jpa.JpaChatsRepository;
import edu.java.domain.repository.jpa.JpaChatsToLinksRepository;
import edu.java.domain.repository.jpa.JpaLinksRepository;
import edu.java.services.ChatService;
import edu.java.services.LinkService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
@Configuration
public class JpaConfig {

    @Bean
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
