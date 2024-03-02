package edu.java.model.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public record LinkUpdate(
    @NotNull Long id,
    @NotNull URI url,
    @NotEmpty String description,
    @NotEmpty List<Long> tgChatIds
) { }
