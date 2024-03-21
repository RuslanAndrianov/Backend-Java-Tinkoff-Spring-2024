package edu.java.services;

import edu.java.domain.dto.Link;
import java.util.Collection;

public interface LinkService {

    int addLinkToChatByUrl(long tgChatId, String url);

    int deleteLinkFromChatByUrl(long tgChatId, String url);

    Link findLinkByUrl(String url);

    Collection<Link> findAllLinksByChat(long tgChatId);
}
