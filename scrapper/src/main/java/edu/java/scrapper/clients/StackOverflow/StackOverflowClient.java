package edu.java.scrapper.clients.StackOverflow;

import edu.java.scrapper.configs.RetryPolicyConfig;
import edu.java.scrapper.services.RetryService.RetryService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;

@SuppressWarnings("MultipleStringLiterals")
public class StackOverflowClient {

    @Value(value = "${api.stackoverflow.defaultUrl}")
    private String defaultUrl;
    private final String stackoverflow = "stackoverflow";
    private final WebClient webClient;

    public StackOverflowClient(RetryPolicyConfig retryPolicyConfig) {
        this.webClient = WebClient
            .builder()
            .baseUrl(defaultUrl)
            .filter(RetryService.createFilter(retryPolicyConfig, stackoverflow))
            .build();
    }

    public StackOverflowClient(RetryPolicyConfig retryPolicyConfig, String baseUrl) {
        this.webClient = WebClient
            .builder()
            .baseUrl(baseUrl)
            .filter(RetryService.createFilter(retryPolicyConfig, stackoverflow))
            .build();
        this.defaultUrl = baseUrl;
    }

    public StackOverflowItemsResponse fetchQuestion(Long questionId) {
        return this.webClient
            .get()
            .uri(defaultUrl
                + "/questions/{questionId}?order=desc&sort=activity&site=stackoverflow", questionId)
            .retrieve()
            .bodyToMono(StackOverflowItemsResponse.class)
            .block();
    }

    public StackOverflowQuestionResponse getQuestion(Long questionId) {
        return this.webClient
            .get()
            .uri(defaultUrl
                + "/questions/{questionId}?order=desc&sort=activity&site=stackoverflow", questionId)
            .retrieve()
            .bodyToMono(StackOverflowQuestionResponse.class)
            .block();
    }
}
