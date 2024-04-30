package edu.java.scrapper.clients.GitHub;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GitHubCommitResponse {

    @JsonProperty("commit") public Commit commit;

    public static class Commit {
        @JsonProperty("message") public String message;
        @JsonProperty("author") public Author author;

        public static class Author {
            @JsonProperty("date") public OffsetDateTime date;
        }
    }
}
