package edu.java.clients;

import edu.shared_dto.request_dto.LinkUpdateRequest;
import edu.shared_dto.response_dto.APIErrorResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class BotClient {

    @Value(value = "${api.bot.defaultUrl}")
    private String defaultUrl;

    private final WebClient webClient;

    public BotClient() {
        this.webClient = WebClient
            .builder()
            .baseUrl(defaultUrl)
            .build();
    }

    public BotClient(String baseUrl) {
        this.webClient = WebClient
            .builder()
            .baseUrl(baseUrl)
            .build();
        this.defaultUrl = baseUrl;
    }

    public String updateLink(LinkUpdateRequest request) {
        return webClient
            .post()
            .uri(defaultUrl + "/updates")
            .body(BodyInserters.fromValue(request))
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                response -> response
                    .bodyToMono(APIErrorResponse.class)
                    .flatMap(errorResponse -> Mono.error(new Exception())))
            .bodyToMono(String.class)
            .block();
    }
}
