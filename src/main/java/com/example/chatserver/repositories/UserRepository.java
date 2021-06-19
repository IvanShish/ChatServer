package com.example.chatserver.repositories;

import com.example.chatserver.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);
    Optional<User> findById(String id);
    User getByUsername(String username);
    Boolean existsByUsername(String username);
}
