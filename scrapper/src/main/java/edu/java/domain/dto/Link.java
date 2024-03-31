package edu.java.domain.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;

@Entity
@Table(name = "links")
public record Link(
    @Id
    long linkId,
    String url,
    OffsetDateTime lastUpdated,
    OffsetDateTime lastChecked,
    int zoneOffset
) {
}
