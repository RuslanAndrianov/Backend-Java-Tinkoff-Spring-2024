package edu.java.services.jdbc;

import edu.java.domain.dto.Chat;
import edu.java.domain.dto.Link;
import edu.java.domain.repository.jdbc.JdbcChatsRepository;
import edu.java.domain.repository.jdbc.JdbcChatsToLinksRepository;
import edu.java.domain.repository.jdbc.JdbcLinksRepository;
import edu.java.services.LinkService;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import static edu.utils.URLValidator.isValidGitHubURL;
import static edu.utils.URLValidator.isValidStackOverflowURL;

@RequiredArgsConstructor
@Service
@Slf4j
@SuppressWarnings("MagicNumber")
public class JdbcLinkService implements LinkService {

    private final JdbcLinksRepository jdbcLinksRepository;
    private final JdbcChatsRepository jdbcChatsRepository;
    private final JdbcChatsToLinksRepository jdbcChatsToLinksRepository;

    // Возвращает разные intы в зависимости от сценария:
    // 1 - чат существует, ошибок при добавлении не наблюдается
    // 0 - чат существует, пытаемся добавить ссылку 2-ой раз
    // -1 - чат не существует
    // -2 - иная проблема при добавлении ссылки
    @Override
    public int addLinkToChatByUrl(long tgChatId, String url) {
        Chat chat = jdbcChatsRepository.getChatById(tgChatId);
        if (chat == null) {
            return -1;
        }

        List<Link> chatLinks = jdbcChatsToLinksRepository.getAllLinksByChat(chat);
        for (Link chatLink : chatLinks) {
            if (chatLink.url().equals(url)) {
                return 0;
            }
        }

        Link link = jdbcLinksRepository.getLinkByUrl(url);

        // Если ссылки еще нет в links, то добавляем ее в links с новым id
        if (link == null) {
            if (isValidGitHubURL(url) || isValidStackOverflowURL(url)) {
                List<Link> links = jdbcLinksRepository.getAllLinks();
                long linkId = links.isEmpty() ? 1 : links.size() + 1;
                link = new Link(
                    linkId,
                    url,
                    OffsetDateTime.now(),
                    OffsetDateTime.now(),
                    OffsetDateTime.now().getOffset().getTotalSeconds()
                );
                jdbcLinksRepository.addLink(link);
                jdbcChatsToLinksRepository.addLinkToChat(chat, link);
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
    @Override
    public int deleteLinkFromChatByUrl(long tgChatId, String url) {
        Chat chat = jdbcChatsRepository.getChatById(tgChatId);
        if (chat == null) {
            return -1;
        }

        List<Link> chatLinks = jdbcChatsToLinksRepository.getAllLinksByChat(chat);
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

        Link link = jdbcLinksRepository.getLinkByUrl(url);

        boolean result = jdbcChatsToLinksRepository.deleteLinkFromChat(chat, link);

        return result ? 1 : -2;
    }

    @Override
    public Link getLinkByUrl(String url) {
        return jdbcLinksRepository.getLinkByUrl(url);
    }

    @Override
    public List<Link> getAllLinksByChat(long tgChatId) {
        Chat chat = jdbcChatsRepository.getChatById(tgChatId);
        return jdbcChatsToLinksRepository.getAllLinksByChat(chat);
    }

    @Override
    public List<Link> getOldestCheckedLinks() {
        return jdbcLinksRepository.getOldestCheckedLinks("3 minutes");
    }

    @Override
    public boolean setLastCheckedTimeToLink(Link link, OffsetDateTime time) {
        return jdbcLinksRepository.setLastCheckedTimeToLink(link, time);
    }

    @Override
    public boolean setLastUpdatedTimeToLink(Link link, OffsetDateTime time) {
        return jdbcLinksRepository.setLastUpdatedTimeToLink(link, time);
    }
}
