package edu.java.bot.configs;

import edu.java.bot.clients.ScrapperClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientsConfig {

    @Bean
    public ScrapperClient scrapperClient(RetryPolicyConfig retryPolicyConfig) {
        return new ScrapperClient(retryPolicyConfig);
    }
}
