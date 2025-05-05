package org.example.mjuteam4.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.mjuteam4.chat.dto.ChatMessageReqDto;
import org.example.mjuteam4.chat.service.ChatService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class StompController {
    private final SimpMessageSendingOperations messageTemplate;
    private final ChatService chatService;

    @MessageMapping("/{roomId}")
    public void sendMessage(@DestinationVariable Long roomId, ChatMessageReqDto chatMessageReqDto) {
        log.debug("chatMessageReqDto.getMessage(): " + chatMessageReqDto.getMessage());
        chatService.saveMessage(roomId, chatMessageReqDto);
        messageTemplate.convertAndSend("/topic/"+roomId, chatMessageReqDto);
    }
}
