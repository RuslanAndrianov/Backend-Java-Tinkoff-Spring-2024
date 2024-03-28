package edu.java.services.jdbc;

import edu.java.domain.dto.Chat;
import edu.java.domain.dto.Link;
import edu.java.domain.repository.ChatsRepository;
import edu.java.domain.repository.ChatsToLinksRepository;
import edu.java.domain.repository.LinksRepository;
import edu.java.services.LinkService;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
@SuppressWarnings("MagicNumber")
public class JdbcLinkService implements LinkService {

    private final LinksRepository linksRepository;
    private final ChatsRepository chatsRepository;
    private final ChatsToLinksRepository chatsToLinksRepository;

    // Возвращает разные intы в зависимости от сценария:
    // 1 - чат существует, ошибок при добавлении не наблюдается
    // 0 - чат существует, пытаемся добавить ссылку 2-ой раз
    // -1 - чат не существует
    // -2 - иная проблема при добавлении ссылки
    @Override
    public int addLinkToChatByUrl(long tgChatId, String url) {
        Chat chat = chatsRepository.getChatById(tgChatId);
        if (chat == null) {
            return -1;
        }

        List<Link> chatLinks = chatsToLinksRepository.getAllLinksByChat(chat);
        for (Link chatLink : chatLinks) {
            if (chatLink.url().equals(url)) {
                return 0;
            }
        }

        Link link = linksRepository.getLinkByUrl(url);

        // Если ссылки еще нет в links, то добавляем ее в links с новым id
        if (link == null) {
            List<Link> links = linksRepository.getAllLinks();
            long linkId = links.isEmpty() ? 1 : links.getLast().linkId() + 1;
            link = new Link(
                linkId,
                url,
                OffsetDateTime.now(),
                OffsetDateTime.now(),
                OffsetDateTime.now().getOffset().getTotalSeconds()
            );
            linksRepository.addLink(link);
        }

        boolean result = chatsToLinksRepository.addLinkToChat(chat, link);

        return result ? 1 : -2;
    }

    // Возвращает разные intы в зависимости от сценария:
    // 1 - чат существует, ссылка к чату привязана
    // 0 - чат существует, ссылка к чату не привязана
    // -1 - чат не существует
    // -2 - иная проблема при добавлении ссылки
    @Override
    public int deleteLinkFromChatByUrl(long tgChatId, String url) {
        Chat chat = chatsRepository.getChatById(tgChatId);
        if (chat == null) {
            return -1;
        }

        List<Link> chatLinks = chatsToLinksRepository.getAllLinksByChat(chat);
        boolean isLinkAddedToChat = false;
        for (Link chatLink : chatLinks) {
            if (chatLink.url().equals(url)) {
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

    @Override
    public Link getLinkByUrl(String url) {
        return linksRepository.getLinkByUrl(url);
    }

    @Override
    public List<Link> getAllLinksByChat(long tgChatId) {
        Chat chat = chatsRepository.getChatById(tgChatId);
        return chatsToLinksRepository.getAllLinksByChat(chat);
    }

    @Override
    public List<Link> getOldestCheckedLinks() {
        return linksRepository.getOldestCheckedLinks("3 minutes");
    }

    @Override
    public boolean setLastCheckedTimeToLink(Link link, OffsetDateTime time) {
        return linksRepository.setLastCheckedTimeToLink(link, time);
    }

    @Override
    public boolean setLastUpdatedTimeToLink(Link link, OffsetDateTime time) {
        return linksRepository.setLastUpdatedTimeToLink(link, time);
    }
}
