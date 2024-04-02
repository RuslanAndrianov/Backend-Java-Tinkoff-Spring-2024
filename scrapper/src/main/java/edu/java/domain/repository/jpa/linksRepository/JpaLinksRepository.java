package edu.java.domain.repository.jpa.linksRepository;

import edu.java.domain.dto.Link;
import edu.java.domain.repository.LinksRepository;
import java.time.OffsetDateTime;
import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class JpaLinksRepository implements LinksRepository {

    // TODO : написать все запросы нормально, чтобы работали

    @Autowired
    private final EntityManagerFactory entityManagerFactory;
    private final JpaLinksRepositoryInterface jpaInterface;

    @Override
    public boolean addLink(Link link) {
        jpaInterface.save(link);
        return jpaInterface.existsById(link.getLinkId());
    }

    @Override
    public boolean deleteLink(Link link) {
        jpaInterface.delete(link);
        return !jpaInterface.existsById(link.getLinkId());
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
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        link.setLastChecked(time);
        entityManager.persist(link);
        //jpaInterface.setLastCheckedTimeToLink(link, time);
        entityManager.close();
        return jpaInterface.getLinkByUrl(link.getUrl()).getLastChecked().isEqual(time);
    }

    @Override
    public boolean setLastUpdatedTimeToLink(Link link, OffsetDateTime time) {
        jpaInterface.setLastUpdatedTimeToLink(link, time);
        return jpaInterface.getLinkByUrl(link.getUrl()).getLastUpdated().isEqual(time);
    }
}
