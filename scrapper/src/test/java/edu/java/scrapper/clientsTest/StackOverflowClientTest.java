package edu.java.scrapper.clientsTest;

import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.clients.StackOverflow.StackOverflowResponse;
import edu.java.clients.StackOverflow.StackOverflowClientImpl;
import edu.java.clients.StackOverflow.StackOverflowItemsResponse;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
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
public class StackOverflowClientTest {
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
    @DisplayName("test for check the required response body")
    public void testFetchQuestion() {
        // Arrange
        StackOverflowClientImpl stackOverflowClient = new StackOverflowClientImpl("http://localhost:8080");

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
