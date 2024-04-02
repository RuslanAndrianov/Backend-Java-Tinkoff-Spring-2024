package edu.java.services.jpa;

import edu.java.domain.dto.Chat;
import edu.java.domain.dto.Link;
import edu.java.domain.repository.jpa.chatsRepository.JpaChatsRepository;
import edu.java.domain.repository.jpa.chatsToLinksRepository.JpaChatsToLinksRepository;
import edu.java.domain.repository.jpa.linksRepository.JpaLinksRepository;
import edu.java.services.LinkService;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import static edu.utils.URLValidator.isValidGitHubURL;
import static edu.utils.URLValidator.isValidStackOverflowURL;

@RequiredArgsConstructor
@Service
@SuppressWarnings("MagicNumber")
public class JpaLinkService implements LinkService {

    private final JpaLinksRepository jpaLinksRepository;
    private final JpaChatsRepository jpaChatsRepository;
    private final JpaChatsToLinksRepository jpaChatsToLinksRepository;

    @Override
    public int addLinkToChatByUrl(long tgChatId, String url) {
        Chat chat = jpaChatsRepository.getChatById(tgChatId);
        if (chat == null) {
            return -1;
        }

        List<Link> chatLinks = jpaChatsToLinksRepository.getAllLinksByChat(chat);
        for (Link chatLink : chatLinks) {
            if (chatLink.getUrl().equals(url)) {
                return 0;
            }
        }

        Link link = jpaLinksRepository.getLinkByUrl(url);

        // Если ссылки еще нет в links, то добавляем ее в links с новым id
        if (link == null) {
            if (isValidGitHubURL(url) || isValidStackOverflowURL(url)) {
                List<Link> links = jpaLinksRepository.getAllLinks();
                long linkId = links.isEmpty() ? 1 : links.size() + 1;
                link = new Link();
                link.setLinkId(linkId);
                link.setUrl(url);
                link.setLastChecked(OffsetDateTime.now());
                link.setLastUpdated(OffsetDateTime.now());
                link.setZoneOffset(OffsetDateTime.now().getOffset().getTotalSeconds());
                jpaLinksRepository.addLink(link);
                jpaChatsToLinksRepository.addLinkToChat(chat, link);
                return 1;
            }
        }
        return -2;
    }

    @Override
    public int deleteLinkFromChatByUrl(long tgChatId, String url) {
        Chat chat = jpaChatsRepository.getChatById(tgChatId);
        if (chat == null) {
            return -1;
        }

        List<Link> chatLinks = jpaChatsToLinksRepository.getAllLinksByChat(chat);
        boolean isLinkAddedToChat = false;
        for (Link chatLink : chatLinks) {
            if (chatLink.getUrl().equals(url)) {
                isLinkAddedToChat = true;
                break;
            }
        }
        if (!isLinkAddedToChat) {
            return 0;
        }

        Link link = jpaLinksRepository.getLinkByUrl(url);

        boolean result = jpaChatsToLinksRepository.deleteLinkFromChat(chat, link);

        return result ? 1 : -2;
    }

    @Override
    public Link getLinkByUrl(String url) {
        return jpaLinksRepository.getLinkByUrl(url);
    }

    @Override
    public List<Link> getAllLinksByChat(long tgChatId) {
        Chat chat = jpaChatsRepository.getChatById(tgChatId);
        return jpaChatsToLinksRepository.getAllLinksByChat(chat);
    }

    @Override
    public List<Link> getOldestCheckedLinks() {
        return jpaLinksRepository.getOldestCheckedLinks("3 minutes");
    }

    @Override
    public boolean setLastCheckedTimeToLink(Link link, OffsetDateTime time) {
        return jpaLinksRepository.setLastCheckedTimeToLink(link, time);
    }

    @Override
    public boolean setLastUpdatedTimeToLink(Link link, OffsetDateTime time) {
        return jpaLinksRepository.setLastUpdatedTimeToLink(link, time);
    }
}
