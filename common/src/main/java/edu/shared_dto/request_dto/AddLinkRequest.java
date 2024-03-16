package edu.shared_dto.request_dto;

import org.jetbrains.annotations.NotNull;
import java.net.URI;

public record AddLinkRequest(
    @NotNull URI link
) { }
