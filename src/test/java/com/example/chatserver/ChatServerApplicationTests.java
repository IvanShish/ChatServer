package com.example.chatserver;

import com.example.chatserver.models.User;
import com.example.chatserver.payload.requests.SignupRequest;
import com.example.chatserver.repositories.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ChatServerApplicationTests {
    UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Test
    void contextLoads() {
        Assertions.assertThat(userRepository).isNotNull();
    }

    @Test
    void addNewUserTest() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername("UsernameTest");

        User user = new User(signupRequest.getUsername());
        int sizeWas = userRepository.findAll().size();
        userRepository.save(user);

        Assertions.assertThat(userRepository.findAll()).hasSize(sizeWas + 1);
        Assertions.assertThat(userRepository.findAll()).contains(user);
    }
}
