package edu.java.domain.repository.jpa;

import edu.java.domain.dto.Link;
import edu.java.domain.repository.LinksRepository;
import java.time.OffsetDateTime;
import java.util.List;

public class JpaLinksRepository implements LinksRepository {
    @Override
    public boolean addLink(Link link) {
        return false;
    }

    @Override
    public boolean deleteLink(Link link) {
        return false;
    }

    @Override
    public Link getLinkById(long linkId) {
        return null;
    }

    @Override
    public Link getLinkByUrl(String url) {
        return null;
    }

    @Override
    public List<Link> getAllLinks() {
        return null;
    }

    @Override
    public List<Link> getOldestCheckedLinks(String interval) {
        return null;
    }

    @Override
    public boolean setLastCheckedTimeToLink(Link link, OffsetDateTime time) {
        return false;
    }

    @Override
    public boolean setLastUpdatedTimeToLink(Link link, OffsetDateTime time) {
        return false;
    }
}
