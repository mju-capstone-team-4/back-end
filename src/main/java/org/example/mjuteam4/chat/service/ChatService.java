package org.example.mjuteam4.chat.service;

import lombok.RequiredArgsConstructor;
import org.example.mjuteam4.chat.domain.ChatMessage;
import org.example.mjuteam4.chat.domain.ChatParticipant;
import org.example.mjuteam4.chat.domain.ChatRoom;
import org.example.mjuteam4.chat.domain.ReadStatus;
import org.example.mjuteam4.chat.dto.ChatMessageReqDto;
import org.example.mjuteam4.chat.repository.ChatMessageRepository;
import org.example.mjuteam4.chat.repository.ChatParticipantRepository;
import org.example.mjuteam4.chat.repository.ChatRoomRepository;
import org.example.mjuteam4.chat.repository.ReadStatusRepository;
import org.example.mjuteam4.mypage.entity.Member;
import org.example.mjuteam4.mypage.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatParticipantRepository chatParticipantRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MemberRepository memberRepository;

    public void saveMessage(Long roomId, ChatMessageReqDto chatMessageReqDto) {
        //1. 채팅방 조회
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(()-> new RuntimeException("room cannot be found"));

        //2. 보낸사람조회
        Member sender = memberRepository.findByEmail(chatMessageReqDto.getSenderEmail()).orElseThrow(()-> new RuntimeException("member cannot be found"));

        //3. 메시지저장
        ChatMessage chatMessage = ChatMessage.builder()
                .chatRoom(chatRoom)
                .member(sender)
                .content(chatMessageReqDto.getMessage())
                .build();
        chatMessageRepository.save(chatMessage);

        //4. 사용자별로 읽음여부 저장
        List<ChatParticipant> chatParticipants = chatParticipantRepository.findByChatRoom(chatRoom);
        for(ChatParticipant c : chatParticipants) {
            ReadStatus readStatus = ReadStatus.builder()
                    .chatRoom(chatRoom)
                    .member(c.getMember())
                    .chatMessage(chatMessage)
                    .isRead(c.getMember().equals(sender)) // 보낸 사람은 무조건 메시지를 읽음
                    .build();
            readStatusRepository.save(readStatus);
        }

    }

}
