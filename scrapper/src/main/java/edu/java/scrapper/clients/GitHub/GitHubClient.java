package edu.java.scrapper.clients.GitHub;

public interface GitHubClient {

    GitHubResponse fetchRepository(String owner, String repo);

    GitHubCommitResponse[] getCommitsFromBranch(String owner, String repo, String branch);

    GitHubBranchResponse[] getBranches(String owner, String repo);
}
