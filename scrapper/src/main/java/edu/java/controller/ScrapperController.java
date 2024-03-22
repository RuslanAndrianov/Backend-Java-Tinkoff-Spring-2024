package edu.java.controller;

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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
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
            log.info("Chat with id " + id + " is registred!");
            return ResponseEntity.ok().build();
        }
        log.error("Request error! Chat with id " + id + " is already registred!");
        return ResponseEntity.status(400).build();
    }

    @DeleteMapping("/tg-chat/{id}")
    @Operation(summary = "Удалить чат")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Чат успешно удалён",
                     content = @Content(schema = @Schema(implementation = LinkResponse.class),
                                        mediaType = "application/json")),
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
            return ResponseEntity.ok().build();
        }
        log.error("Request error! Chat with id " + id + " is not registred!");
        return ResponseEntity.status(404).build();
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

            linkService.findAllLinksByChat(chatId);
            log.info("Get all links of chat " + chatId);
            return ResponseEntity.ok().build();
        }
        log.error("Request error of getting links of chat " + chatId + "!");
        return ResponseEntity.status(400).build();
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

        return switch (linkService.addLinkToChatByUrl(chatId, String.valueOf(request.link()))) {
            case 1 -> {
                log.info("Link " + request.link() + " is added to chat " + chatId);
                yield ResponseEntity.ok().build();
            }
            case 0 -> {
                log.error("Request error! Link " + request.link() + " is already added to chat " + chatId + "!");
                yield ResponseEntity.status(406).build();
            }
            case -1 -> {
                log.error("Request error! Chat " + chatId + " is not exist!");
                yield ResponseEntity.status(400).build();
            }
            default -> {
                log.error("Request error! Something went wrong!");
                yield ResponseEntity.status(400).build();
            }
        };
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

        return switch (linkService.deleteLinkFromChatByUrl(chatId, String.valueOf(request.link()))) {
            case 1 -> {
                log.info("Link " + request.link() + " is deleted from chat " + chatId);
                yield ResponseEntity.ok().build();
            }
            case 0 -> {
                log.error("Request error! Link " + request.link() + " is not added to chat " + chatId + "!");
                yield ResponseEntity.status(404).build();
            }
            case -1 -> {
                log.error("Request error! Chat " + chatId + " is not exist!");
                yield ResponseEntity.status(400).build();
            }
            default -> {
                log.error("Request error! Something went wrong!");
                yield ResponseEntity.status(400).build();
            }
        };
    }
}
