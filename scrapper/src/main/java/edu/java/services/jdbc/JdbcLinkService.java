package edu.java.services.jdbc;

import edu.java.domain.dto.Chat;
import edu.java.domain.dto.Link;
import edu.java.domain.repository.ChatsRepository;
import edu.java.domain.repository.ChatsToLinksRepository;
import edu.java.domain.repository.LinksRepository;
import edu.java.services.LinkService;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class JdbcLinkService implements LinkService {

    private final LinksRepository linksRepository;
    private final ChatsRepository chatsRepository;
    private final ChatsToLinksRepository chatsToLinksRepository;

    @Override
    public boolean addLinkToChatByUrl(long tgChatId, String url) {
        Chat chat = chatsRepository.getChatById(tgChatId);
        Link link = linksRepository.getLinkByURL(url);
        boolean result1 = false;
        boolean result2;

        if (link == null) {
            List<Link> links = linksRepository.getAllLinks();
            long linkId = links.isEmpty() ? 1 : links.getLast().linkId() + 1;
            link = new Link(linkId, url, OffsetDateTime.now());
            result1 = linksRepository.addLink(link);
        }

        result2 = chatsToLinksRepository.addLinkToChat(chat, link);

        return result1 || result2;
    }

    @Override
    public boolean deleteLinkFromChatByUrl(long tgChatId, String url) {
        Chat chat = chatsRepository.getChatById(tgChatId);
        Link link = linksRepository.getLinkByURL(url);
        return chatsToLinksRepository.deleteLinkFromChat(chat, link);
    }

    @Override
    public Link findLinkByUrl(String url) {
        return linksRepository.getLinkByURL(url);
    }

    @Override
    public Collection<Link> findAllLinksByChat(long tgChatId) {
        Chat chat = chatsRepository.getChatById(tgChatId);
        return chatsToLinksRepository.getAllLinksByChat(chat);
    }
}
