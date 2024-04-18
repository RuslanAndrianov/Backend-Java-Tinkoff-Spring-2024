package edu.java.controller;

import edu.java.domain.dto.Link;
import edu.java.services.ChatService;
import edu.java.services.LinkService;
import edu.shared_dto.request_dto.AddLinkRequest;
import edu.shared_dto.request_dto.RemoveLinkRequest;
import edu.shared_dto.response_dto.APIErrorResponse;
import edu.shared_dto.response_dto.LinkResponse;
import edu.shared_dto.response_dto.ListLinksResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings({"MagicNumber", "MultipleStringLiterals"})
public class ScrapperController {

    // TODO : добавить удаление ссылки из links, если 0 чатов отслеживают ссылку

    private final ChatService chatService;
    private final LinkService linkService;

    @PostMapping("/tg-chat/{id}")
    @Operation(summary = "Зарегистрировать чат")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Чат зарегистрирован",
                     content = @Content(schema = @Schema(implementation = LinkResponse.class),
                                        mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса",
                     content = @Content(schema = @Schema(implementation = APIErrorResponse.class),
                                        mediaType = "application/json"))
    })
    public ResponseEntity<?> registerChat(@PathVariable("id") Long id) {
        if (chatService.addChat(id)) {
            log.info("Chat with id " + id + " is registered!");
            return new ResponseEntity<>(id, HttpStatusCode.valueOf(200));
        }
        log.error("Error! Chat with id " + id + " is already registered!");
        return new ResponseEntity<>(HttpStatusCode.valueOf(400));
    }

    @DeleteMapping("/tg-chat/{id}")
    @Operation(summary = "Удалить чат")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Чат успешно удалён",
                     content = @Content(schema = @Schema(implementation = LinkResponse.class),
                                        mediaType = "application/json")),

        // TODO : Пока что не придумано, когда возвращать код 400

        @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса",
                     content = @Content(schema = @Schema(implementation = APIErrorResponse.class),
                                        mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "Чат не существует",
                     content = @Content(schema = @Schema(implementation = APIErrorResponse.class),
                                        mediaType = "application/json"))
    })
    public ResponseEntity<?> deleteChat(@PathVariable("id") Long id) {
        if (chatService.deleteChat(id)) {
            log.info("Chat with id " + id + " is deleted");
            return new ResponseEntity<>(id, HttpStatusCode.valueOf(200));
        }
        log.error("Error! Chat with id " + id + " is not registered!");
        return new ResponseEntity<>(HttpStatusCode.valueOf(404));
    }

    @GetMapping("/links")
    @Operation(summary = "Получить все отслеживаемые ссылки")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ссылки успешно получены",
                     content = @Content(schema = @Schema(implementation = ListLinksResponse.class),
                                        mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса",
                     content = @Content(schema = @Schema(implementation = APIErrorResponse.class),
                                        mediaType = "application/json"))
    })
    public ResponseEntity<?> getLinks(@RequestHeader("Tg-Chat-Id") Long chatId) {
        if (chatService.findChatById(chatId) != null) {
            List<Long> chatLinkIds = linkService.getAllLinkIdsByChat(chatId);
            List<LinkResponse> linkResponses = new ArrayList<>();

            try {
                for (long chatLinkId : chatLinkIds) {
                    Link link = linkService.getLinkById(chatLinkId);
                    linkResponses.add(new LinkResponse(link.getLinkId(), new URI(link.getUrl())));
                }
                log.info("Get all links of chat " + chatId);
                return new ResponseEntity<>(
                    new ListLinksResponse(linkResponses, linkResponses.size()),
                    HttpStatusCode.valueOf(200)
                );
            } catch (URISyntaxException ignored) {
            }
        }
        log.error("Error of getting links of chat " + chatId + "!");
        return new ResponseEntity<>(HttpStatusCode.valueOf(400));
    }

    @PostMapping("/links")
    @Operation(summary = "Добавить отслеживание ссылки")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ссылка успешно добавлена",
                     content = @Content(schema = @Schema(implementation = LinkResponse.class),
                                        mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса",
                     content = @Content(schema = @Schema(implementation = APIErrorResponse.class),
                                        mediaType = "application/json")),
        @ApiResponse(responseCode = "406", description = "Ссылка уже отслеживается",
                     content = @Content(schema = @Schema(implementation = APIErrorResponse.class),
                                        mediaType = "application/json"))
    })
    public ResponseEntity<?> addLink(
        @RequestHeader("Tg-Chat-Id") Long chatId,
        @RequestBody @Valid @NotNull AddLinkRequest request
    ) {
        String linkUrl = String.valueOf(request.link());
        switch (linkService.addLinkToChatByUrl(chatId, linkUrl)) {
            case 1:
                long linkId = linkService.getLinkByUrl(linkUrl).getLinkId();
                log.info("Link " + linkUrl + " is added to chat " + chatId);
                return new ResponseEntity<>(
                    new LinkResponse(linkId, request.link()),
                    HttpStatusCode.valueOf(200));
            case 0:
                log.error("Error! Link " + linkUrl + " is already added to chat " + chatId + "!");
                return new ResponseEntity<>(HttpStatusCode.valueOf(406));
            case -1:
                log.error("Error! Chat " + chatId + " is not exist!");
                break;
            default:
                log.error("Error! Something went wrong!");
        }
        return new ResponseEntity<>(HttpStatusCode.valueOf(400));
    }

    @DeleteMapping("/links")
    @Operation(summary = "Убрать отслеживание ссылки")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ссылка успешно убрана",
                     content = @Content(schema = @Schema(implementation = LinkResponse.class),
                                        mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса",
                     content = @Content(schema = @Schema(implementation = APIErrorResponse.class),
                                        mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "Ссылка не найдена",
                     content = @Content(schema = @Schema(implementation = APIErrorResponse.class),
                                        mediaType = "application/json"))
    })
    public ResponseEntity<?> deleteLink(
        @RequestHeader("Tg-Chat-Id") Long chatId,
        @RequestBody @Valid @NotNull RemoveLinkRequest request
    ) {
        String linkUrl = String.valueOf(request.link());
        switch (linkService.deleteLinkFromChatByUrl(chatId, linkUrl)) {
            case 1:
                log.info("Link " + linkUrl + " is deleted from chat " + chatId);
                long linkId = linkService.getLinkByUrl(linkUrl).getLinkId();
                return new ResponseEntity<>(
                    new LinkResponse(linkId, request.link()),
                    HttpStatusCode.valueOf(200));
            case 0:
                log.error("Error! Link " + linkUrl + " is not added to chat " + chatId + "!");
                return new ResponseEntity<>(HttpStatusCode.valueOf(404));
            case -1:
                log.error("Error! Chat " + chatId + " is not exist!");
                break;
            default:
                log.error("Error! Something went wrong!");
        }
        return new ResponseEntity<>(HttpStatusCode.valueOf(400));
    }
}
