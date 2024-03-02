package edu.java.controller;

import edu.java.model.request.AddLinkRequest;
import edu.java.model.response.APIErrorResponse;
import edu.java.model.response.LinkResponse;
import edu.java.model.response.ListLinksResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ScrapperController {

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
        log.info("registerChat");
        return ResponseEntity.ok().build();
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
        log.info("deleteChat");
        return ResponseEntity.ok().build();
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
        log.info("getLinks");
        return ResponseEntity.ok().build();
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
        @RequestBody @Valid AddLinkRequest request) {

        log.info("addLink");
        return ResponseEntity.ok().build();
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
    public ResponseEntity<?> deleteLink(@RequestHeader("Tg-Chat-Id") Long chatId) {
        log.info("deleteLink");
        return ResponseEntity.ok().build();
    }
}
