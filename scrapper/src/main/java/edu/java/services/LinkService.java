package edu.java.services;

import edu.java.domain.dto.Link;
import java.util.Collection;

public interface LinkService {

    void add(long tgChatId, String url);

    void remove(long tgChatId, String url);

    Collection<Link> listAll(long tgChatId);
}
