package edu.java.configs;

import edu.java.clients.BotClient;
import edu.java.clients.GitHub.GitHubClientImpl;
import edu.java.clients.StackOverflow.StackOverflowClientImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientsConfig {

    @Bean
    public BotClient botClient(RetryPolicyConfig retryPolicyConfig) {
        return new BotClient(retryPolicyConfig);
    }

    @Bean
    public GitHubClientImpl gitHubClient(RetryPolicyConfig retryPolicyConfig) {
        return new GitHubClientImpl(retryPolicyConfig);
    }

    @Bean
    public StackOverflowClientImpl stackOverflowClient(RetryPolicyConfig retryPolicyConfig) {
        return new StackOverflowClientImpl(retryPolicyConfig);
    }
}
