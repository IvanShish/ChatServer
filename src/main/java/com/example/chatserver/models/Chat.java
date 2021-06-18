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
@Table(name = "chats",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "name")
        })
@Getter
@Setter
@NoArgsConstructor
public class Chat {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @NotBlank(message = "Chat name is mandatory")
    @Size(max = 20)
    private String name;

    private Long createdAt;

    @ManyToMany(mappedBy = "chats")
    Set<User> users;

    @PrePersist
    public void prePersist() {
        createdAt = System.currentTimeMillis();
    }
}
