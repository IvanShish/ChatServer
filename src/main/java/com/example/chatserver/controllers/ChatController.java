package com.example.chatserver.controllers;

import com.example.chatserver.models.Chat;
import com.example.chatserver.models.User;
import com.example.chatserver.payload.requests.ChatRequest;
import com.example.chatserver.payload.requests.SignupRequest;
import com.example.chatserver.payload.requests.UserChatsRequest;
import com.example.chatserver.payload.responses.MessageResponse;
import com.example.chatserver.payload.responses.UserChatsResponse;
import com.example.chatserver.repositories.ChatRepository;
import com.example.chatserver.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping("/chats")
public class ChatController {
    UserRepository userRepository;
    ChatRepository chatRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setChatRepository(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @PostMapping("/add")
    public ResponseEntity<?> newChat(@Valid @RequestBody ChatRequest chatRequest) {
        if (chatRepository.existsByName(chatRequest.getName())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Chat name is already taken!"));
        }

        Set<String> usersId = chatRequest.getUsers();
        if (usersId.size() < 2) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: There must be at least 2 users in the chat!"));
        }

        Chat chat = new Chat();
        chat.setName(chatRequest.getName());
        for (String id : usersId) {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Error: User is not found."));
            chat.getUsers().add(user);
            user.getChats().add(chat);
            chatRepository.save(chat);
        }

        return ResponseEntity.ok(chatRepository.getByName(chat.getName()).getId());
    }

    @PostMapping("/get")
    public ResponseEntity<?> getUserChats(@Valid @RequestBody UserChatsRequest userChatsRequest) {
        User user = userRepository.findById(userChatsRequest.getUser())
                .orElseThrow(() -> new RuntimeException("Error: User is not found."));

        Set<Chat> chats = user.getChats();
        UserChatsResponse userChatsResponse = new UserChatsResponse();
        userChatsResponse.setChats(chats);
        return ResponseEntity.ok(userChatsResponse);
    }
}
