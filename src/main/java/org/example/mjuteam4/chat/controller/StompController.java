package org.example.mjuteam4.chat.controller;

import lombok.RequiredArgsConstructor;
import org.example.mjuteam4.chat.dto.ChatMessageReqDto;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class StompController {
    private final SimpMessageSendingOperations messageTemplate;

    @MessageMapping("/{roomId}")
    public void sendMessage(@DestinationVariable Long roomId, ChatMessageReqDto chatMessageReqDto) {
        System.out.println(chatMessageReqDto.getMessage());
        messageTemplate.convertAndSend("/topic/"+roomId, chatMessageReqDto);
    }
}
