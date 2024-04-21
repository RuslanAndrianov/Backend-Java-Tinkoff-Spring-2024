package edu.java.scrapper.domain.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "links")
public class Link {
    @Id
    @Column(name = "link_id")
    long linkId;

    @Column(name = "url")
    String url;

    @Column(name = "last_updated")
    OffsetDateTime lastUpdated;

    @Column(name = "last_checked")
    OffsetDateTime lastChecked;

    @Column(name = "zone_offset")
    int zoneOffset;
}
