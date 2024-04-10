package edu.java.domain.repository.jpa;

import edu.java.domain.dto.Chat;
import edu.java.domain.dto.Link;
import edu.java.domain.repository.ChatsToLinksRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
@Repository
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings("MultipleStringLiterals")
public class JpaChatsToLinksRepository implements ChatsToLinksRepository {

    @Autowired
    private final EntityManagerFactory entityManagerFactory;

    @Override
    public boolean addLinkToChat(Chat chat, Link link) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        boolean result = false;
        try {
            String sql = "INSERT INTO chats_to_links VALUES (" + chat.getChatId() + ", " + link.getLinkId() + ")";
            Query query = entityManager.createNativeQuery(sql);
            result = query.executeUpdate() != 0;
        } catch (Exception e) {
            log.error("Error of addition link to chat!");
        }
        entityManager.getTransaction().commit();
        entityManager.close();
        return result;
    }

    @Override
    public boolean deleteLinkFromChat(Chat chat, Link link) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        boolean result = false;
        try {
            String sql = "DELETE FROM chats_to_links WHERE chat_id = "
                + chat.getChatId() + " AND link_id = " + link.getLinkId();
            Query query = entityManager.createNativeQuery(sql);
            result = query.executeUpdate() != 0;
        } catch (NullPointerException e) {
            log.error("Error of deletion link from chat!");
        }
        entityManager.getTransaction().commit();
        entityManager.close();
        return result;
    }

    @Override
    public boolean deleteChat(Chat chat) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        boolean result = false;
        try {
            String sql = "DELETE FROM chats_to_links WHERE chat_id = " + chat.getChatId();
            Query query = entityManager.createNativeQuery(sql);
            result = query.executeUpdate() != 0;
        } catch (NullPointerException e) {
            log.error("Error of deletion chat!");
        }
        entityManager.getTransaction().commit();
        entityManager.close();
        return result;
    }

    @Override
    public boolean isChatExist(Chat chat) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        boolean result = false;
        try {
            String sql = "SELECT DISTINCT chat_id FROM chats_to_links WHERE chat_id = " + chat.getChatId();
            Query query = entityManager.createNativeQuery(sql);
            result = !(query.getResultList().isEmpty());
        } catch (NullPointerException e) {
            log.error("Chat is not exist in table!");
        }
        entityManager.getTransaction().commit();
        entityManager.close();
        return result;
    }

    @Override
    public List<Long> getAllLinkIdsByChat(@NotNull Chat chat) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        String sql = "SELECT link_id FROM chats_to_links WHERE chat_id = " + chat.getChatId();
        Query query = entityManager.createNativeQuery(sql, Long.class);
        List<Long> result = (List<Long>) query.getResultList();
        entityManager.getTransaction().commit();
        entityManager.close();
        return result;
    }

    @Override
    public List<Long> getAllChatIdsByLink(@NotNull Link link) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        String sql = "SELECT chat_id FROM chats_to_links WHERE link_id = " + link.getLinkId();
        Query query = entityManager.createNativeQuery(sql, Long.class);
        List<Long> result = (List<Long>) query.getResultList();
        entityManager.getTransaction().commit();
        entityManager.close();
        return result;
    }
}
