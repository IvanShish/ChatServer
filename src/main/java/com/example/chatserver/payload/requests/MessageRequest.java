package com.example.chatserver.payload.requests;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class MessageRequest {
    @NotBlank
    @Size(max = 20)
    private String name;

    @NotBlank
    private String chat;

    @NotBlank
    private String author;

    @NotBlank
    private String text;
}
