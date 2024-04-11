package edu.java.clients.StackOverflow;

import edu.java.configs.RetryPolicyConfig;
import edu.java.services.RetryService.RetryService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;

public class StackOverflowClientImpl implements StackOverflowClient {

    @Value(value = "${api.stackoverflow.defaultUrl}")
    private String defaultUrl;
    private final String stackoverflow = "stackoverflow";
    private final WebClient webClient;

    public StackOverflowClientImpl(RetryPolicyConfig retryPolicyConfig) {
        this.webClient = WebClient
            .builder()
            .baseUrl(defaultUrl)
            .filter(RetryService.createFilter(retryPolicyConfig, stackoverflow))
            .build();
    }

    public StackOverflowClientImpl(RetryPolicyConfig retryPolicyConfig, String baseUrl) {
        this.webClient = WebClient
            .builder()
            .baseUrl(baseUrl)
            .filter(RetryService.createFilter(retryPolicyConfig, stackoverflow))
            .build();
        this.defaultUrl = baseUrl;
    }

    @Override
    public StackOverflowItemsResponse fetchQuestion(Long questionId) {
        return this.webClient
            .get()
            .uri(defaultUrl
                + "/questions/{questionId}?order=desc&sort=activity&site=stackoverflow", questionId)
            .retrieve()
            .bodyToMono(StackOverflowItemsResponse.class)
            .block();
    }
}
