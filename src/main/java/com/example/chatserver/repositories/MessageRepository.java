package com.example.chatserver.repositories;

import com.example.chatserver.models.Chat;
import com.example.chatserver.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface MessageRepository extends JpaRepository<Message, String> {
    Set<Message> getAllByChat(Chat chat);
}
