package edu.java.domain.repository.jpa.linksRepository;

import edu.java.domain.dto.Link;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaLinksRepositoryInterface extends JpaRepository<Link, Long> {

    @Query(value = """
        SELECT * FROM links WHERE url = :url
        """, nativeQuery = true)
    Link getLinkByUrl(@Param("url") String url);

    @Query(value = """
        SELECT * FROM links
        """, nativeQuery = true)
//    WHERE (last_checked < now() - interval ':interval')
    List<Link> getOldestCheckedLinks(@Param("interval") String interval);

    @Modifying
    @Query(value = """
        UPDATE links SET last_checked = :time WHERE link_id = :link
        """, nativeQuery = true)
    void setLastCheckedTimeToLink(
        @Param("link") Link link,
        @Param("time") OffsetDateTime time);

    @Modifying
    @Query(value = """
        UPDATE links SET last_updated = :time WHERE link_id = :link
        """, nativeQuery = true)
    void setLastUpdatedTimeToLink(
        @Param("link") Link link,
        @Param("time") OffsetDateTime time);
}
