package edu.shared_dto.request_dto;

import java.net.URI;
import org.jetbrains.annotations.NotNull;

public record AddLinkRequest(
    @NotNull URI link
) { }
