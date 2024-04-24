package edu.java.scrapper.clients;

import edu.java.scrapper.configs.RetryPolicyConfig;
import edu.java.scrapper.services.RetryService.RetryService;
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
    private final String bot = "bot";
    private final WebClient webClient;

    public BotClient(RetryPolicyConfig retryPolicyConfig) {
        this.webClient = WebClient
            .builder()
            .baseUrl(defaultUrl)
            .filter(RetryService.createFilter(retryPolicyConfig, bot))
            .build();
    }

    public BotClient(RetryPolicyConfig retryPolicyConfig, String baseUrl) {
        this.webClient = WebClient
            .builder()
            .baseUrl(baseUrl)
            .filter(RetryService.createFilter(retryPolicyConfig, bot))
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
