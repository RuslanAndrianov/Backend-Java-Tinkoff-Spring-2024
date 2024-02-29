package edu.java.configuration;

import edu.java.client.GitHub.GitHubClientImpl;
import edu.java.client.StackOverflow.StackOverflowClientImpl;
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
