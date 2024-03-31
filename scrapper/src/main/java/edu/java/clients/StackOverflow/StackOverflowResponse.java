package edu.java.clients.StackOverflow;

import java.time.OffsetDateTime;

public record StackOverflowResponse(
    long questionId,
    String title,
    OffsetDateTime lastActivityDate
) {
}
