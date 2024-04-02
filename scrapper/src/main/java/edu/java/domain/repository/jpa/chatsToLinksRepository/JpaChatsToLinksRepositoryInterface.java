package edu.java.domain.repository.jpa.chatsToLinksRepository;

import edu.java.domain.dto.Chat;
import edu.java.domain.dto.Link;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface JpaChatsToLinksRepositoryInterface extends JpaRepository<Chat, Link> {

    @Modifying
    @Query(value = """
        DELETE FROM chats_to_links WHERE chat_id = :chatId
        """, nativeQuery = true)
    void deleteChat(long chatId);

    @Query(value = """
        SELECT DISTINCT chat_id FROM chats_to_links WHERE chat_id = :chatId
        """, nativeQuery = true)
    Chat isChatExist(long chatId);

    @Query(value = """
        SELECT * FROM chats_to_links JOIN links USING (link_id) WHERE chat_id = :chatId
        """, nativeQuery = true)
    List<Link> getAllLinksByChat(long chatId);

    @Query(value = """
        SELECT chat_id FROM chats_to_links WHERE link_id = :linkId
        """, nativeQuery = true)
    List<Long> getAllChatsByLink(long linkId);
}
