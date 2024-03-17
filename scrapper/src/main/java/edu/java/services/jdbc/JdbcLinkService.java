package edu.java.services.jdbc;

import edu.java.domain.dto.Chat;
import edu.java.domain.dto.Link;
import edu.java.domain.repository.ChatsRepository;
import edu.java.domain.repository.ChatsToLinksRepository;
import edu.java.domain.repository.LinksRepository;
import edu.java.services.LinkService;
import java.time.OffsetDateTime;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JdbcLinkService implements LinkService {

    private final LinksRepository linksRepository;
    private final ChatsRepository chatsRepository;
    private final ChatsToLinksRepository chatsToLinksRepository;

    @Override
    public void add(long tgChatId, String url) {
        Chat chat = chatsRepository.findChat(tgChatId);
        long linkId = linksRepository.findAllLinks().size() + 1;
        Link link = new Link(linkId, url, OffsetDateTime.now());

        linksRepository.add(link);
        chatsToLinksRepository.addLink(chat, link);
    }

    @Override
    public void remove(long tgChatId, String url) {
        Chat chat = chatsRepository.findChat(tgChatId);
        Link link = linksRepository.findLinkByURL(url);

        chatsToLinksRepository.removeLink(chat, link);
        linksRepository.remove(link);
    }

    @Override
    public Collection<Link> listAll(long tgChatId) {
        Chat chat = chatsRepository.findChat(tgChatId);
        return chatsToLinksRepository.findAllLinksByChat(chat);
    }
}
