package com.example.chatserver.payload.responses;

import com.example.chatserver.models.Chat;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserChatsResponse {
    Set<Chat> chats;
}
