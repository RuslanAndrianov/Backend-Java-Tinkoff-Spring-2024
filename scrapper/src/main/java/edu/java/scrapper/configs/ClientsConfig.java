package edu.java.scrapper.configs;

import edu.java.scrapper.clients.BotClient;
import edu.java.scrapper.clients.GitHub.GitHubClient;
import edu.java.scrapper.clients.StackOverflow.StackOverflowClientImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientsConfig {

    @Bean
    public BotClient botClient(RetryPolicyConfig retryPolicyConfig) {
        return new BotClient(retryPolicyConfig);
    }

    @Bean
    public GitHubClient gitHubClient(RetryPolicyConfig retryPolicyConfig) {
        return new GitHubClient(retryPolicyConfig);
    }

    @Bean
    public StackOverflowClientImpl stackOverflowClient(RetryPolicyConfig retryPolicyConfig) {
        return new StackOverflowClientImpl(retryPolicyConfig);
    }
}
