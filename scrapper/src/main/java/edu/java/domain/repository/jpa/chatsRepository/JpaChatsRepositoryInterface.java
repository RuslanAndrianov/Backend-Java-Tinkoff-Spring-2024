package edu.java.domain.repository.jpa.chatsRepository;

import edu.java.domain.dto.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaChatsRepositoryInterface extends JpaRepository<Chat, Long> { }
