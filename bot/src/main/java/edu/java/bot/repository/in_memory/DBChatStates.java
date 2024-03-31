package edu.java.bot.repository.in_memory;

import edu.ChatState;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import static edu.ChatState.REGISTERED;
import static edu.ChatState.UNREGISTERED;

@Component
@Slf4j
@SuppressWarnings({"MultipleStringLiterals", "HideUtilityClassConstructor"})
public class DBChatStates {

    public DBChatStates() {
        db = new HashMap<>();
    }

    public static Map<Long, ChatState> db;

    public static void addChat(Long chatId) {
        db.put(chatId, REGISTERED);
    }

    public static void deleteChat(Long chatId) {
        db.remove(chatId);
    }

    public static boolean isChatRegistered(Long chatId) {
        return db.containsKey(chatId);
    }

    public static ChatState getChatState(Long chatId) {
        if (isChatRegistered(chatId)) {
            return db.get(chatId);
        }
        return UNREGISTERED;
    }

    public static void setChatState(Long chatId, ChatState newState) {
        if (isChatRegistered(chatId)) {
            db.put(chatId, newState);
        }
    }

    public static void clear() {
        db.clear();
    }

    public static ChatState loadChatStateFromCache(long chatId) {
        Path path = Path.of("cache/" + chatId);
        ChatState chatState = UNREGISTERED;
        if (Files.notExists(path)) {
            return chatState;
        }
        try {
            chatState = ChatState.valueOf(Files.readString(path));
        } catch (IOException e) {
            log.error("Error of getting state from file " + chatId);
        }
        if (chatState != UNREGISTERED) {
            addChat(chatId);
        }
        return chatState;
    }

    public static void saveChatStateToCache(long chatId) {
        Path path = Path.of("cache/" + chatId);
        try {
            if (Files.notExists(path)) {
                Files.createFile(path);
            }
            Files.writeString(path, getChatState(chatId).toString());
        } catch (IOException e) {
            log.error("Error of saving state to file " + chatId);
        }
    }
}
