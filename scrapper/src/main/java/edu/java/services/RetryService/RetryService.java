package edu.java.services.RetryService;

import edu.java.configs.RetryPolicyConfig;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import reactor.util.retry.RetryBackoffSpec;

@UtilityClass
public class RetryService {
    private static final Map<
        String, Function<RetryPolicyConfig.RetryPolicySettings, Retry>>
        RETRY_BUILDERS = new HashMap<>();

    static {
        RETRY_BUILDERS.put(
            "fixed",
            retrySettings -> RetryBackoffSpec
                .fixedDelay(retrySettings.maxAttempts(), retrySettings.minDelay())
                .filter(buildErrorFilter(retrySettings.codes()))
        );
        RETRY_BUILDERS.put(
            "exponential",
            retrySettings -> RetryBackoffSpec
                .backoff(retrySettings.maxAttempts(), retrySettings.minDelay())
                .maxBackoff(retrySettings.maxDelay())
                .filter(buildErrorFilter(retrySettings.codes()))
        );

        RETRY_BUILDERS.put(
            "linear",
            retryElement -> LinearRetryBackoffSpec
                .backoff(retryElement.maxAttempts(), retryElement.minDelay())
                .factor(retryElement.factor())
                .filter(buildErrorFilter(retryElement.codes()))
        );
    }

    public static ExchangeFilterFunction createFilter(RetryPolicyConfig config, String instance) {
        return (response, next) -> next.exchange(response)
            .flatMap(clientResponse -> {
                if (clientResponse.statusCode().isError()
                    && config.instances().get(instance).codes().contains(
                        clientResponse.statusCode().value())) {
                    return clientResponse.createError();
                } else {
                    return Mono.just(clientResponse);
                }
            })
            .retryWhen(createRetry(config, instance));
    }

    public static Retry createRetry(@NotNull RetryPolicyConfig config, String instance) {
        RetryPolicyConfig.RetryPolicySettings settings = config.instances().get(instance);
        return RETRY_BUILDERS.get(settings.type()).apply(settings);
    }

    private static Predicate<Throwable> buildErrorFilter(List<Integer> retryCodes) {
        return retrySignal -> {
            if (retrySignal instanceof WebClientResponseException e) {
                return retryCodes.contains(e.getStatusCode().value());
            }
            return true;
        };
    }
}
