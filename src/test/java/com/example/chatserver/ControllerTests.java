package com.example.chatserver;

import com.example.chatserver.controllers.ChatController;
import com.example.chatserver.controllers.MessageController;
import com.example.chatserver.controllers.UserController;
import com.example.chatserver.payload.requests.ChatRequest;
import com.example.chatserver.payload.requests.MessageRequest;
import com.example.chatserver.payload.requests.SignupRequest;
import com.example.chatserver.repositories.ChatRepository;
import com.example.chatserver.repositories.MessageRepository;
import com.example.chatserver.repositories.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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

    @Autowired
    MessageController messageController;

    @BeforeAll
    void setup() {
        SignupRequest user1 = new SignupRequest();
        user1.setUsername("user_1");
        SignupRequest user2 = new SignupRequest();
        user2.setUsername("user_2");
        SignupRequest user3 = new SignupRequest();
        user3.setUsername("user_3");
        SignupRequest user4 = new SignupRequest();
        user4.setUsername("user_4");
        userController.registerUser(user1);
        userController.registerUser(user2);
        userController.registerUser(user3);
        userController.registerUser(user4);
    }

    @AfterAll
    void clean() {
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
        user.setUsername("username_test");
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
        user1.setUsername("duplicate_username");
        SignupRequest user2 = new SignupRequest();
        user2.setUsername("duplicate_username");
        int sizeWas = userRepository.findAll().size();
        userController.registerUser(user1);
        userController.registerUser(user2);

        Assertions.assertThat(userRepository.findAll()).hasSize(sizeWas + 1);
    }

    @Test
    void newChatTest() {
        ChatRequest chat = new ChatRequest();
        chat.setName("new_chat_test");
        Set<String> usersId = new HashSet<>();
        usersId.add(userRepository.getByUsername("user_1").getId());
        usersId.add(userRepository.getByUsername("user_2").getId());
        chat.setUsers(usersId);

        int sizeWas = chatRepository.findAll().size();
        chatController.newChat(chat);

        Assertions.assertThat(chatRepository.findAll()).hasSize(sizeWas + 1);
    }

    @Test
    void duplicateChatNameTest() {
        ChatRequest chat = new ChatRequest();
        chat.setName("duplicate_chat_name");
        Set<String> usersId = new HashSet<>();
        usersId.add(userRepository.getByUsername("user_1").getId());
        usersId.add(userRepository.getByUsername("user_2").getId());
        chat.setUsers(usersId);

        int sizeWas = chatRepository.findAll().size();
        chatController.newChat(chat);

        ChatRequest chat2 = new ChatRequest();
        chat2.setName("duplicate_chat_name");
        Set<String> usersId2 = new HashSet<>();
        usersId2.add(userRepository.getByUsername("user_1").getId());
        usersId2.add(userRepository.getByUsername("user_2").getId());
        chat2.setUsers(usersId2);
        chatController.newChat(chat2);

        Assertions.assertThat(chatRepository.findAll()).hasSize(sizeWas + 1);
    }

    @Test
    void getUserChatsTest() {
        SignupRequest user1 = new SignupRequest();
        user1.setUsername("new_user_for_chats");
        SignupRequest user2 = new SignupRequest();
        user2.setUsername("new_user_for_chats2");
        userController.registerUser(user1);
        userController.registerUser(user2);

        ChatRequest chat = new ChatRequest();
        chat.setName("chat_name");
        Set<String> usersId = new HashSet<>();
        usersId.add(userRepository.getByUsername(user1.getUsername()).getId());
        usersId.add(userRepository.getByUsername(user2.getUsername()).getId());
        chat.setUsers(usersId);
        chatController.newChat(chat);

        Assertions.assertThat(userRepository.getByUsername(user1.getUsername()).getChats()).hasSize(1);
        Assertions.assertThat(userRepository.getByUsername(user2.getUsername()).getChats()).hasSize(1);
    }

    @Test
    void newMessageTest() {
        ChatRequest chat = new ChatRequest();
        chat.setName("new_message_test");
        Set<String> usersId = new HashSet<>();
        usersId.add(userRepository.getByUsername("user_1").getId());
        usersId.add(userRepository.getByUsername("user_2").getId());
        chat.setUsers(usersId);
        chatController.newChat(chat);

        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setText("hi");
        messageRequest.setChat(chatRepository.getByName(chat.getName()).getId());
        messageRequest.setAuthor(userRepository.getByUsername("user_1").getId());

        int sizeWas = messageRepository.findAll().size();
        messageController.newMessage(messageRequest);
        Assertions.assertThat(messageRepository.findAll()).hasSize(sizeWas + 1);
    }
}
