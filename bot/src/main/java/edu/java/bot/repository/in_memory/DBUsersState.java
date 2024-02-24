package edu.java.bot.repository.in_memory;

import edu.java.bot.model.UserState;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;
import static edu.java.bot.model.UserState.REGISTERED;
import static edu.java.bot.model.UserState.UNREGISTERED;

@Component
public class DBUsersState {

    private DBUsersState() {
        db = new HashMap<>();
    }

    public static Map<Long, UserState> db;

    public static void addUser(Long chatId) {
        db.put(chatId, REGISTERED);
    }

    public static void deleteUser(Long chatId) {
        db.remove(chatId);
    }

    public static boolean isUserRegistered(Long chatId) {
        return db.containsKey(chatId);
    }

    public static UserState getUserState(Long chatId) {
        if (isUserRegistered(chatId)) {
            return db.get(chatId);
        }
        return UNREGISTERED;
    }

    public static void setUserState(Long chatId, UserState newState) {
        if (isUserRegistered(chatId)) {
            deleteUser(chatId);
            db.put(chatId, newState);
        }
    }

    public static void clear() {
        db.clear();
    }
}
