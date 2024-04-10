package edu.java.domain.repository.jdbc;

import edu.java.domain.dto.Chat;
import edu.java.domain.dto.Link;
import edu.java.domain.repository.ChatsToLinksRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class JdbcChatsToLinksRepository implements ChatsToLinksRepository {

    @Autowired
    private final JdbcClient jdbcClient;

    @Override
    @Transactional
    public boolean addLinkToChat(Chat chat, Link link) {
        String sql = "INSERT INTO chats_to_links VALUES (?, ?)";
        boolean result = false;
        try {
            result = (jdbcClient
                .sql(sql)
                .param(chat.getChatId())
                .param(link.getLinkId())
                .update() != 0);
        } catch (DataAccessException | NullPointerException e) {
            log.error("Error of addition link to chat!");
        }
        return result;
    }

    @Override
    @Transactional
    public boolean deleteLinkFromChat(Chat chat, Link link) {
        String sql = "DELETE FROM chats_to_links WHERE chat_id = ? AND link_id = ?";
        boolean result = false;
        try {
            result = (jdbcClient
                .sql(sql)
                .param(chat.getChatId())
                .param(link.getLinkId())
                .update() != 0);
        } catch (DataAccessException | NullPointerException e) {
            log.error("Error of deletion link from chat!");
        }
        return result;
    }

    @Override
    @Transactional
    public boolean deleteChat(Chat chat) {
        String sql = "DELETE FROM chats_to_links WHERE chat_id = ?";
        boolean result = false;
        try {
            result = (jdbcClient
                .sql(sql)
                .param(chat.getChatId())
                .update() != 0);
        } catch (DataAccessException | NullPointerException e) {
            log.error("Error of deletion chat!");
        }
        return result;
    }

    @Override
    @Transactional
    public boolean isChatExist(Chat chat) {
        String sql = "SELECT DISTINCT chat_id FROM chats_to_links WHERE chat_id = ?";
        boolean result = false;
        try {
            result = jdbcClient
                .sql(sql)
                .param(chat.getChatId())
                .query(Chat.class)
                .optional()
                .isPresent();
        } catch (NullPointerException e) {
            log.error("Chat is not exist in table!");
        }
        return result;
    }

    @Override
    @Transactional
    public List<Long> getAllLinkIdsByChat(@NotNull Chat chat) {
        String sql = "SELECT link_id FROM chats_to_links WHERE chat_id = ?";
        return jdbcClient
            .sql(sql)
            .param(chat.getChatId())
            .query(Long.class)
            .list();
    }

    @Override
    @Transactional
    public List<Long> getAllChatIdsByLink(@NotNull Link link) {
        String sql = "SELECT chat_id FROM chats_to_links WHERE link_id = ?";
        return jdbcClient
            .sql(sql)
            .param(link.getLinkId())
            .query(Long.class)
            .list();
    }
}
