package com.example.chatserver.payload.requests;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ChatMessagesRequest {
    @NotBlank
    String chat;
}
