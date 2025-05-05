package org.example.mjuteam4.chat.controller;

import lombok.RequiredArgsConstructor;
import org.example.mjuteam4.chat.service.ChatService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;


}
