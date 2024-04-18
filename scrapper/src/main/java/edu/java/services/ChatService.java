package edu.java.services;

import edu.java.domain.dto.Chat;
import edu.java.domain.repository.ChatsRepository;
import edu.java.domain.repository.ChatsToLinksRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ChatService {

    private final ChatsRepository chatsRepository;
    private final ChatsToLinksRepository chatsToLinksRepository;

    public boolean addChat(long tgChatId) {
        Chat chat = new Chat();
        chat.setChatId(tgChatId);
        return chatsRepository.addChat(chat);
    }

    public boolean deleteChat(long tgChatId) {
        Chat chat = chatsRepository.getChatById(tgChatId);
        boolean result1 = chatsRepository.deleteChat(chat);
        boolean result2 = false;
        if (chatsToLinksRepository.isChatExist(chat)) {
            result2 = chatsToLinksRepository.deleteChat(chat);
        }
        return result2 || result1;
    }

    public Chat findChatById(long tgChatId) {
        return chatsRepository.getChatById(tgChatId);
    }
}
