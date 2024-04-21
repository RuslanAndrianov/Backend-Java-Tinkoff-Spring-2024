package edu.java.scrapper.domain.repository.jpa;

import edu.java.scrapper.domain.dto.Chat;
import edu.java.scrapper.domain.repository.ChatsRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
@Repository
@RequiredArgsConstructor
@Slf4j
public class JpaChatsRepository implements ChatsRepository {

    @Autowired
    private final EntityManagerFactory entityManagerFactory;

    @Override
    public boolean addChat(Chat chat) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        boolean result = false;
        try {
            String sql = "INSERT INTO chats VALUES (" + chat.getChatId() + ")";
            Query query = entityManager.createNativeQuery(sql);
            result = (query.executeUpdate() != 0);
        } catch (Exception e) {
            log.error("Chat addition error!");
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
            String sql = "DELETE FROM chats WHERE chat_id = " + chat.getChatId();
            Query query = entityManager.createNativeQuery(sql);
            result = (query.executeUpdate() != 0);
        } catch (Exception e) {
            log.error("Chat deletion error!");
        }
        entityManager.getTransaction().commit();
        entityManager.close();
        return result;
    }

    @Override
    public Chat getChatById(long chatId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        String hql = "FROM Chat WHERE chatId = :chatId";
        entityManager.getTransaction().begin();
        Query query = entityManager.createQuery(hql);
        Chat chat = null;
        try {
            chat = (Chat) query
                .setParameter("chatId", chatId)
                .getResultList()
                .getFirst();
        } catch (NoSuchElementException ignored) {
            log.error("Chat with id " + chatId + " is not found!");
        }
        entityManager.getTransaction().commit();
        entityManager.close();
        return chat;
    }

    @Override
    public List<Chat> getAllChats() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        String hql = "FROM Chat";
        entityManager.getTransaction().begin();
        Query query = entityManager.createQuery(hql, Chat.class);
        List<Chat> list = (List<Chat>) query.getResultList();
        entityManager.getTransaction().commit();
        entityManager.close();
        return list;
    }
}
