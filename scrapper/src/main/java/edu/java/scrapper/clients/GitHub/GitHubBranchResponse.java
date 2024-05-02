package edu.java.scrapper.clients.GitHub;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GitHubBranchResponse {

    @JsonProperty("name") public String branchName;
}
