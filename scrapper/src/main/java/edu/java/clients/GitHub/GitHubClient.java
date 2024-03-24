package edu.java.clients.GitHub;

public interface GitHubClient {

    GitHubResponse fetchRepository(String owner, String repo);
}
