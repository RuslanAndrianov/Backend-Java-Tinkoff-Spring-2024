package edu.java.scrapper.clientsTest;

import com.github.tomakehurst.wiremock.WireMockServer;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.time.OffsetDateTime;
import edu.java.clients.GitHub.GitHubClientImpl;
import edu.java.clients.GitHub.RepositoryResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootTest
public class GitHubClientTest {

    private static WireMockServer wireMockServer;
    private final WebClient.Builder webClientBuilder;

    @Autowired
    public GitHubClientTest(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @BeforeAll
    public static void beforeAll() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();
    }

    @AfterAll
    public static void afterAll() {
        wireMockServer.stop();
    }

    @Test
    public void fetchRepositoryTest() {
        GitHubClientImpl gitHubClient = new GitHubClientImpl("http://localhost:8080");
        String owner = "RuslanAndrianov";
        String repo = "Backend-Java-Tinkoff-Spring-2024";
        stubFor(get(urlPathMatching(String.format("/repos/%s/%s", owner, repo)))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json; charset=utf-8")
                .withBody("""
                                {
                                    "id": 757504505,
                                    "full_name": "RuslanAndrianov/Backend-Java-Tinkoff-Spring-2024",
                                    "updated_at": "2024-02-14T16:24:22Z"
                                }
                                """)));

        RepositoryResponse repositoryResponse = gitHubClient.fetchRepository(owner, repo);

        assertEquals(repositoryResponse.id(),
            757504505);
        assertEquals(repositoryResponse.fullName(),
            "RuslanAndrianov/Backend-Java-Tinkoff-Spring-2024");
        assertEquals(repositoryResponse.updatedAt(),
            OffsetDateTime.parse("2024-02-14T16:24:22Z"));
    }
}
