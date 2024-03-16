package edu.shared_dto.request_dto;

import jakarta.validation.constraints.NotEmpty;
import org.jetbrains.annotations.NotNull;
import java.net.URI;
import java.util.List;

public record LinkUpdate(
    @NotNull Long id,
    @NotNull URI url,
    @NotEmpty String description,
    @NotEmpty List<Long> tgChatIds
) { }
