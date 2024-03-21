package edu.java.services;

import edu.java.domain.dto.Link;
import java.util.Collection;

public interface LinkService {

    boolean addLinkToChatByUrl(long tgChatId, String url);

    boolean deleteLinkFromChatByUrl(long tgChatId, String url);

    Link findLinkByUrl(String url);

    Collection<Link> findAllLinksByChat(long tgChatId);
}
