package edu.java.scrapper.configs;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "resilience4j.retry", ignoreUnknownFields = false)
public record RetryPolicyConfig(
    Map<String, RetryPolicySettings> instances
) {
    public record RetryPolicySettings(
        @NotNull String type,
        int maxAttempts,
        Duration minDelay,
        Duration maxDelay,
        List<Integer> codes,
        int factor
    ) { }
}
