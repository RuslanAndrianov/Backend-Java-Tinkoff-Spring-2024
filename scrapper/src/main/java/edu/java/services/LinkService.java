package edu.java.services;

import edu.java.domain.dto.Link;
import java.time.OffsetDateTime;
import java.util.Collection;

public interface LinkService {

    int addLinkToChatByUrl(long tgChatId, String url);

    int deleteLinkFromChatByUrl(long tgChatId, String url);

    Link getLinkByUrl(String url);

    Collection<Link> getAllLinksByChat(long tgChatId);

    Collection<Link> getOldestCheckedLinks();

    boolean setLastCheckedTimeToLink(Link link, OffsetDateTime time);

    boolean setLastUpdatedTimeToLink(Link link, OffsetDateTime time);
}
