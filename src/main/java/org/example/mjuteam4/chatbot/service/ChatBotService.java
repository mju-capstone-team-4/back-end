package org.example.mjuteam4.chatbot.service;

import lombok.RequiredArgsConstructor;
import org.example.mjuteam4.chatbot.dto.ChatBotRequestDto;
import org.example.mjuteam4.chatbot.dto.ChatBotResponseDto;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class ChatBotService {
    private final ChatClient chatClient;

    public CompletableFuture<ChatBotResponseDto> chatJson(ChatBotRequestDto chatBotRequestDto) {
        return CompletableFuture.supplyAsync(() -> {
            return chatClient.prompt()
                    .user(chatBotRequestDto.getMessage())
                    .options(OpenAiChatOptions.builder()
                            .model("gpt-4o")
                            .temperature(0.3)
                            .maxTokens(200)
                            .build())
                    .call()
                    .entity(ChatBotResponseDto.class);
        });
    }
}
