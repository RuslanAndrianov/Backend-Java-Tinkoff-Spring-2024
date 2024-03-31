package edu.java.updater;

import edu.java.clients.BotClient;
import edu.java.clients.GitHub.GitHubClient;
import edu.java.clients.GitHub.GitHubResponse;
import edu.java.clients.StackOverflow.StackOverflowResponse;
import edu.java.clients.StackOverflow.StackOverflowClient;
import edu.java.clients.StackOverflow.StackOverflowItemsResponse;
import edu.java.domain.dto.Link;
import edu.java.domain.repository.ChatsToLinksRepository;
import edu.java.domain.repository.LinksRepository;
import edu.shared_dto.request_dto.LinkUpdateRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings("MagicNumber")
public class LinkUpdater {

    private final LinksRepository linksRepository;
    private final ChatsToLinksRepository chatsToLinksRepository;
    private final BotClient botClient;
    private final GitHubClient gitHubClient;
    private final StackOverflowClient stackOverflowClient;

    public void updateLink(@NotNull String url) {
        Link link = linksRepository.getLinkByUrl(url);
        String[] partsOfUrl = url.split("/");
        String domain = partsOfUrl[2];

        // TODO : возможно стоит переписать с использованием валидаторов URLValidator

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
        GitHubResponse response = gitHubClient.fetchRepository(owner, repo);
        OffsetDateTime updatedAt = response.updatedAt();

        OffsetDateTime lastUpdated = getCorrectLastUpdated(link);

        if (updatedAt.isAfter(lastUpdated)) {
            linksRepository.setLastUpdatedTimeToLink(link, updatedAt);
            try {
                botClient.updateLink(new LinkUpdateRequest(
                    link.linkId(),
                    new URI(link.url()),
                    "Repository: " + repo + "\n" + "Owner: " + owner,
                    chatsToLinksRepository.getAllChatsByLink(link)
                ));
            } catch (URISyntaxException e) {
                log.error("Error updateGitHubLink");
            }
        }
    }

    private void updateStackOverflowLink(@NotNull Link link, Long questionId) {
        StackOverflowItemsResponse response = stackOverflowClient.fetchQuestion(questionId);
        StackOverflowResponse properties = response.deserialize();
        OffsetDateTime lastActivityDate = properties.lastActivityDate();

        OffsetDateTime lastUpdated = getCorrectLastUpdated(link);

        if (lastActivityDate.isAfter(lastUpdated)) {
            linksRepository.setLastUpdatedTimeToLink(link, lastActivityDate);
            try {
                botClient.updateLink(new LinkUpdateRequest(
                    link.linkId(),
                    new URI(link.url()),
                    "Question id: " + questionId,
                    chatsToLinksRepository.getAllChatsByLink(link)
                ));
            } catch (URISyntaxException e) {
                log.error("Error updateStackOverflowLink");
            }
        }
    }

    private OffsetDateTime getCorrectLastUpdated(@NotNull Link link) {
        return OffsetDateTime.ofInstant(
            Instant.ofEpochSecond(link.lastUpdated().toEpochSecond() - link.zoneOffset()),
            ZoneOffset.UTC);
    }
}
