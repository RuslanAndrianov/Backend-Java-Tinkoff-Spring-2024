package edu.java.scrapper.updater;

import edu.java.scrapper.clients.BotClient;
import edu.java.scrapper.clients.GitHub.GitHubBranchResponse;
import edu.java.scrapper.clients.GitHub.GitHubClient;
import edu.java.scrapper.clients.GitHub.GitHubCommitResponse;
import edu.java.scrapper.clients.StackOverflow.StackOverflowClient;
import edu.java.scrapper.clients.StackOverflow.StackOverflowQuestionResponse;
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
        StackOverflowQuestionResponse response = stackOverflowClient.getQuestion(questionId);

        int answerCount = response.items[0].answerCount;
        String title = response.items[0].title;

        response.items[0].createLastActivityDateFromSeconds();
        OffsetDateTime lastActivityDate1 = response.items[0].lastActivityDate;

        if (link.getAdditionalInfo() == null) {
            String initialAnswerCountInfo = "0";
            linkService.addAdditionalInfoToLink(link, initialAnswerCountInfo);
            link.setAdditionalInfo(initialAnswerCountInfo);
        }

        if (answerCount > Integer.parseInt(link.getAdditionalInfo())) {
            linkService.setLastUpdatedTimeToLink(link, lastActivityDate1);
            try {
                botClient.updateLink(new LinkUpdateRequest(
                    link.getLinkId(),
                    new URI(link.getUrl()),
                    "Новый ответ в вопросе " + questionId + "\n"
                    + "Вопрос: " + title,
                    chatsToLinksRepository.getAllChatIdsByLink(link)
                ));
            } catch (URISyntaxException e) {
                log.error("Error updateStackOverflowLink");
            }
        }
    }
}
