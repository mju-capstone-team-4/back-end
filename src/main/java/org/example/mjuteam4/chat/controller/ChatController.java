package org.example.mjuteam4.chat.controller;

import lombok.RequiredArgsConstructor;
import org.example.mjuteam4.chat.dto.ChatMessageDto;
import org.example.mjuteam4.chat.dto.MyChatListResponseDto;
import org.example.mjuteam4.chat.service.ChatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;

    // 이전 메시지 조회
    @GetMapping("/history/{roomId}")
    public ResponseEntity<?> getChatHistory(@PathVariable Long roomId){
        List<ChatMessageDto> chatMessageDtos = chatService.getChatHistory(roomId);
        return new ResponseEntity<>(chatMessageDtos, HttpStatus.OK);
    }

    // 내채팅방목록조회 : roomId, roomName, 그룹채팅여부, 메시지읽음개수
    @GetMapping("/my/rooms")
    public ResponseEntity<?> getMyChatRooms(){
        List<MyChatListResponseDto> myChatListResDtos = chatService.getMyChatRooms();
        return new ResponseEntity<>(myChatListResDtos, HttpStatus.OK);
    }

    // 개인 채팅방 개설 또는 기존roomId return
    @PostMapping("/room/private/create")
    public ResponseEntity<?> getOrCreatePrivateRoom(@RequestParam Long otherMemberId){
        Long roomId = chatService.getOrCreatePrivateRoom(otherMemberId);
        return new ResponseEntity<>(roomId, HttpStatus.OK);
    }
}
