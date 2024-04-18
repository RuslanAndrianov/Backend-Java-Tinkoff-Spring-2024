package edu.java.bot.clients;

import edu.java.bot.configs.RetryPolicyConfig;
import edu.java.bot.services.RetryService.RetryService;
import edu.shared_dto.request_dto.AddLinkRequest;
import edu.shared_dto.request_dto.RemoveLinkRequest;
import edu.shared_dto.response_dto.APIErrorResponse;
import edu.shared_dto.response_dto.LinkResponse;
import edu.shared_dto.response_dto.ListLinksResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@SuppressWarnings("MagicNumber")
public class ScrapperClient {

    @Value(value = "${api.scrapper.defaultUrl}")
    private String defaultUrl;

    private final WebClient webClient;
    private final String tgChat = "/tg-chat/";
    private final String links = "/links";
    private final String scrapper = "scrapper";
    private final String tgChatIdHeader = "Tg-Chat-Id";

    public ScrapperClient(RetryPolicyConfig retryPolicyConfig) {
        this.webClient = WebClient
            .builder()
            .baseUrl(defaultUrl)
            .filter(RetryService.createFilter(retryPolicyConfig, scrapper))
            .build();
    }

    public ScrapperClient(RetryPolicyConfig retryPolicyConfig, String baseUrl) {
        this.webClient = WebClient
            .builder()
            .baseUrl(baseUrl)
            .filter(RetryService.createFilter(retryPolicyConfig, scrapper))
            .build();
        this.defaultUrl = baseUrl;
    }

    public String registerChat(Long id) {
        return webClient
            .post()
            .uri(defaultUrl + tgChat + id)
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                response -> response
                    .bodyToMono(APIErrorResponse.class)
                    .flatMap(errorResponse -> Mono.error(new Exception()))
            )
            .bodyToMono(String.class)
            .block();
    }

    // TODO : добавить в будущем команду остановки бота <=> удаление чата из БД
    public String deleteChat(Long id) {
        return webClient
            .delete()
            .uri(defaultUrl + tgChat + id)
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                response -> response
                    .bodyToMono(APIErrorResponse.class)
                    .flatMap(errorResponse -> Mono.error(new Exception()))
            )
            .bodyToMono(String.class)
            .block();
    }

    public ListLinksResponse getLinks(Long id) {
        return webClient
            .get()
            .uri(defaultUrl + links)
            .header(tgChatIdHeader, String.valueOf(id))
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                response -> response
                    .bodyToMono(APIErrorResponse.class)
                    .flatMap(errorResponse -> Mono.error(new Exception()))
            )
            .bodyToMono(ListLinksResponse.class)
            .block();
    }

    public LinkResponse addLink(Long id, AddLinkRequest request) {
        LinkResponse linkResponse = null;
        boolean isDuplicateLink = false;
        try {
            linkResponse = webClient
                .post()
                .uri(defaultUrl + links)
                .header(tgChatIdHeader, String.valueOf(id))
                .body(BodyInserters.fromValue(request))
                .retrieve()
                .onStatus(
                    httpStatusCode ->
                        httpStatusCode.value() >= 400
                    && httpStatusCode.value() < 500
                    && httpStatusCode.value() != 406,
                    response -> response
                        .bodyToMono(APIErrorResponse.class)
                        .flatMap(errorResponse -> Mono.error(new Exception()))
                )
                .bodyToMono(LinkResponse.class)
                .block();

        } catch (WebClientResponseException e) {
            if (e.getStatusCode().value() == 406) {
                isDuplicateLink = true;
            }
        }

        if (isDuplicateLink) {
            return new LinkResponse(0L, null);
        }

        return linkResponse;
    }

    public LinkResponse deleteLink(Long id, RemoveLinkRequest request) {
        LinkResponse linkResponse = null;
        boolean isNotTrackingLink = false;
        try {
            linkResponse = webClient
                .method(HttpMethod.DELETE)
                .uri(defaultUrl + links)
                .header(tgChatIdHeader, String.valueOf(id))
                .body(BodyInserters.fromValue(request))
                .retrieve()
                .onStatus(
                    httpStatusCode ->
                        httpStatusCode.value() >= 400
                            && httpStatusCode.value() < 500
                            && httpStatusCode.value() != 404,
                    response -> response
                        .bodyToMono(APIErrorResponse.class)
                        .flatMap(errorResponse -> Mono.error(new Exception()))
                )
                .bodyToMono(LinkResponse.class)
                .block();
        } catch (WebClientResponseException e) {
            if (e.getStatusCode().value() == 404) {
                isNotTrackingLink = true;
            }
        }

        if (isNotTrackingLink) {
            return new LinkResponse(0L, null);
        }

        return linkResponse;
    }
}
