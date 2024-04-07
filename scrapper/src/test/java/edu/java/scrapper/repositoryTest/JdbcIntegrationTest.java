package edu.java.scrapper.repositoryTest;

import edu.java.domain.repository.jdbc.JdbcChatsRepository;
import edu.java.domain.repository.jdbc.JdbcChatsToLinksRepository;
import edu.java.domain.repository.jdbc.JdbcLinksRepository;
import edu.java.scrapper.IntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "app.database-access-type=jdbc")
public class JdbcIntegrationTest extends IntegrationTest {

    @Autowired
    protected JdbcChatsRepository jdbcChatsRepository;
    @Autowired
    protected JdbcLinksRepository jdbcLinksRepository;
    @Autowired
    protected JdbcChatsToLinksRepository jdbcChatsToLinksRepository;
}
