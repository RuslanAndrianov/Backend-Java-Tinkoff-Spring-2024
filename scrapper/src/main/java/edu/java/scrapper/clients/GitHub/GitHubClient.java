package edu.java.scrapper.clients.GitHub;

public interface GitHubClient {

    GitHubResponse fetchRepository(String owner, String repo);
}
