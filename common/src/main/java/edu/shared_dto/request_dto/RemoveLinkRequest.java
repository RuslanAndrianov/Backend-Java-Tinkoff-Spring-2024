package edu.shared_dto.request_dto;

import jakarta.validation.constraints.NotNull;
import java.net.URI;

public record RemoveLinkRequest(
    @NotNull URI link
) {
}
