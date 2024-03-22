package edu.java.services;

import edu.java.domain.dto.Link;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class LinkUpdaterService {

    private final LinkService linkService;

    public void updateLinks() {
        Collection<Link> linksForUpdate = linkService.getOldestCheckedLinks();
        linksForUpdate.forEach(this::updateLink);
    }

    private void updateLink(Link link) {
        linkService.setLastCheckedTimeToLink(link);
        log.info("Update link");
    }

}
