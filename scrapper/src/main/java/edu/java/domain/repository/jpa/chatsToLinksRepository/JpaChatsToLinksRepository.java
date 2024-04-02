package edu.java.domain.repository.jpa.chatsToLinksRepository;

import edu.java.domain.dto.Chat;
import edu.java.domain.dto.Link;
import edu.java.domain.repository.ChatsToLinksRepository;
import java.util.List;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
        long chatId = chat.getChatId();
        jpaInterface.deleteChat(chatId);
        return true;
    }

    @Override
    public boolean isChatExist(Chat chat) {
        long chatId = chat.getChatId();
        return jpaInterface.isChatExist(chatId) != null;
    }

    @Override
    public List<Link> getAllLinksByChat(Chat chat) {
        long chatId = chat.getChatId();
        return jpaInterface.getAllLinksByChat(chatId);
    }

    @Override
    public List<Long> getAllChatsByLink(Link link) {
        long linkId = link.getLinkId();
        return jpaInterface.getAllChatsByLink(linkId);
    }
}
