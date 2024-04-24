package edu.java.scrapper.domain.repository;

import edu.java.scrapper.domain.dto.Link;
import java.time.OffsetDateTime;
import java.util.List;

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
