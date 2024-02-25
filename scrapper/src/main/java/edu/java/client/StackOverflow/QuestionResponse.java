package edu.java.client.StackOverflow;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record QuestionResponse(
    @JsonProperty("question_id") String questionId,
    @JsonProperty("title") String title,
    @JsonProperty("last_activity_date") OffsetDateTime lastActivityDate
) {

}
