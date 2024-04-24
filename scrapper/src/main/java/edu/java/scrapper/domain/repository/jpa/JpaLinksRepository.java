package edu.java.scrapper.domain.repository.jpa;

import edu.java.scrapper.domain.dto.Link;
import edu.java.scrapper.domain.repository.LinksRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
@Repository
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings("MultipleStringLiterals")
public class JpaLinksRepository implements LinksRepository {

    @Autowired
    private final EntityManagerFactory entityManagerFactory;

    @Override
    public boolean addLink(Link link) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        boolean result = false;
        try {
            String sql = "INSERT INTO links VALUES (:linkId, :url, :lastUpdated, :lastChecked, :zoneOffset)";
            Query query = entityManager
                .createNativeQuery(sql)
                .setParameter("linkId", link.getLinkId())
                .setParameter("url", link.getUrl())
                .setParameter("lastUpdated", link.getLastUpdated())
                .setParameter("lastChecked", link.getLastChecked())
                .setParameter("zoneOffset", link.getZoneOffset());
            result = (query.executeUpdate() != 0);
        } catch (Exception e) {
            log.error("Link addition error!");
        }
        entityManager.getTransaction().commit();
        entityManager.close();
        return result;
    }

    @Override
    public boolean deleteLink(Link link) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        String hql = "DELETE Link WHERE linkId = :linkId";
        entityManager.getTransaction().begin();
        Query query = entityManager.createQuery(hql);
        boolean result = false;
        try {
            result = query
                .setParameter("linkId", link.getLinkId())
                .executeUpdate() != 0;
        } catch (Exception e) {
            log.error("Link deletion error!");
        }
        entityManager.getTransaction().commit();
        entityManager.close();
        return result;
    }

    @Override
    public Link getLinkById(long linkId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        String hql = "FROM Link WHERE linkId = :linkId";
        entityManager.getTransaction().begin();
        Query query = entityManager.createQuery(hql);
        Link link = null;
        try {
            link = (Link) query
                .setParameter("linkId", linkId)
                .getResultList()
                .getFirst();
        } catch (Exception e) {
            log.error("Link with id " + linkId + " is not found!");
        }
        entityManager.getTransaction().commit();
        entityManager.close();
        return link;
    }

    @Override
    public Link getLinkByUrl(String url) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        String hql = "FROM Link WHERE url = :url";
        entityManager.getTransaction().begin();
        Query query = entityManager.createQuery(hql);
        Link link = null;
        try {
            link = (Link) query
                .setParameter("url", url)
                .getResultList()
                .getFirst();
        } catch (Exception e) {
            log.error("Link " + url + " is not found!");
        }
        entityManager.getTransaction().commit();
        entityManager.close();
        return link;
    }

    @Override
    public List<Link> getAllLinks() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        String hql = "FROM Link";
        entityManager.getTransaction().begin();
        Query query = entityManager.createQuery(hql, Link.class);
        List<Link> list = (List<Link>) query.getResultList();
        entityManager.getTransaction().commit();
        entityManager.close();
        return list;
    }

    @Override
    public List<Link> getOldestCheckedLinks(String interval) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        String sql = "SELECT * FROM links WHERE (last_checked "
            + "+ make_time(zone_offset/3600, (zone_offset - (zone_offset/3600) * 3600)/60, 0)"
            + "< now() - interval '" + interval + "')";
        entityManager.getTransaction().begin();
        Query query = entityManager.createNativeQuery(sql, Link.class);
        List<Link> list = (List<Link>) query.getResultList();
        entityManager.getTransaction().commit();
        entityManager.close();
        return list;
    }

    @Override
    public boolean setLastCheckedTimeToLink(Link link, OffsetDateTime time) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        String hql = "UPDATE Link SET lastChecked = :time WHERE linkId = :linkId";
        entityManager.getTransaction().begin();
        Query query = entityManager.createQuery(hql);
        boolean result = false;
        try {
            result = query
                .setParameter("time", time)
                .setParameter("linkId", link.getLinkId())
                .executeUpdate() != 0;
        } catch (NullPointerException e) {
            log.error("Link's last_checked field update error!");
        }
        entityManager.getTransaction().commit();
        entityManager.close();
        return result;
    }

    @Override
    public boolean setLastUpdatedTimeToLink(Link link, OffsetDateTime time) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        String hql = "UPDATE Link SET lastUpdated = :time WHERE linkId = :linkId";
        entityManager.getTransaction().begin();
        Query query = entityManager.createQuery(hql);
        boolean result = false;
        try {
            result = query
                .setParameter("time", time)
                .setParameter("linkId", link.getLinkId())
                .executeUpdate() != 0;
        } catch (NullPointerException e) {
            log.error("Link's last_updated field update error!");
        }
        entityManager.getTransaction().commit();
        entityManager.close();
        return result;
    }
}
