package edu.java.domain.dto;

import edu.shared_dto.ChatState;

public record Chat(
    long chatId,
    ChatState chatState
) {
}
