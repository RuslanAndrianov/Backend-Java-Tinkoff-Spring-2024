package edu.java.scrapper.clients.StackOverflow;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import lombok.Getter;

@Getter
public class StackOverflowItemsResponse {

    private Object[] items;
    private final long secondsToMilliseconds = 1000L;

    @JsonProperty("items")
    public void setItems(Object[] items) {
        this.items = items;
    }

    public StackOverflowResponse deserialize() {
        long questionId = 0;
        String title = "";
        OffsetDateTime lastActivityDate = null;

        Map<String, Object> list = (Map<String, Object>) items[0];

        for (Map.Entry<String, Object> entry : list.entrySet()) {
            //System.out.println(entry + " : " + entry.getKey().getClass() + " ||| " + entry.getValue().getClass());
            if (entry.getKey().equals("question_id")) {
                questionId = ((Integer) entry.getValue());
                continue;
            }
            if (entry.getKey().equals("title")) {
                title = (String) entry.getValue();
                continue;
            }
            if (entry.getKey().equals("last_activity_date")) {
                long milliseconds = secondsToMilliseconds * (Integer) entry.getValue();

                Instant instant = Instant.ofEpochMilli(milliseconds);
                lastActivityDate = instant.atOffset(ZoneOffset.UTC);
            }
        }
        return new StackOverflowResponse(questionId, title, lastActivityDate);
    }
}
