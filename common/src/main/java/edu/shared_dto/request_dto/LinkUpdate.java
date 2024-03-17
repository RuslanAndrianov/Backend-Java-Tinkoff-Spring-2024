package edu.shared_dto.request_dto;

import jakarta.validation.constraints.NotEmpty;
import java.net.URI;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public record LinkUpdate(
    @NotNull Long id,
    @NotNull URI url,
    @NotEmpty String description,
    @NotEmpty List<Long> tgChatIds
) { }
