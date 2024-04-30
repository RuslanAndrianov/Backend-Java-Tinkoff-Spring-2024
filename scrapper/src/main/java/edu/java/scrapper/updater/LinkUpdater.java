package edu.java.scrapper.updater;

import edu.java.scrapper.clients.BotClient;
import edu.java.scrapper.clients.GitHub.GitHubBranchResponse;
import edu.java.scrapper.clients.GitHub.GitHubClient;
import edu.java.scrapper.clients.GitHub.GitHubCommitResponse;
import edu.java.scrapper.clients.StackOverflow.StackOverflowClient;
import edu.java.scrapper.clients.StackOverflow.StackOverflowItemsResponse;
import edu.java.scrapper.clients.StackOverflow.StackOverflowResponse;
import edu.java.scrapper.domain.dto.Link;
import edu.java.scrapper.domain.repository.ChatsToLinksRepository;
import edu.java.scrapper.services.LinkService;
import edu.shared_dto.request_dto.LinkUpdateRequest;
import java.net.URI;
import java.net.URISyntaxException;
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
    private final ChatsToLinksRepository chatsToLinksRepository;
    private final BotClient botClient;
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
            default:
                log.error("Inappropriate link!");
        }
    }

    private void updateGitHubLink(@NotNull Link link, String owner, String repo) {
        OffsetDateTime lastUpdated = link.getLastUpdated();
        GitHubBranchResponse[] branchResponses = gitHubClient.getBranches(owner, repo);

        // Коммиты приходят в порядке от недавних к давним => обходим массив в обратном порядке
        for (int i = branchResponses.length - 1; i >= 0; i--) {
            String branchName = branchResponses[i].branchName;
            GitHubCommitResponse[] commitResponses = gitHubClient
                .getCommitsFromBranch(owner, repo, branchName);

            for (GitHubCommitResponse commitResponse : commitResponses) {
                OffsetDateTime updatedAt = commitResponse.commit.author.date;
                String message = commitResponse.commit.message;

                if (updatedAt.isAfter(lastUpdated)) {
                    linkService.setLastUpdatedTimeToLink(link, updatedAt);
                    try {
                        botClient.updateLink(new LinkUpdateRequest(
                            link.getLinkId(),
                            new URI(link.getUrl()),
                            "Новый коммит в репозитории: " + repo + "\n"
                                + "Ветка: " + branchName + "\n"
                                + "Коммит: " + message + "\n"
                                + "Владелец репозитория: " + owner,
                            chatsToLinksRepository.getAllChatIdsByLink(link)
                        ));
                    } catch (URISyntaxException e) {
                        log.error("Error updateGitHubLink");
                    }
                }
            }
        }
    }

    private void updateStackOverflowLink(@NotNull Link link, Long questionId) {
        StackOverflowItemsResponse response = stackOverflowClient.fetchQuestion(questionId);
        StackOverflowResponse properties = response.deserialize();
        OffsetDateTime lastActivityDate = properties.lastActivityDate();
        OffsetDateTime lastUpdated = link.getLastUpdated();

        if (lastActivityDate.isAfter(lastUpdated)) {
            linkService.setLastUpdatedTimeToLink(link, lastActivityDate);
            try {
                botClient.updateLink(new LinkUpdateRequest(
                    link.getLinkId(),
                    new URI(link.getUrl()),
                    "Question id: " + questionId,
                    chatsToLinksRepository.getAllChatIdsByLink(link)
                ));
            } catch (URISyntaxException e) {
                log.error("Error updateStackOverflowLink");
            }
        }
    }
}
