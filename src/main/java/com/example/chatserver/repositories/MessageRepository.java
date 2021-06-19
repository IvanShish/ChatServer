package com.example.chatserver.repositories;

import com.example.chatserver.models.Message;
import com.example.chatserver.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, String>  {
}
