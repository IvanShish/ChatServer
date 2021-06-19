package com.example.chatserver.repositories;

import com.example.chatserver.models.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, String> {
    Chat getByName(String name);
    Boolean existsByName(String name);
}
