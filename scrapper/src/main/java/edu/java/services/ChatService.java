package edu.java.services;

import edu.java.domain.dto.Chat;

public interface ChatService {

    boolean addChat(long tgChatId);

    boolean deleteChat(long tgChatId);

    Chat findChatById(long tgChatId);
}
