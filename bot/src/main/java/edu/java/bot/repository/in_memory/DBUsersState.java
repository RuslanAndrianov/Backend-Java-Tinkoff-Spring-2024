package edu.java.bot.repository.in_memory;

import edu.java.bot.model.UserState;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class DBUsersState {

    private DBUsersState() {
        db = new HashMap<>();
    }

    public static Map<Long, UserState> db;

    public static void addUser(Long chatId) {
        db.put(chatId, UserState.REGISTERED);
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
        return UserState.UNREGISTRED;
    }

    public static void setUserState(Long chatId, UserState newState) {
        if (isUserRegistered(chatId)) {
            DBUsersState.deleteUser(chatId);
            DBUsersState.db.put(chatId, newState);
        }
    }

    public static void clear() {
        db.clear();
    }
}
