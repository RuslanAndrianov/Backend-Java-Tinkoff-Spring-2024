package edu.java.scrapper.clientsTest;

import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.clients.GitHub.GitHubClientImpl;
import edu.java.clients.GitHub.GitHubResponse;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import edu.java.configs.RetryPolicyConfig;
import edu.java.scrapper.IntegrationEnvironment;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class GitHubClientTest extends IntegrationEnvironment {

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
        RetryPolicyConfig config = new RetryPolicyConfig(
            Map.of("github", new RetryPolicyConfig.RetryPolicySettings(
                    "fixed",
                    1,
                    Duration.of(1, ChronoUnit.SECONDS),
                    Duration.of(10, ChronoUnit.SECONDS),
                    List.of(500, 502, 503, 504, 507),
                    2
                )
            )
        );
        GitHubClientImpl gitHubClient = new GitHubClientImpl(
            config,
            "http://localhost:8080"
        );
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

        GitHubResponse gitHubResponse = gitHubClient.fetchRepository(owner, repo);

        assertEquals(
            gitHubResponse.id(),
            757504505
        );
        assertEquals(
            gitHubResponse.fullName(),
            "RuslanAndrianov/Backend-Java-Tinkoff-Spring-2024"
        );
        assertEquals(
            gitHubResponse.updatedAt(),
            OffsetDateTime.parse("2024-02-14T16:24:22Z")
        );
    }
}
