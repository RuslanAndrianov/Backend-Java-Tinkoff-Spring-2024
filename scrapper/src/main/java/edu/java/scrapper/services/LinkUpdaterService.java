package edu.java.scrapper.services;

import edu.java.scrapper.domain.dto.Link;
import edu.java.scrapper.updater.LinkUpdater;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class LinkUpdaterService {

    private final LinkUpdater linkUpdater;
    private final LinkService linkService;

    public void updateLinks() {
        List<Link> linksForUpdate = linkService.getOldestCheckedLinks();
        linksForUpdate.forEach(this::updateLink);
    }

    private void updateLink(Link link) {
        linkService.setLastCheckedTimeToLink(link, OffsetDateTime.now());
        linkUpdater.updateLink(link.getUrl());
    }
}
