package edu.java.scrapper.repositoryTest;

import edu.java.domain.repository.jpa.JpaChatsRepository;
import edu.java.domain.repository.jpa.JpaChatsToLinksRepository;
import edu.java.domain.repository.jpa.JpaLinksRepository;
import edu.java.scrapper.IntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "app.database-access-type=jpa")
public class JpaIntegrationTest extends IntegrationTest {

    @Autowired
    protected JpaChatsRepository jpaChatsRepository;
    @Autowired
    protected JpaLinksRepository jpaLinksRepository;
    @Autowired
    protected JpaChatsToLinksRepository jpaChatsToLinksRepository;
}

