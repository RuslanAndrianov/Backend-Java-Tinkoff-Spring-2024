package edu.java.configs;

import edu.java.clients.GitHub.GitHubClientImpl;
import edu.java.clients.StackOverflow.StackOverflowClientImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfig {

    @Bean
    public GitHubClientImpl gitHubClient() {
        return new GitHubClientImpl();
    }

    @Bean
    public StackOverflowClientImpl stackOverflowClient() {
        return new StackOverflowClientImpl();
    }
}
