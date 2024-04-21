package edu.java.scrapper.domain.repository.jooq;

import edu.java.scrapper.domain.dto.Chat;
import edu.java.scrapper.domain.repository.ChatsRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import static edu.java.scrapper.domain.jooq.Tables.CHATS;

@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
@Repository
@RequiredArgsConstructor
@Slf4j
public class JooqChatsRepository implements ChatsRepository {

    @Autowired
    private final DSLContext dslContext;

    @Override
    @Transactional
    public boolean addChat(Chat chat) {
        boolean result = false;
        try {
            result = (dslContext
                .insertInto(CHATS)
                .values(chat.getChatId())
                .execute() != 0);
        } catch (Exception e) {
            log.error("Chat addition error!");
        }
        return result;
    }

    @Override
    @Transactional
    public boolean deleteChat(Chat chat) {
        boolean result = false;
        try {
            result = (dslContext
                .deleteFrom(CHATS)
                .where(CHATS.CHAT_ID.eq(chat.getChatId()))
                .execute() != 0);
        } catch (Exception e) {
            log.error("Chat deletion error!");
        }
        return result;
    }

    @Override
    @Transactional
    public Chat getChatById(long chatId) {
        Chat chat = null;
        try {
            chat = dslContext
                .selectFrom(CHATS)
                .where(CHATS.CHAT_ID.eq(chatId))
                .fetchSingleInto(Chat.class);

            // TODO : почему-то выдает 0

            log.warn("jooqchatsrepository");
            log.warn(chatId + "");
            log.warn(chat.getChatId() + "");
        } catch (Exception e) {
            log.error("Chat with id " + chatId + " is not found!");
        }
        return chat;
    }

    @Override
    @Transactional
    public List<Chat> getAllChats() {
        return dslContext
            .selectFrom(CHATS)
            .fetchInto(Chat.class);
    }
}
