package edu.java.domain.repository.jdbc;

import edu.java.domain.dto.Chat;
import edu.java.domain.repository.ChatsRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
@Repository
@RequiredArgsConstructor
@Slf4j
public class JdbcChatsRepository implements ChatsRepository {

    @Autowired
    private final JdbcClient jdbcClient;

    @Override
    @Transactional
    public boolean addChat(Chat chat) {
        String sql = "INSERT INTO chats VALUES (?)";
        boolean result = false;
        try {
            result = (jdbcClient
                .sql(sql)
                .param(chat.getChatId())
                .update() != 0);
        } catch (DataAccessException | NullPointerException e) {
            log.error("Chat addition error!");
        }
        return result;
    }

    @Override
    @Transactional
    public boolean deleteChat(Chat chat) {
        String sql = "DELETE FROM chats WHERE chat_id = ?";
        boolean result = false;
        try {
            result = (jdbcClient
                .sql(sql)
                .param(chat.getChatId())
                .update() != 0);
        } catch (DataAccessException | NullPointerException e) {
            log.error("Chat deletion error!");
        }
        return result;
    }

    @Override
    @Transactional
    public Chat getChatById(long chatId) {
        String sql = "SELECT * FROM chats WHERE chat_id = ?";
        Chat chat = null;
        try {
            chat = jdbcClient
                .sql(sql)
                .param(chatId)
                .query(Chat.class)
                .single();
        } catch (DataAccessException e) {
            log.error("Chat with id " + chatId + " is not found!");
        }
        return chat;
    }

    @Override
    @Transactional
    public List<Chat> getAllChats() {
        String sql = "SELECT * FROM chats";
        return jdbcClient
            .sql(sql)
            .query(Chat.class)
            .list();
    }
}
