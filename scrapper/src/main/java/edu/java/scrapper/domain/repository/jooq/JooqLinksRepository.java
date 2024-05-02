package edu.java.scrapper.domain.repository.jooq;

import edu.java.scrapper.domain.dto.Link;
import edu.java.scrapper.domain.jooq.tables.pojos.Links;
import edu.java.scrapper.domain.repository.LinksRepository;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import static edu.java.scrapper.domain.jooq.Tables.LINKS;

@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
@Repository
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings("MultipleStringLiterals")
public class JooqLinksRepository implements LinksRepository {

    @Autowired
    private final DSLContext dslContext;

    @Override
    @Transactional
    public boolean addLink(Link link) {
        boolean result = false;
        try {
            result = (dslContext
                .insertInto(LINKS)
                .values(
                    link.getLinkId(),
                    link.getUrl(),
                    link.getLastUpdated().minusSeconds(link.getZoneOffset()),
                    link.getLastChecked().minusSeconds(link.getZoneOffset()),
                    link.getZoneOffset(),
                    null)
                .execute() != 0);
        } catch (Exception e) {
            log.error("Link addition error!");
        }
        return result;
    }

    @Override
    @Transactional
    public boolean deleteLink(Link link) {
        boolean result = false;
        try {
            result = (dslContext
                .deleteFrom(LINKS)
                .where(LINKS.LINK_ID.eq(link.getLinkId()))
                .execute() != 0);
        } catch (Exception e) {
            log.error("Link deletion error!");
        }
        return result;
    }

    @Override
    @Transactional
    public Link getLinkById(long linkId) {
        Link link = null;
        try {
            link = dslContext
                .selectFrom(LINKS)
                .where(LINKS.LINK_ID.eq(linkId))
                .fetchInto(Links.class)
                .stream()
                .map(jooqPOJO -> {
                    Link listLink = new Link();
                    listLink.setLinkId(jooqPOJO.getLinkId());
                    listLink.setUrl(jooqPOJO.getUrl());
                    listLink.setLastUpdated(jooqPOJO.getLastUpdated().atOffset(ZoneOffset.of("Z")));
                    listLink.setLastChecked(jooqPOJO.getLastChecked().atOffset(ZoneOffset.of("Z")));
                    listLink.setZoneOffset(jooqPOJO.getZoneOffset());
                    return listLink;
                })
                .toList()
                .getFirst();
        } catch (Exception e) {
            log.error("Link with id " + linkId + " is not found!");
        }
        return link;
    }

    @Override
    @Transactional
    public Link getLinkByUrl(String url) {
        Link link = null;
        try {
            link = dslContext
                .selectFrom(LINKS)
                .where(LINKS.URL.eq(url))
                .fetchInto(Links.class)
                .stream()
                .map(jooqPOJO -> {
                    Link listLink = new Link();
                    listLink.setLinkId(jooqPOJO.getLinkId());
                    listLink.setUrl(jooqPOJO.getUrl());
                    listLink.setLastUpdated(jooqPOJO.getLastUpdated().atOffset(ZoneOffset.of("Z")));
                    listLink.setLastChecked(jooqPOJO.getLastChecked().atOffset(ZoneOffset.of("Z")));
                    listLink.setZoneOffset(jooqPOJO.getZoneOffset());
                    return listLink;
                })
                .toList()
                .getFirst();
        } catch (Exception e) {
            log.error("Link " + url + " is not found!");
        }
        return link;
    }

    @Override
    @Transactional
    public List<Link> getAllLinks() {
        return dslContext
            .selectFrom(LINKS)
            .fetchInto(Links.class)
            .stream()
            .map(jooqPOJO -> {
                Link listLink = new Link();
                listLink.setLinkId(jooqPOJO.getLinkId());
                listLink.setUrl(jooqPOJO.getUrl());
                listLink.setLastUpdated(jooqPOJO.getLastUpdated().atOffset(ZoneOffset.of("Z")));
                listLink.setLastChecked(jooqPOJO.getLastChecked().atOffset(ZoneOffset.of("Z")));
                listLink.setZoneOffset(jooqPOJO.getZoneOffset());
                return listLink;
            })
            .toList();
    }

    @Override
    @Transactional
    public List<Link> getOldestCheckedLinks(String interval) {
        String sql = "SELECT * FROM links WHERE (last_checked "
            + "+ make_time(zone_offset/3600, (zone_offset - (zone_offset/3600) * 3600)/60, 0)"
            + "< now() - interval '" + interval + "')";
        return dslContext
            .fetch(sql)
            .into(Links.class)
            .stream()
            .map(jooqPOJO -> {
                Link listLink = new Link();
                listLink.setLinkId(jooqPOJO.getLinkId());
                listLink.setUrl(jooqPOJO.getUrl());
                listLink.setLastUpdated(jooqPOJO.getLastUpdated().atOffset(ZoneOffset.of("Z")));
                listLink.setLastChecked(jooqPOJO.getLastChecked().atOffset(ZoneOffset.of("Z")));
                listLink.setZoneOffset(jooqPOJO.getZoneOffset());
                return listLink;
            })
            .toList();
    }

    @Override
    @Transactional
    public boolean setLastCheckedTimeToLink(Link link, OffsetDateTime time) {
        boolean result = false;
        try {
            result = (dslContext
                .update(LINKS)
                .set(LINKS.LAST_CHECKED, jooqCorrectedTime(time, link.getZoneOffset()))
                .where(LINKS.LINK_ID.eq(link.getLinkId()))
                .execute()) != 0;
        } catch (Exception e) {
            log.error("Link's last_checked field update error!");
        }
        return result;
    }

    @Override
    @Transactional
    public boolean setLastUpdatedTimeToLink(Link link, OffsetDateTime time) {
        boolean result = false;
        try {
            result = (dslContext
                .update(LINKS)
                .set(LINKS.LAST_UPDATED, time.toLocalDateTime())
                .where(LINKS.LINK_ID.eq(link.getLinkId()))
                .execute()) != 0;
        } catch (Exception e) {
            log.error("Link's last_checked field update error!");
        }
        return result;
    }

    @Override
    public boolean addAdditionalInfoToLink(Link link, String info) {
        boolean result = false;
        try {
            result = (dslContext
                .update(LINKS)
                .set(LINKS.ADDITIONAL_INFO, info)
                .where(LINKS.LINK_ID.eq(link.getLinkId()))
                .execute()) != 0;
        } catch (Exception e) {
            log.error("Link's additional_info field update error!");
        }
        return result;
    }

    private @NotNull LocalDateTime jooqCorrectedTime(@NotNull OffsetDateTime time, int offset) {
        return time.toLocalDateTime().minusSeconds(offset);
    }
}
