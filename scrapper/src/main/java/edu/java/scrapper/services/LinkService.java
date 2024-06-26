package edu.java.scrapper.services;

import edu.java.scrapper.domain.dto.Chat;
import edu.java.scrapper.domain.dto.Link;
import edu.java.scrapper.domain.repository.ChatsRepository;
import edu.java.scrapper.domain.repository.ChatsToLinksRepository;
import edu.java.scrapper.domain.repository.LinksRepository;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import static edu.utils.URLValidator.isValidGitHubURL;
import static edu.utils.URLValidator.isValidStackOverflowURL;

@Service
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings("MagicNumber")
public class LinkService {

    private final LinksRepository linksRepository;
    private final ChatsRepository chatsRepository;
    private final ChatsToLinksRepository chatsToLinksRepository;

    // Возвращает разные intы в зависимости от сценария:
    // 1 - чат существует, ошибок при добавлении не наблюдается
    // 0 - чат существует, пытаемся добавить ссылку 2-ой раз
    // -1 - чат не существует
    // -2 - иная проблема при добавлении ссылки
    public int addLinkToChatByUrl(long tgChatId, String url) {
        Chat chat = chatsRepository.getChatById(tgChatId);
        if (chat == null) {
            return -1;
        }

        List<Long> chatLinkIds = chatsToLinksRepository.getAllLinkIdsByChat(chat);
        for (long chatLinkId : chatLinkIds) {
            Link link = linksRepository.getLinkById(chatLinkId);
            if (link.getUrl().equals(url)) {
                return 0;
            }
        }

        Link link = linksRepository.getLinkByUrl(url);

        // Если ссылки еще нет в links, то добавляем ее в links с новым id
        if (link == null) {
            if (isValidGitHubURL(url) || isValidStackOverflowURL(url)) {
                List<Link> links = linksRepository.getAllLinks();
                long linkId = links.isEmpty() ? 1 : links.size() + 1;
                link = new Link();
                link.setLinkId(linkId);
                link.setUrl(url);
                link.setLastChecked(OffsetDateTime.now());
                link.setLastUpdated(OffsetDateTime.now());
                link.setZoneOffset(OffsetDateTime.now().getOffset().getTotalSeconds());
                linksRepository.addLink(link);
                chatsToLinksRepository.addLinkToChat(chat, link);
                return 1;
            }
        }
        return -2;
    }

    // Возвращает разные intы в зависимости от сценария:
    // 1 - чат существует, ссылка к чату привязана
    // 0 - чат существует, ссылка к чату не привязана
    // -1 - чат не существует
    // -2 - иная проблема при добавлении ссылки
    public int deleteLinkFromChatByUrl(long tgChatId, String url) {
        Chat chat = chatsRepository.getChatById(tgChatId);
        if (chat == null) {
            return -1;
        }

        List<Long> chatLinkIds = chatsToLinksRepository.getAllLinkIdsByChat(chat);
        boolean isLinkAddedToChat = false;
        for (long chatLinkId : chatLinkIds) {
            Link link = linksRepository.getLinkById(chatLinkId);
            if (link.getUrl().equals(url)) {
                isLinkAddedToChat = true;
                break;
            }
        }
        if (!isLinkAddedToChat) {
            return 0;
        }

        Link link = linksRepository.getLinkByUrl(url);

        boolean result = chatsToLinksRepository.deleteLinkFromChat(chat, link);

        return result ? 1 : -2;
    }

    public Link getLinkById(long linkId) {
        return linksRepository.getLinkById(linkId);
    }

    public Link getLinkByUrl(String url) {
        return linksRepository.getLinkByUrl(url);
    }

    public List<Long> getAllLinkIdsByChat(long tgChatId) {
        Chat chat = chatsRepository.getChatById(tgChatId);
        return chatsToLinksRepository.getAllLinkIdsByChat(chat);
    }

    public List<Link> getOldestCheckedLinks() {
        return linksRepository.getOldestCheckedLinks(
            "3 minutes");
    }

    public boolean setLastCheckedTimeToLink(Link link, OffsetDateTime time) {
        return linksRepository.setLastCheckedTimeToLink(link, time);
    }

    public boolean setLastUpdatedTimeToLink(Link link, OffsetDateTime time) {
        return linksRepository.setLastUpdatedTimeToLink(link, time);
    }
}
