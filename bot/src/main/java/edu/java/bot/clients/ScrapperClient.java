package edu.java.bot.clients;

import edu.shared_dto.request_dto.AddLinkRequest;
import edu.shared_dto.request_dto.RemoveLinkRequest;
import edu.shared_dto.response_dto.APIErrorResponse;
import edu.shared_dto.response_dto.LinkResponse;
import edu.shared_dto.response_dto.ListLinksResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@SuppressWarnings("MemberName")
public class ScrapperClient {

    @Value(value = "${api.scrapper.defaultUrl}")
    private String defaultUrl;

    private final WebClient webClient;
    private final String TG_CHAT = "/tg-chat/";
    private final String LINKS = "/links";
    private final String TG_CHAT_ID_HEADER = "Tg-Chat-Id";


    public ScrapperClient() {
        this.webClient = WebClient
            .builder()
            .baseUrl(defaultUrl)
            .build();
    }

    public ScrapperClient(String baseUrl) {
        this.webClient = WebClient
            .builder()
            .baseUrl(baseUrl)
            .build();
        this.defaultUrl = baseUrl;
    }

    public String registerChat(Long id) {
        return webClient
            .post()
            .uri(defaultUrl + TG_CHAT + id)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError,
                response -> response
                    .bodyToMono(APIErrorResponse.class)
                    .flatMap(errorResponse -> Mono.error(new Exception())))
            .bodyToMono(String.class)
            .block();
    }

    // TODO : добавить в будущем команду остановки бота <=> удаление чата из БД
    public String deleteChat(Long id) {
        return webClient
            .delete()
            .uri(defaultUrl + TG_CHAT + id)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError,
                response -> response
                    .bodyToMono(APIErrorResponse.class)
                    .flatMap(errorResponse -> Mono.error(new Exception())))
            .bodyToMono(String.class)
            .block();
    }

    public ListLinksResponse getLinks(Long id) {
        return webClient
            .get()
            .uri(defaultUrl + LINKS)
            .header(TG_CHAT_ID_HEADER, String.valueOf(id))
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError,
                response -> response
                    .bodyToMono(APIErrorResponse.class)
                    .flatMap(errorResponse -> Mono.error(new Exception())))
            .bodyToMono(ListLinksResponse.class)
            .block();
    }

    public LinkResponse addLink(Long id, AddLinkRequest request) {
        return webClient
            .post()
            .uri(defaultUrl + LINKS)
            .header(TG_CHAT_ID_HEADER, String.valueOf(id))
            .body(BodyInserters.fromValue(request))
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError,
                response -> response
                    .bodyToMono(APIErrorResponse.class)
                    .flatMap(errorResponse -> Mono.error(new Exception())))
            .bodyToMono(LinkResponse.class)
            .block();
    }

    public LinkResponse deleteLink(Long id, RemoveLinkRequest request) {
        return webClient
            .method(HttpMethod.DELETE)
            .uri(defaultUrl + LINKS)
            .header(TG_CHAT_ID_HEADER, String.valueOf(id))
            .body(BodyInserters.fromValue(request))
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError,
                response -> response
                    .bodyToMono(APIErrorResponse.class)
                    .flatMap(errorResponse -> Mono.error(new Exception())))
            .bodyToMono(LinkResponse.class)
            .block();
    }
}
