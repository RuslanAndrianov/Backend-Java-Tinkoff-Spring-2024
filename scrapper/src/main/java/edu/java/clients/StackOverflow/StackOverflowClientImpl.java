package edu.java.clients.StackOverflow;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;

public class StackOverflowClientImpl implements StackOverflowClient {

    @Value(value = "${api.stackoverflow.defaultUrl}")
    private String defaultUrl;
    private final WebClient webClient;

    public StackOverflowClientImpl() {
        this.webClient = WebClient
            .builder()
            .baseUrl(defaultUrl)
            .build();
    }

    public StackOverflowClientImpl(String baseUrl) {
        this.webClient = WebClient
            .builder()
            .baseUrl(baseUrl)
            .build();
    }

    @Override
    public QuestionResponse fetchQuestion(Long questionId) {
        return this.webClient
            .get()
            .uri(uriBuilder -> uriBuilder
                .path("/questions/" + questionId)
                .queryParam("pagesize", 1)
                .queryParam("order", "desc")
                .queryParam("sort", "activity")
                .queryParam("site", "stackoverflow")
                .build())
            .retrieve()
            .bodyToMono(QuestionResponse.class)
            .block();
    }
}
