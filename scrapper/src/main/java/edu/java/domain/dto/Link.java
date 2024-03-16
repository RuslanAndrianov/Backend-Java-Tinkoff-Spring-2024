package edu.java.domain.dto;

import java.net.URL;
import java.time.OffsetDateTime;

public record Link(
    long linkId,
    URL url,
    OffsetDateTime lastUpdated
) {
}
