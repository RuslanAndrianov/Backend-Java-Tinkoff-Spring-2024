package edu.java.scrapper.clients.GitHub;

import edu.java.scrapper.configs.RetryPolicyConfig;
import edu.java.scrapper.services.RetryService.RetryService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;

public class GitHubClient {

    @Value(value = "${api.github.defaultUrl}")
    private String defaultUrl;
    private final String github = "github";
    private final WebClient webClient;

    public GitHubClient(RetryPolicyConfig retryPolicyConfig) {
        this.webClient = WebClient
            .builder()
            .baseUrl(defaultUrl)
            .filter(RetryService.createFilter(retryPolicyConfig, github))
            .build();
    }

    public GitHubClient(RetryPolicyConfig retryPolicyConfig, String baseUrl) {
        this.webClient = WebClient
            .builder()
            .baseUrl(baseUrl)
            .filter(RetryService.createFilter(retryPolicyConfig, github))
            .build();
        this.defaultUrl = baseUrl;
    }

    public GitHubResponse fetchRepository(String owner, String repo) {
        return this.webClient
            .get()
            .uri(defaultUrl + "/repos/{owner}/{repo}", owner, repo)
            .retrieve()
            .bodyToMono(GitHubResponse.class)
            .block();
    }

    public GitHubBranchResponse[] getBranches(String owner, String repo) {
        return this.webClient
            .get()
            .uri(defaultUrl + "/repos/{owner}/{repo}/branches", owner, repo)
            .retrieve()
            .bodyToMono(GitHubBranchResponse[].class)
            .block();
    }

    public GitHubCommitResponse[] getCommitsFromBranch(String owner, String repo, String branch) {
        return this.webClient
            .get()
            .uri(defaultUrl + "/repos/{owner}/{repo}/commits?sha={branch}",
                owner, repo, branch)
            .retrieve()
            .bodyToMono(GitHubCommitResponse[].class)
            .block();
    }
}
