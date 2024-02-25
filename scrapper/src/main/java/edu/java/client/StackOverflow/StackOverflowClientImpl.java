package edu.java.client.StackOverflow;

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
    public QuestionResponse fetchQuestion(long questionId, String order, String sort) {
        return this.webClient
            .get()
            .uri("questions/${questionId}?order=${order}&sort=${sort}&site=stackoverflow", questionId, order, sort)
            .retrieve()
            .bodyToMono(QuestionResponse.class)
            .block();
    }
}
