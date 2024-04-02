package edu.java.domain.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "links")
public class Link {
    @Id
    long linkId;
    String url;
    OffsetDateTime lastUpdated;
    OffsetDateTime lastChecked;
    int zoneOffset;
}
