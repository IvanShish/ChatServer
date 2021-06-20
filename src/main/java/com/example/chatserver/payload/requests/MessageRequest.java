package com.example.chatserver.payload.requests;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class MessageRequest {
    @NotBlank
    private String chat;

    @NotBlank
    private String author;

    @NotBlank
    private String text;
}
