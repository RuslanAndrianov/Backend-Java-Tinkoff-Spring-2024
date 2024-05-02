package edu.java.bot.services;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import edu.shared_dto.request_dto.LinkUpdateRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UpdateService {

    private final TelegramBot telegramBot;

    public void sendNotificationToChats(@NotNull LinkUpdateRequest request) {
        List<Long> tgChatIds = request.tgChatIds();
        String messageText = "Новое обновление по ссылке: " + request.url() + "\n\n"
            + request.description();
        for (Long tgChatId : tgChatIds) {
            telegramBot.execute(new SendMessage(tgChatId, messageText));
        }
    }
}
