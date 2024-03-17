package edu.java.services.jdbc;

import edu.java.domain.dto.Chat;
import edu.java.domain.repository.ChatsRepository;
import edu.java.services.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import static edu.shared_dto.ChatState.REGISTERED;

@Service
@RequiredArgsConstructor
public class JdbcChatService implements ChatService {

    private final ChatsRepository chatsRepository;

    @Override
    public void register(long tgChatId) {
        Chat newChat = new Chat(tgChatId, REGISTERED.toString());
        chatsRepository.add(newChat);
    }

    @Override
    public void unregister(long tgChatId) {
        Chat delChat = chatsRepository.findChat(tgChatId);
        chatsRepository.remove(delChat);
    }
}
