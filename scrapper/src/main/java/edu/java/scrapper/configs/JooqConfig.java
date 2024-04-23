package edu.java.scrapper.configs;

import edu.java.scrapper.domain.repository.jooq.JooqChatsRepository;
import edu.java.scrapper.domain.repository.jooq.JooqChatsToLinksRepository;
import edu.java.scrapper.domain.repository.jooq.JooqLinksRepository;
import edu.java.scrapper.services.ChatService;
import edu.java.scrapper.services.LinkService;
import java.sql.Connection;
import java.sql.DriverManager;
import lombok.SneakyThrows;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.conf.RenderNameCase;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JooqConfig {

    @Value(value = "${spring.datasource.username}")
    String username;
    @Value(value = "${spring.datasource.password}")
    String password;
    @Value(value = "${spring.datasource.url}")
    String url;

    @Bean
    @SneakyThrows
    @ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
    DSLContext dslContext() {
        var settings = new Settings().withRenderNameCase(RenderNameCase.LOWER);
        Class.forName("org.postgresql.Driver");
        Connection connection = DriverManager.getConnection(url, username, password);
        return DSL.using(connection, SQLDialect.POSTGRES, settings);
    }

    @Bean
    @ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
    ChatService chatService(
        JooqChatsRepository jooqChatsRepository,
        JooqChatsToLinksRepository jooqChatsToLinksRepository
        ) {
        return new ChatService(
            jooqChatsRepository,
            jooqChatsToLinksRepository
        );
    }

    @Bean
    @ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
    LinkService linkService(
        JooqLinksRepository jooqLinksRepository,
        JooqChatsRepository jooqChatsRepository,
        JooqChatsToLinksRepository jooqChatsToLinksRepository
    ) {
        return new LinkService(
            jooqLinksRepository,
            jooqChatsRepository,
            jooqChatsToLinksRepository
        );
    }
}
