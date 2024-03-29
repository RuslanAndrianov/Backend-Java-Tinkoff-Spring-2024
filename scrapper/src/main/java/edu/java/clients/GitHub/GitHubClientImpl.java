package edu.java.clients.GitHub;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;

public class GitHubClientImpl implements GitHubClient {

    @Value(value = "${api.github.defaultUrl}")
    private String defaultUrl;
    private final WebClient webClient;

    public GitHubClientImpl() {
        this.webClient = WebClient
            .builder()
            .baseUrl(defaultUrl)
            .build();
    }

    public GitHubClientImpl(String baseUrl) {
        this.webClient = WebClient
            .builder()
            .baseUrl(baseUrl)
            .build();
        this.defaultUrl = baseUrl;
    }

    @Override
    public GitHubResponse fetchRepository(String owner, String repo) {
        return this.webClient
            .get()
            .uri(defaultUrl + "/repos/{owner}/{repo}", owner, repo)
            .retrieve()
            .bodyToMono(GitHubResponse.class)
            .block();
    }
}
