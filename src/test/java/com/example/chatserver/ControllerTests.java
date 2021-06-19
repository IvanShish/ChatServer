package com.example.chatserver;

import com.example.chatserver.controllers.ChatController;
import com.example.chatserver.controllers.UserController;
import com.example.chatserver.payload.requests.ChatRequest;
import com.example.chatserver.payload.requests.SignupRequest;
import com.example.chatserver.repositories.ChatRepository;
import com.example.chatserver.repositories.MessageRepository;
import com.example.chatserver.repositories.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.TransactionSystemException;

import java.util.HashSet;
import java.util.Set;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ControllerTests {
    @Autowired
    UserRepository userRepository;

    @Autowired
    ChatRepository chatRepository;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    UserController userController;

    @Autowired
    ChatController chatController;

    @AfterEach
    public void cleanAll() {
        userRepository.deleteAll();
        chatRepository.deleteAll();
        messageRepository.deleteAll();
    }

    @Test
    void contextLoads() {
        Assertions.assertThat(userRepository).isNotNull();
        Assertions.assertThat(chatRepository).isNotNull();
        Assertions.assertThat(messageRepository).isNotNull();
        Assertions.assertThat(userController).isNotNull();
        Assertions.assertThat(chatController).isNotNull();
    }

    @Test
    void registerUserTest() {
        SignupRequest user = new SignupRequest();
        user.setUsername("user_1");
        int sizeWas = userRepository.findAll().size();
        userController.registerUser(user);

        Assertions.assertThat(userRepository.findAll()).hasSize(sizeWas + 1);
    }

    @Test
    void wrongUsernameTest() {
        SignupRequest user = new SignupRequest();
        user.setUsername("");
        int sizeWas = userRepository.findAll().size();
        try {
            userController.registerUser(user);
        } catch (TransactionSystemException ignored) {
            Assertions.assertThat(userRepository.findAll()).hasSize(sizeWas);
        }
    }

    @Test
    void duplicateUsernameTest() {
        SignupRequest user1 = new SignupRequest();
        user1.setUsername("username");
        SignupRequest user2 = new SignupRequest();
        user2.setUsername("username");
        int sizeWas = userRepository.findAll().size();
        userController.registerUser(user1);
        userController.registerUser(user2);

        Assertions.assertThat(userRepository.findAll()).hasSize(sizeWas + 1);
    }

    @Test
    void newChatTest() {
        SignupRequest user1 = new SignupRequest();
        user1.setUsername("user_1");
        SignupRequest user2 = new SignupRequest();
        user2.setUsername("user_2");
        userController.registerUser(user1);
        userController.registerUser(user2);

        ChatRequest chat = new ChatRequest();
        chat.setName("chat_1");
        Set<String> usersId = new HashSet<>();
        usersId.add(userRepository.getByUsername(user1.getUsername()).getId());
        usersId.add(userRepository.getByUsername(user2.getUsername()).getId());
        chat.setUsers(usersId);

        int sizeWas = chatRepository.findAll().size();
        chatController.newChat(chat);

        Assertions.assertThat(chatRepository.findAll()).hasSize(sizeWas + 1);
    }
}
