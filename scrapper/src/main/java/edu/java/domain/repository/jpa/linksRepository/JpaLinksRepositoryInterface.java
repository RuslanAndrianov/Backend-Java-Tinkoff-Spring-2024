package edu.java.domain.repository.jpa.linksRepository;

import edu.java.domain.dto.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.time.OffsetDateTime;
import java.util.List;

public interface JpaLinksRepositoryInterface extends JpaRepository<Link, Long> {

    @Query(value = """
        SELECT * FROM links WHERE url = :url
        """)
    Link getLinkByUrl(String url);

    @Query(value = """
        SELECT * FROM links WHERE (last_checked < now() - interval ':interval')"
        """)
    List<Link> getOldestCheckedLinks(String interval);

    @Modifying
    @Query(value = """
        UPDATE links SET last_checked = :time WHERE link_id = :link
        """)
    void setLastCheckedTimeToLink(Link link, OffsetDateTime time);

    @Modifying
    @Query(value = """
        UPDATE links SET last_updated = :time WHERE link_id = :link
        """)
    void setLastUpdatedTimeToLink(Link link, OffsetDateTime time);
}
