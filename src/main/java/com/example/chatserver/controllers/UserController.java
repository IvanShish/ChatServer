package com.example.chatserver.controllers;

import com.example.chatserver.models.User;
import com.example.chatserver.payload.requests.SignupRequest;
import com.example.chatserver.payload.responses.MessageResponse;
import com.example.chatserver.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {
    UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/add")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        User user = new User(signupRequest.getUsername());
        userRepository.save(user);
        return ResponseEntity.ok(userRepository.getByUsername(user.getUsername()).getId());
    }
}
