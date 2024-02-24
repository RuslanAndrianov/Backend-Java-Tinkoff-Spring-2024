package edu.java.bot.repository.in_memory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class DBUsersLinks {

    private DBUsersLinks() {
        db = new HashMap<>();
    }

    public static Map<Long, List<String>> db;

    public static void addUser(Long chatId) {
        db.put(chatId, new ArrayList<>());
    }

    public static void deleteUser(Long chatId) {
        db.remove(chatId);
    }

    public static boolean isUserRegistered(Long chatId) {
        return db.containsKey(chatId);
    }

    public static void addLink(Long chatId, String link) {
        if (isUserRegistered(chatId)) {
            List<String> links = db.get(chatId);
            links.add(link);
        }
    }

    public static void deleteLink(Long chatId, String link) {
        if (isUserRegistered(chatId)) {
            List<String> links = db.get(chatId);
            links.remove(link);
        }
    }

    public static List<String> getUserLinks(Long chatId) {
        if (isUserRegistered(chatId)) {
            return db.get(chatId);
        }
        return new ArrayList<>();
    }

    public static boolean isUserHasLink(Long chatId, String link) {
        if (isUserRegistered(chatId)) {
            return db.get(chatId).contains(link);
        }
        return false;
    }

    public static void clear() {
        db.clear();
    }
}
