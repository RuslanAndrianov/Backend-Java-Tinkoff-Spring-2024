package edu.java.scrapper.services;

import edu.java.scrapper.domain.dto.Chat;
import edu.java.scrapper.domain.repository.ChatsRepository;
import edu.java.scrapper.domain.repository.ChatsToLinksRepository;
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
        if (chatsToLinksRepository.isChatHasLinks(chat)) {
            result2 = chatsToLinksRepository.deleteChat(chat);
        }
        return result2 || result1;
    }

    public Chat findChatById(long tgChatId) {
        return chatsRepository.getChatById(tgChatId);
    }
}
