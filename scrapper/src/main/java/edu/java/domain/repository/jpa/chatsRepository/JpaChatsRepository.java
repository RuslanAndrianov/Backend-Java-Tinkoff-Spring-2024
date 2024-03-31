package edu.java.domain.repository.jpa.chatsRepository;

import edu.java.domain.dto.Chat;
import edu.java.domain.repository.ChatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class JpaChatsRepository implements ChatsRepository {

    private final JpaChatsRepositoryInterface jpaInterface;

    @Override
    public boolean addChat(Chat chat) {
        jpaInterface.save(chat);
        return jpaInterface.existsById(chat.chatId());
    }

    @Override
    public boolean deleteChat(Chat chat) {
        jpaInterface.delete(chat);
        return !jpaInterface.existsById(chat.chatId());
    }

    @Override
    public Chat getChatById(long chatId) {
        return jpaInterface.getReferenceById(chatId);
    }

    @Override
    public List<Chat> getAllChats() {
        return jpaInterface.findAll();
    }
}
