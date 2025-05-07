package org.example.mjuteam4.chatbot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatBotResponseDto {
    private String message;
    private String finishReason;
}
