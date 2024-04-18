package edu.java.domain.repository;

import edu.java.domain.dto.Chat;
import java.util.List;

public interface ChatsRepository {
    boolean addChat(Chat chat);

    boolean deleteChat(Chat chat);

    Chat getChatById(long chatId);

    List<Chat> getAllChats();
}
