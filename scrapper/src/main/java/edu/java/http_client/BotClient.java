package edu.java.http_client;

import java.util.Optional;
import edu.shared_dto.request_dto.LinkUpdate;
import edu.shared_dto.response_dto.APIErrorResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class BotClient {

    @Value(value = "${api.bot.defaultUrl}")
    private String defaultUrl;

    private final WebClient webClient;

    public BotClient() {
        this.webClient = WebClient.builder().baseUrl(defaultUrl).build();
    }

    public BotClient(String baseUrl) {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    public Optional<String> updateLink(LinkUpdate request) {
        return webClient
            .post()
            .uri("/updates")
            .body(BodyInserters.fromValue(request))
            .retrieve()
            .onStatus(
                HttpStatus.BAD_REQUEST::equals,
                response -> response
                    .bodyToMono(APIErrorResponse.class)
                    .flatMap(errorResponse -> Mono.error(new Exception())))
            .bodyToMono(String.class)
            .blockOptional();
    }
}
