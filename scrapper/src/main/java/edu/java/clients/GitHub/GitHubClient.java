package edu.java.clients.GitHub;

public interface GitHubClient {

    RepositoryResponse fetchRepository(String owner, String repo);
}
