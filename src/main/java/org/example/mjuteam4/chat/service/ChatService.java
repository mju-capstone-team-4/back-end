package org.example.mjuteam4.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.mjuteam4.chat.domain.ChatMessage;
import org.example.mjuteam4.chat.domain.ChatParticipant;
import org.example.mjuteam4.chat.domain.ChatRoom;
import org.example.mjuteam4.chat.domain.ReadStatus;
import org.example.mjuteam4.chat.dto.ChatMessageDto;
import org.example.mjuteam4.chat.dto.MyChatListResponseDto;
import org.example.mjuteam4.chat.exception.ChatRoomNotFound;
import org.example.mjuteam4.chat.repository.ChatMessageRepository;
import org.example.mjuteam4.chat.repository.ChatParticipantRepository;
import org.example.mjuteam4.chat.repository.ChatRoomRepository;
import org.example.mjuteam4.chat.repository.ReadStatusRepository;
import org.example.mjuteam4.global.uitl.JwtUtil;
import org.example.mjuteam4.mypage.entity.Member;
import org.example.mjuteam4.mypage.exception.MemberNotFoundException;
import org.example.mjuteam4.mypage.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatParticipantRepository chatParticipantRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    public void saveMessage(Long roomId, ChatMessageDto chatMessageDto) {
        //1. 채팅방 조회
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(ChatRoomNotFound::new);

        //2. 보낸사람조회
        Member sender = memberRepository.findByUsername(chatMessageDto.getSenderEmail()).orElseThrow(MemberNotFoundException::new);

        //3. 메시지저장
        ChatMessage chatMessage = ChatMessage.builder()
                .chatRoom(chatRoom)
                .member(sender)
                .content(chatMessageDto.getMessage())
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


    // ChatParticipant객체생성 후 저장
    public void addParticipantToRoom(ChatRoom chatRoom, Member member){
        ChatParticipant chatParticipant = ChatParticipant.builder()
                .chatRoom(chatRoom)
                .member(member)
                .build();
        chatParticipantRepository.save(chatParticipant);
    }

    public List<ChatMessageDto> getChatHistory(Long roomId){
        // 내가 해당 채팅방의 참여자가 아닐경우 에러
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(ChatRoomNotFound::new);

        Member member = jwtUtil.getLoginMember();

        List<ChatParticipant> chatParticipants = chatParticipantRepository.findByChatRoom(chatRoom);
        boolean check = false;
        for(ChatParticipant c : chatParticipants){
            if(c.getMember().equals(member)){
                check = true;
            }
        }

        if(!check)throw new IllegalArgumentException("본인이 속하지 않은 채팅방입니다.");
        // 특정 room에 대한 message조회
        List<ChatMessage> chatMessages = chatMessageRepository.findByChatRoomOrderByCreatedTimeAsc(chatRoom);
        List<ChatMessageDto> chatMessageDtos = new ArrayList<>();
        for(ChatMessage c : chatMessages){
            ChatMessageDto chatMessageDto = ChatMessageDto.builder()
                    .message(c.getContent())
                    .senderEmail(c.getMember().getEmail())
                    .build();
            chatMessageDtos.add(chatMessageDto);
        }
        return chatMessageDtos;
    }

    public boolean isRoomParticipant(String name, Long roomId){
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(ChatRoomNotFound::new);
        Member member = memberRepository.findByUsername(name).orElseThrow(MemberNotFoundException::new);
        List<ChatParticipant> chatParticipants = chatParticipantRepository.findByChatRoom(chatRoom);
        for(ChatParticipant c : chatParticipants){
            if(c.getMember().equals(member)){
                return true;
            }
        }
        return false;
    }

    public List<MyChatListResponseDto> getMyChatRooms(){
        Member member = jwtUtil.getLoginMember();

        List<ChatParticipant> chatParticipants = chatParticipantRepository.findAllByMember(member);
        List<MyChatListResponseDto> chatListResDtos = new ArrayList<>();
        for(ChatParticipant c : chatParticipants){
            Long count = readStatusRepository.countByChatRoomAndMemberAndIsReadFalse(c.getChatRoom(), member);
            MyChatListResponseDto dto = MyChatListResponseDto.builder()
                    .roomId(c.getChatRoom().getId())
                    .roomName(c.getChatRoom().getName())
                    .isGroupChat(c.getChatRoom().getIsGroupChat())
                    .unReadCount(count)
                    .build();
            chatListResDtos.add(dto);
        }
        return chatListResDtos;
    }

    public Long getOrCreatePrivateRoom(Long otherMemberId){
        Member member = jwtUtil.getLoginMember();
        log.debug("chat member: " + member);
        Member otherMember = memberRepository.findById(otherMemberId).orElseThrow(MemberNotFoundException::new);
        log.debug("chat other member: " + otherMember);
        // 나와 상대방이 1:1채팅에 이미 참석하고 있다면 해당 roomId return
        Optional<ChatRoom> chatRoom = chatParticipantRepository.findExistingPrivateRoom(member.getId(), otherMember.getId());
        if(chatRoom.isPresent()){
            return chatRoom.get().getId();
        }

        // 만약에 1:1채팅방이 없을경우 기존 채팅방 개설
        ChatRoom newRoom = ChatRoom.builder()
                .isGroupChat("N")
                .name(member.getUsername() + "-" + otherMember.getUsername())
                .build();
        chatRoomRepository.save(newRoom);

        // 두사람 모두 참여자로 새롭게 추가
        addParticipantToRoom(newRoom, member);
        addParticipantToRoom(newRoom, otherMember);

        return newRoom.getId();
    }

}
