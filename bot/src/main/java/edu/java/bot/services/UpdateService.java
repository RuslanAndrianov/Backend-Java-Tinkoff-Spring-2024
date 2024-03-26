package edu.java.bot.services;

import com.pengrad.telegrambot.request.SendMessage;
import edu.shared_dto.request_dto.LinkUpdateRequest;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UpdateService {

    public List<SendMessage> sendUpdateToAllChats(@NotNull LinkUpdateRequest request) {
        List<Long> tgChatIds = request.tgChatIds();
        List<SendMessage> sendMessages = new ArrayList<>();
        String messageText = "Новое обновление по ссылке: " + request.url();
        for (Long tgChatId : tgChatIds) {
            sendMessages.add(
                new SendMessage(tgChatId, messageText)
            );
        }
        return sendMessages;
    }
}
