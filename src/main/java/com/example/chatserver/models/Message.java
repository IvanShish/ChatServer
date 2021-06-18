package com.example.chatserver.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "messages")
@Getter
@Setter
@NoArgsConstructor
public class Message {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @NotBlank(message = "Chat id is mandatory")
    private String chat;

    @NotBlank(message = "Author id is mandatory")
    private String author;

    @NotBlank(message = "Text is mandatory")
    private String text;

    private Long createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = System.currentTimeMillis();
    }
}
