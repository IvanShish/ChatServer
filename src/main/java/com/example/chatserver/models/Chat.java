package com.example.chatserver.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "chats",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "name")
        })
@Getter
@Setter
@NoArgsConstructor
public class Chat {
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    @Size(max = 20)
    private String name;

    private Long createdAt;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_chats",
            joinColumns = @JoinColumn(name = "chat_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    @JsonIgnore
    Set<User> users = new HashSet<>();

    @PrePersist
    public void prePersist() {
        createdAt = System.currentTimeMillis();
    }

    public Chat(String name, Set<User> users) {
        this.name = name;
        this.users = users;
    }
}
