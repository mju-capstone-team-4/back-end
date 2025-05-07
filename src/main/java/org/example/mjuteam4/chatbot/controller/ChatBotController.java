package org.example.mjuteam4.chatbot.controller;

import lombok.RequiredArgsConstructor;
import org.example.mjuteam4.chatbot.dto.ChatBotRequestDto;
import org.example.mjuteam4.chatbot.dto.ChatBotResponseDto;
import org.example.mjuteam4.chatbot.service.ChatBotService;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat/bot")
public class ChatBotController {

    private final ChatBotService chatBotService;

    @GetMapping("/ask")
    public CompletableFuture<ResponseEntity<ChatBotResponseDto>> getResponse(@RequestBody ChatBotRequestDto chatBotRequestDto){
        return chatBotService.chatJson(chatBotRequestDto).thenApply(response -> {
            return ResponseEntity.ok(response);
        });
    }

}
