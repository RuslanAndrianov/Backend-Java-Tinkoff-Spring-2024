package edu.java.domain.repository.jpa.chatsToLinksRepository;

import edu.java.domain.dto.Chat;
import edu.java.domain.dto.Link;
import edu.java.domain.repository.ChatsToLinksRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class JpaChatsToLinksRepository implements ChatsToLinksRepository {

    private final JpaChatsToLinksRepositoryInterface jpaInterface;

    @Override
    public boolean addLinkToChat(Chat chat, Link link) {
        return false;
    }

    @Override
    public boolean deleteLinkFromChat(Chat chat, Link link) {
        return false;
    }

    @Override
    public boolean deleteChat(Chat chat) {
        long chatId = chat.chatId();
        jpaInterface.deleteChat(chatId);
        return true;
    }

    @Override
    public boolean isChatExist(Chat chat) {
        long chatId = chat.chatId();
        return jpaInterface.isChatExist(chatId) != null;
    }

    @Override
    public List<Link> getAllLinksByChat(Chat chat) {
        long chatId = chat.chatId();
        return jpaInterface.getAllLinksByChat(chatId);
    }

    @Override
    public List<Long> getAllChatsByLink(Link link) {
        long linkId = link.linkId();
        return jpaInterface.getAllChatsByLink(linkId);
    }
}
