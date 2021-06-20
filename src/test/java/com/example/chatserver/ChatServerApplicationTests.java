package com.example.chatserver;

import com.example.chatserver.models.Chat;
import com.example.chatserver.models.Message;
import com.example.chatserver.models.User;
import com.example.chatserver.payload.requests.ChatRequest;
import com.example.chatserver.payload.requests.MessageRequest;
import com.example.chatserver.payload.requests.SignupRequest;
import com.example.chatserver.payload.responses.UserChatsResponse;
import com.example.chatserver.repositories.ChatRepository;
import com.example.chatserver.repositories.MessageRepository;
import com.example.chatserver.repositories.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.Set;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ChatServerApplicationTests {
    @Autowired
    UserRepository userRepository;

    @Autowired
    ChatRepository chatRepository;

    @Autowired
    MessageRepository messageRepository;

    @Test
    void contextLoads() {
        Assertions.assertThat(userRepository).isNotNull();
        Assertions.assertThat(chatRepository).isNotNull();
        Assertions.assertThat(messageRepository).isNotNull();
    }

    @Test
    void addNewUserTest() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername("user_1");

        User user = new User(signupRequest.getUsername());
        int sizeWas = userRepository.findAll().size();
        userRepository.save(user);

        Assertions.assertThat(userRepository.findAll()).hasSize(sizeWas + 1);
        Assertions.assertThat(userRepository.findAll()).contains(user);
    }

    @Test
    void createNewChatTest() {
        User user1 = new User("user_1");
        User user2 = new User("user_2");
        userRepository.save(user1);
        userRepository.save(user2);

        ChatRequest chatRequest = new ChatRequest();
        chatRequest.setName("chat_1");
        Set<String> usersId = new HashSet<>();
        usersId.add(user1.getId());
        usersId.add(user2.getId());
        chatRequest.setUsers(usersId);

        Set<User> users = new HashSet<>();
        for (String id : usersId) {
            users.add(userRepository.getById(id));
        }

        Chat chat = new Chat(chatRequest.getName(), users);
        int sizeWas = chatRepository.findAll().size();
        chatRepository.save(chat);

        Assertions.assertThat(chatRepository.findAll()).hasSize(sizeWas + 1);
        Assertions.assertThat(chatRepository.findAll()).contains(chat);
    }

    @Test
    void newMessageTest() {
        User user1 = new User("user_1");
        User user2 = new User("user_2");
        userRepository.save(user1);
        userRepository.save(user2);

        Set<User> users = new HashSet<>();
        users.add(userRepository.getById(user1.getId()));
        users.add(userRepository.getById(user2.getId()));
        Chat chat = new Chat("chat_1", users);
        chatRepository.save(chat);

        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setChat(chat.getId());
        messageRequest.setAuthor(user1.getId());
        messageRequest.setText("hi");

        Message message = new Message(chatRepository.getById(messageRequest.getChat()),
                userRepository.getById(messageRequest.getAuthor()), messageRequest.getText());
        int sizeWas = messageRepository.findAll().size();
        messageRepository.save(message);

        Assertions.assertThat(messageRepository.findAll()).hasSize(sizeWas + 1);
        Assertions.assertThat(messageRepository.findAll()).contains(message);
    }

    @Test
    void getUserChatsTest() {
        User user1 = new User("user_1");
        User user2 = new User("user_2");
        User user3 = new User("user_3");
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        Set<User> users1 = new HashSet<>();
        users1.add(userRepository.getById(user1.getId()));
        users1.add(userRepository.getById(user2.getId()));
        Chat chat1 = new Chat("chat_1", users1);
        chatRepository.save(chat1);
        user1.getChats().add(chat1);
        user2.getChats().add(chat1);
        userRepository.save(user1);
        userRepository.save(user2);

        Set<User> users2 = new HashSet<>();
        users2.add(userRepository.getById(user1.getId()));
        users2.add(userRepository.getById(user3.getId()));
        Chat chat2 = new Chat("chat_2", users2);
        chatRepository.save(chat2);
        user1.getChats().add(chat2);
        user3.getChats().add(chat2);
        userRepository.save(user1);
        userRepository.save(user3);

        User assert_user1 = userRepository.getById(user1.getId());
        UserChatsResponse userChatsResponse1 = new UserChatsResponse();
        userChatsResponse1.setChats(assert_user1.getChats());

        Assertions.assertThat(userChatsResponse1.getChats()).hasSize(2);
        Assertions.assertThat(userChatsResponse1.getChats()).contains(chat1, chat2);

        User assert_user2 = userRepository.getById(user2.getId());
        UserChatsResponse userChatsResponse2 = new UserChatsResponse();
        userChatsResponse2.setChats(assert_user2.getChats());

        Assertions.assertThat(userChatsResponse2.getChats()).hasSize(1);
        Assertions.assertThat(userChatsResponse2.getChats()).contains(chat1);
    }
}
