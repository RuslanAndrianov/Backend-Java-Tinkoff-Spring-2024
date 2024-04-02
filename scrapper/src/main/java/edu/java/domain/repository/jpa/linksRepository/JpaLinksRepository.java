package edu.java.domain.repository.jpa.linksRepository;

import edu.java.domain.dto.Link;
import edu.java.domain.repository.LinksRepository;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JpaLinksRepository implements LinksRepository {

    private final JpaLinksRepositoryInterface jpaInterface;

    @Override
    public boolean addLink(Link link) {
        jpaInterface.save(link);
        return jpaInterface.existsById(link.linkId());
    }

    @Override
    public boolean deleteLink(Link link) {
        jpaInterface.delete(link);
        return !jpaInterface.existsById(link.linkId());
    }

    @Override
    public Link getLinkById(long linkId) {
        return jpaInterface.getReferenceById(linkId);
    }

    @Override
    public Link getLinkByUrl(String url) {
        return jpaInterface.getLinkByUrl(url);
    }

    @Override
    public List<Link> getAllLinks() {
        return jpaInterface.findAll();
    }

    @Override
    public List<Link> getOldestCheckedLinks(String interval) {
        return jpaInterface.getOldestCheckedLinks(interval);
    }

    @Override
    public boolean setLastCheckedTimeToLink(Link link, OffsetDateTime time) {
        jpaInterface.setLastCheckedTimeToLink(link, time);
        return jpaInterface.getLinkByUrl(link.url()).lastChecked().isEqual(time);
    }

    @Override
    public boolean setLastUpdatedTimeToLink(Link link, OffsetDateTime time) {
        jpaInterface.setLastUpdatedTimeToLink(link, time);
        return jpaInterface.getLinkByUrl(link.url()).lastUpdated().isEqual(time);
    }
}
