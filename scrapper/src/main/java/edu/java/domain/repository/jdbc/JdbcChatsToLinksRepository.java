package edu.java.domain.repository.jdbc;

import edu.java.domain.dto.Chat;
import edu.java.domain.dto.Link;
import edu.java.domain.repository.ChatsToLinksRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
@Repository
@RequiredArgsConstructor
@Slf4j
public class JdbcChatsToLinksRepository implements ChatsToLinksRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Long> chatLinkRowMapper;
    private final RowMapper<Link> linkRowMapper;

    @Override
    @Transactional
    public boolean addLinkToChat(Chat chat, Link link) {
        String sql = "INSERT INTO chats_to_links VALUES (?, ?)";
        boolean result = false;
        try {
            result = (jdbcTemplate.update(sql, chat.getChatId(), link.getLinkId()) != 0);
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
            result = (jdbcTemplate.update(sql, chat.getChatId(), link.getLinkId()) != 0);
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
            result = (jdbcTemplate.update(sql, chat.getChatId()) != 0);
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
            result = (jdbcTemplate.queryForObject(sql, chatLinkRowMapper, chat.getChatId()) != 0);
        } catch (DataAccessException | NullPointerException e) {
            log.error("Chat is not exist in table!");
        }
        return result;
    }

    @Override
    @Transactional
    public List<Link> getAllLinksByChat(@NotNull Chat chat) {
        String sql = "SELECT * FROM chats_to_links JOIN links USING (link_id) WHERE chat_id = ?";
        return jdbcTemplate.query(sql, linkRowMapper, chat.getChatId());
    }

    @Override
    @Transactional
    public List<Long> getAllChatsByLink(@NotNull Link link) {
        String sql = "SELECT chat_id FROM chats_to_links WHERE link_id = ?";
        return jdbcTemplate.query(sql, chatLinkRowMapper, link.getLinkId());
    }
}
