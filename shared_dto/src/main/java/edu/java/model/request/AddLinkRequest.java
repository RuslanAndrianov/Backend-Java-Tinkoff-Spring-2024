package edu.java.model.request;

import jakarta.validation.constraints.NotNull;
import java.net.URI;
import org.springframework.stereotype.Component;

@Component
public record AddLinkRequest(
    @NotNull URI link
) { }
