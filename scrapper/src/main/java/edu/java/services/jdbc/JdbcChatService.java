package edu.java.services.jdbc;

import edu.java.domain.dto.Chat;
import edu.java.domain.repository.jdbc.JdbcChatsRepository;
import edu.java.domain.repository.jdbc.JdbcChatsToLinksRepository;
import edu.java.services.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
public class JdbcChatService implements ChatService {

    private final JdbcChatsRepository jdbcChatsRepository;
    private final JdbcChatsToLinksRepository jdbcChatsToLinksRepository;

    @Override
    public boolean addChat(long tgChatId) {
        Chat chat = new Chat();
        chat.setChatId(tgChatId);
        return jdbcChatsRepository.addChat(chat);
    }

    @Override
    public boolean deleteChat(long tgChatId) {
        Chat chat = jdbcChatsRepository.getChatById(tgChatId);
        boolean result1 = jdbcChatsRepository.deleteChat(chat);
        boolean result2 = false;
        if (jdbcChatsToLinksRepository.isChatExist(chat)) {
            result2 = jdbcChatsToLinksRepository.deleteChat(chat);
        }
        return result2 || result1;
    }

    @Override
    public Chat findChatById(long tgChatId) {
        return jdbcChatsRepository.getChatById(tgChatId);
    }
}
