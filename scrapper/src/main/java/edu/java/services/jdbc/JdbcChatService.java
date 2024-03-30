package edu.java.services.jdbc;

import edu.java.domain.dto.Chat;
import edu.java.domain.repository.ChatsRepository;
import edu.java.domain.repository.ChatsToLinksRepository;
import edu.java.services.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import static edu.ChatState.REGISTERED;

@RequiredArgsConstructor
@Service
public class JdbcChatService implements ChatService {

    private final ChatsRepository chatsRepository;
    private final ChatsToLinksRepository chatsToLinksRepository;

    @Override
    public boolean addChat(long tgChatId) {
        Chat chat = new Chat(tgChatId, REGISTERED.toString());
        return chatsRepository.addChat(chat);
    }

    @Override
    public boolean deleteChat(long tgChatId) {
        Chat chat = chatsRepository.getChatById(tgChatId);
        boolean result1 = chatsRepository.deleteChat(chat);
        boolean result2 = false;
        if (chatsToLinksRepository.isChatExist(chat)) {
            result2 = chatsToLinksRepository.deleteChat(chat);
        }
        return result2 || result1;
    }

    @Override
    public Chat findChatById(long tgChatId) {
        return chatsRepository.getChatById(tgChatId);
    }
}
