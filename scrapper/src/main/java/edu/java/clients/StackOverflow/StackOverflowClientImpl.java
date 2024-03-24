package edu.java.clients.StackOverflow;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
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
        this.defaultUrl = baseUrl;
    }

    @Override
    public StackOverflowResponse fetchQuestion(Long questionId) {
        return this.webClient
            .get()
            .uri(defaultUrl
                + "/questions/{questionId}?order=desc&sort=activity&site=stackoverflow", questionId)
            .retrieve()
            .bodyToMono(StackOverflowResponse.class)
            .block();
    }
}
