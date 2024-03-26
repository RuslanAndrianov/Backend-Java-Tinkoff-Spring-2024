package edu.java.bot.listeners;

import com.pengrad.telegrambot.UpdatesListener;
import edu.java.bot.services.MessageService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TgUpdatesListener {

    private final UpdatesListener updatesListener;

    public TgUpdatesListener(MessageService messageService) {
        this.updatesListener = list -> {
            list.forEach(messageService::handleMessage);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        };
    }
}
