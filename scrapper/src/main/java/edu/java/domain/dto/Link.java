package edu.java.domain.dto;

import java.time.OffsetDateTime;

public record Link(
    long linkId,
    String url,
    OffsetDateTime lastUpdated,
    OffsetDateTime lastChecked
) {
}
