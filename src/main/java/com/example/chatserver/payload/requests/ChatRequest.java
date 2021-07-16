package com.example.chatserver.payload.requests;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
public class ChatRequest {
    @NotBlank
    @Size(max = 20)
    private String name;

    Set<Long> users;
}
