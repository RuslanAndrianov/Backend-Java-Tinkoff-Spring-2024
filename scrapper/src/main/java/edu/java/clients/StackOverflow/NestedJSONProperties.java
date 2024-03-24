package edu.java.clients.StackOverflow;

import java.time.OffsetDateTime;

public record NestedJSONProperties(
    long questionId,
    String title,
    OffsetDateTime lastActivityDate
) {
}
