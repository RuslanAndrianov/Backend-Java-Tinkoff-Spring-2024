package edu.java.bot.controller;

import edu.java.bot.services.UpdateService;
import edu.shared_dto.request_dto.LinkUpdateRequest;
import edu.shared_dto.response_dto.APIErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Slf4j
public class BotController {

    private final UpdateService updateService;

    @PostMapping("/updates")
    @Operation(summary = "Отправить обновление")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Обновление обработано"),
        @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса",
                     content = @Content(schema = @Schema(implementation = APIErrorResponse.class),
                                        mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "Ссылка не существует",
                     content = @Content(schema = @Schema(implementation = APIErrorResponse.class),
                                        mediaType = "application/json"))
    })
    public ResponseEntity<?> updateLink(@RequestBody LinkUpdateRequest request) {
        log.info("Запрос: " + request);
        updateService.sendUpdateToAllChats(request);
        return ResponseEntity.ok().build();
    }
}
