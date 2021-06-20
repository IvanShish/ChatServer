package com.example.chatserver.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
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

    @NotBlank
    @Size(max = 20)
    private String username;

    private Long createdAt;

    @ManyToMany(mappedBy = "users", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//            cascade = {
//                    CascadeType.PERSIST,
//                    CascadeType.MERGE
//            })
    @OrderBy("createdAt DESC")
    Set<Chat> chats = new HashSet<>();

    @PrePersist
    public void prePersist() {
        createdAt = System.currentTimeMillis();
    }

    public User(String username) {
        this.username = username;
    }
}
