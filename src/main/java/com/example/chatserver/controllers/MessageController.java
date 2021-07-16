package com.example.chatserver.controllers;

import com.example.chatserver.exceptions.MyNotFoundException;
import com.example.chatserver.models.Chat;
import com.example.chatserver.models.Message;
import com.example.chatserver.models.User;
import com.example.chatserver.payload.requests.ChatMessagesRequest;
import com.example.chatserver.payload.requests.MessageRequest;
import com.example.chatserver.payload.responses.ChatMessagesResponse;
import com.example.chatserver.payload.responses.MessageResponse;
import com.example.chatserver.repositories.ChatRepository;
import com.example.chatserver.repositories.MessageRepository;
import com.example.chatserver.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping("/messages")
public class MessageController {
    UserRepository userRepository;
    ChatRepository chatRepository;
    MessageRepository messageRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setChatRepository(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Autowired
    public void setMessageRepository(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @PostMapping("/add")
    public ResponseEntity<?> newMessage(@Valid @RequestBody MessageRequest messageRequest) {
        Chat chat = chatRepository.findById(messageRequest.getChat())
                .orElseThrow(() -> new MyNotFoundException("Error: Chat is not found. Message not sent"));

        User user = userRepository.findById(messageRequest.getAuthor())
                .orElseThrow(() -> new MyNotFoundException("Error: User is not found. Message not sent"));

        Set<User> usersInChat = chat.getUsers();

        if (usersInChat.stream().noneMatch(o -> o.getId().equals(user.getId()))) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: This user cannot write to this chat"));
        }

        Message message = new Message(chat, user, messageRequest.getText());
        messageRepository.save(message);
        return ResponseEntity.ok(message.getId());
    }

    @PostMapping("/get")
    public ResponseEntity<?> getChatMessages(@Valid @RequestBody ChatMessagesRequest chatMessagesRequest) {
        Chat chat = chatRepository.findById(chatMessagesRequest.getChat())
                .orElseThrow(() -> new MyNotFoundException("Error: Chat is not found"));

        ChatMessagesResponse chatMessagesResponse = new ChatMessagesResponse();
        Set<Message> messages = messageRepository.getAllByChat(chat);
        chatMessagesResponse.setMessages(messages);

        return ResponseEntity.ok(chatMessagesResponse);
    }
}
