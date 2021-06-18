package com.example.chatserver.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username")
        })
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @NotBlank(message = "Username is mandatory")
    @Size(max = 20)
    private String username;

    private Long createdAt;

    @ManyToMany
    @JoinTable(
            name = "users_chats",
            joinColumns = @JoinColumn(name = "usert_id"),
            inverseJoinColumns = @JoinColumn(name = "chat_id"))
    Set<Chat> chats;

    @PrePersist
    public void prePersist() {
        createdAt = System.currentTimeMillis();
    }

    public User(String username) {
        this.username = username;
    }
}