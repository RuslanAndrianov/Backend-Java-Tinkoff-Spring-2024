package edu.java.scrapper.domain.repository.jooq;

import edu.java.scrapper.domain.dto.Chat;
import edu.java.scrapper.domain.dto.Link;
import edu.java.scrapper.domain.jooq.tables.pojos.Chats;
import edu.java.scrapper.domain.jooq.tables.pojos.Links;
import edu.java.scrapper.domain.repository.ChatsToLinksRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import static edu.java.scrapper.domain.jooq.Tables.CHATS_TO_LINKS;

@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
@Repository
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings("RegexpSinglelineJava")
public class JooqChatsToLinksRepository implements ChatsToLinksRepository {

    @Autowired
    private final DSLContext dslContext;

    @Override
    @Transactional
    public boolean addLinkToChat(Chat chat, Link link) {
        boolean result = false;
        try {
            result = (dslContext
                .insertInto(CHATS_TO_LINKS)
                .values(chat.getChatId(), link.getLinkId())
                .execute() != 0);
        } catch (Exception e) {
            log.error("Error of addition link to chat!");
        }
        return result;
    }

    @Override
    @Transactional
    public boolean deleteLinkFromChat(Chat chat, Link link) {
        boolean result = false;
        try {
            result = (dslContext
                .deleteFrom(CHATS_TO_LINKS)
                .where(CHATS_TO_LINKS.CHAT_ID.eq(chat.getChatId()))
                .and(CHATS_TO_LINKS.LINK_ID.eq(link.getLinkId()))
                .execute() != 0);
        } catch (Exception e) {
            log.error("Error of deletion link from chat!");
        }
        return result;
    }

    @Override
    @Transactional
    public boolean deleteChat(Chat chat) {
        boolean result = false;
        try {
            result = (dslContext
                .deleteFrom(CHATS_TO_LINKS)
                .where(CHATS_TO_LINKS.CHAT_ID.eq(chat.getChatId()))
                .execute() != 0);
        } catch (Exception e) {
            log.error("Error of deletion chat!");
        }
        return result;
    }

    @Override
    @Transactional
    public boolean isChatHasLinks(Chat chat) {
        boolean result = false;
        try {
            result = (dslContext.fetchCount(
                CHATS_TO_LINKS,
                CHATS_TO_LINKS.CHAT_ID.eq(chat.getChatId()))) != 0;
        } catch (Exception e) {
            log.error("Chat is not exist in table!");
        }
        return result;
    }

    @Override
    @Transactional
    public List<Long> getAllLinkIdsByChat(@NotNull Chat chat) {
        return dslContext
            .selectFrom(CHATS_TO_LINKS)
            .where(CHATS_TO_LINKS.CHAT_ID.eq(chat.getChatId()))
            .fetchInto(Links.class)
            .stream()
            .map(Links::getLinkId)
            .toList();
    }

    @Override
    @Transactional
    public List<Long> getAllChatIdsByLink(@NotNull Link link) {
        return dslContext
            .selectFrom(CHATS_TO_LINKS)
            .where(CHATS_TO_LINKS.LINK_ID.eq(link.getLinkId()))
            .fetchInto(Chats.class)
            .stream()
            .map(Chats::getChatId)
            .toList();
    }
}
