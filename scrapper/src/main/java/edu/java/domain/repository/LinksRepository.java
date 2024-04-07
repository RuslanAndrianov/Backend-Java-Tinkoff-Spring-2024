package edu.java.domain.repository;

import edu.java.domain.dto.Link;
import java.time.OffsetDateTime;
import java.util.List;


// TODO : поправить все логи

public interface LinksRepository {
    boolean addLink(Link link);

    boolean deleteLink(Link link);

    Link getLinkById(long linkId);

    Link getLinkByUrl(String url);

    List<Link> getAllLinks();

    List<Link> getOldestCheckedLinks(String interval);

    boolean setLastCheckedTimeToLink(Link link, OffsetDateTime time);

    boolean setLastUpdatedTimeToLink(Link link, OffsetDateTime time);
}
