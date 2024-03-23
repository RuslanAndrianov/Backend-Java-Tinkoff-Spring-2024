package edu.java.updater;

import edu.java.clients.GitHub.GitHubClient;
import edu.java.clients.GitHub.RepositoryResponse;
import edu.java.clients.StackOverflow.QuestionResponse;
import edu.java.clients.StackOverflow.StackOverflowClient;
import edu.java.domain.dto.Link;
import edu.java.services.LinkService;
import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings("MagicNumber")
public class LinkUpdater {

    private final LinkService linkService;
    private final GitHubClient gitHubClient;
    private final StackOverflowClient stackOverflowClient;

    public void updateLink(@NotNull String url) {
        Link link = linkService.getLinkByUrl(url);
        String[] partsOfUrl = url.split("/");
        String domain = partsOfUrl[2];

        switch (domain) {
            case "github.com":
                String owner = partsOfUrl[3];
                String repo = partsOfUrl[4];
                updateGitHubLink(link, owner, repo);
                break;
            case "stackoverflow.com":
                Long questionId = Long.valueOf(partsOfUrl[4]);
                updateStackOverflowLink(link, questionId);
                break;
            default: log.error("Inappropriate link!");
        }
    }

    private void updateGitHubLink(@NotNull Link link, String owner, String repo) {
        RepositoryResponse response = gitHubClient.fetchRepository(owner, repo);
        OffsetDateTime updatedAt = response.updatedAt();
        if (updatedAt.isAfter(link.lastUpdated())) {
            linkService.setLastUpdatedTimeToLink(link, updatedAt);
        }
    }

    private void updateStackOverflowLink(@NotNull Link link, Long questionId) {
        QuestionResponse response = stackOverflowClient.fetchQuestion(questionId);
        OffsetDateTime lastActivityDate = response.lastActivityDate();
        if (lastActivityDate.isAfter(link.lastUpdated())) {
            linkService.setLastUpdatedTimeToLink(link, lastActivityDate);
        }
    }
}
