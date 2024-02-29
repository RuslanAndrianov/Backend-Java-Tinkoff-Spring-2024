package edu.java.client.GitHub;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record RepositoryResponse(
    @JsonProperty("id") Long id,
    @JsonProperty("full_name") String fullName,
    @JsonProperty("updated_at") OffsetDateTime updatedAt
) {

}
