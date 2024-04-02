package edu.java.configs;

import edu.java.domain.repository.jpa.chatsRepository.JpaChatsRepository;
import edu.java.domain.repository.jpa.chatsToLinksRepository.JpaChatsToLinksRepository;
import edu.java.domain.repository.jpa.linksRepository.JpaLinksRepository;
import edu.java.services.ChatService;
import edu.java.services.LinkService;
import edu.java.services.jpa.JpaChatService;
import edu.java.services.jpa.JpaLinkService;
import jakarta.persistence.EntityManager;
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
        return new JpaChatService(
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
        return new JpaLinkService(
            jpaLinksRepository,
            jpaChatsRepository,
            jpaChatsToLinksRepository
        );
    }
}
