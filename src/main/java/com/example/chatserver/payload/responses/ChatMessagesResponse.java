package com.example.chatserver.payload.responses;

import com.example.chatserver.models.Message;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class ChatMessagesResponse {
    Set<Message> messages;
}
