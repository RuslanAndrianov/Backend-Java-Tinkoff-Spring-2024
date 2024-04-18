package edu.java.domain.repository;

import edu.java.domain.dto.Chat;
import edu.java.domain.dto.Link;
import java.util.List;

public interface ChatsToLinksRepository {
    boolean addLinkToChat(Chat chat, Link link);

    boolean deleteLinkFromChat(Chat chat, Link link);

    boolean deleteChat(Chat chat);

    boolean isChatExist(Chat chat);

    List<Long> getAllLinkIdsByChat(Chat chat);

    List<Long> getAllChatIdsByLink(Link link);
}
