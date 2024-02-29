package edu.java.client.GitHub;

public interface GitHubClient {

    RepositoryResponse fetchRepository(String owner, String repo);
}
