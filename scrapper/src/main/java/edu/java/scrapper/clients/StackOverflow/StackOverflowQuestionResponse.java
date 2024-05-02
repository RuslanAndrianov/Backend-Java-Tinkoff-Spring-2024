package edu.java.scrapper.clients.StackOverflow;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StackOverflowQuestionResponse {

    @JsonProperty("items") public Items[] items;

    public static class Items {

        @JsonProperty("answer_count") public int answerCount;
        @JsonProperty("question_id") public long questionId;
        @JsonProperty("title") public String title;
        @JsonProperty("last_activity_date") public int lastActivityDateSeconds;
        public OffsetDateTime lastActivityDate;
        private final int secondsToMilliseconds = 1000;

        public void createLastActivityDateFromSeconds() {

            long milliseconds = (long) secondsToMilliseconds * lastActivityDateSeconds;
            Instant instant = Instant.ofEpochMilli(milliseconds);
            lastActivityDate = instant.atOffset(ZoneOffset.UTC);
        }
    }
}
