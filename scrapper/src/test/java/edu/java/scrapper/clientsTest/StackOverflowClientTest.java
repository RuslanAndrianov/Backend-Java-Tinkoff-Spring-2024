package edu.java.scrapper.clientsTest;

import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.scrapper.clients.StackOverflow.StackOverflowClientImpl;
import edu.java.scrapper.clients.StackOverflow.StackOverflowItemsResponse;
import edu.java.scrapper.clients.StackOverflow.StackOverflowResponse;
import edu.java.scrapper.configs.RetryPolicyConfig;
import edu.java.scrapper.IntegrationEnvironment;
import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
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
public class StackOverflowClientTest extends IntegrationEnvironment {
    private static WireMockServer wireMockServer;
    private final WebClient.Builder webClientBuilder;

    @Autowired
    public StackOverflowClientTest(WebClient.Builder webClientBuilder) {
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
    public void testFetchQuestion() {
        // Arrange
        RetryPolicyConfig config = new RetryPolicyConfig(
            Map.of("stackoverflow", new RetryPolicyConfig.RetryPolicySettings(
                    "linear",
                    5,
                    Duration.of(1, ChronoUnit.SECONDS),
                    Duration.of(10, ChronoUnit.SECONDS),
                    List.of(500, 502, 503, 504, 507),
                    2
                )
            )
        );
        StackOverflowClientImpl stackOverflowClient = new StackOverflowClientImpl(
            config,
            "http://localhost:8080");

        Long questionId = 12345671L;

        stubFor(get(urlPathMatching(String.format
            ("/questions/%s", questionId)))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json; charset=utf-8")
                .withBody("""
                    {
                        "items": [
                            {
                                "question_id": 12345671,
                                "title": "Get and set value spinner from other activity in android",
                                "last_activity_date": 1347254940
                            }
                        ]
                    }
                    """)));

        StackOverflowItemsResponse stackOverflowItemsResponse = stackOverflowClient.fetchQuestion(questionId);
        StackOverflowResponse properties = stackOverflowItemsResponse.deserialize();

        assertEquals(properties.questionId(), 12345671);
        assertEquals(properties.title(),
            "Get and set value spinner from other activity in android");
        assertEquals(properties.lastActivityDate(),
            OffsetDateTime.ofInstant(Instant.ofEpochSecond(1347254940L), ZoneId.of("UTC")));
    }
}
