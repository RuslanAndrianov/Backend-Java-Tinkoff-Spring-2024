package edu.java.services.jpa;

import edu.java.domain.dto.Chat;
import edu.java.domain.repository.jpa.chatsRepository.JpaChatsRepository;
import edu.java.domain.repository.jpa.chatsToLinksRepository.JpaChatsToLinksRepository;
import edu.java.services.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class JpaChatService implements ChatService {

    private final JpaChatsRepository jpaChatsRepository;
    private final JpaChatsToLinksRepository jpaChatsToLinksRepository;

    @Override
    public boolean addChat(long tgChatId) {
        Chat chat = new Chat();
        chat.setChatId(tgChatId);
        return jpaChatsRepository.addChat(chat);
    }

    @Override
    public boolean deleteChat(long tgChatId) {
        Chat chat = jpaChatsRepository.getChatById(tgChatId);
        boolean result1 = jpaChatsRepository.deleteChat(chat);
        boolean result2 = false;
        if (jpaChatsToLinksRepository.isChatExist(chat)) {
            result2 = jpaChatsToLinksRepository.deleteChat(chat);
        }
        return result2 || result1;
    }

    @Override
    public Chat findChatById(long tgChatId) {
        return jpaChatsRepository.getChatById(tgChatId);
    }
}
