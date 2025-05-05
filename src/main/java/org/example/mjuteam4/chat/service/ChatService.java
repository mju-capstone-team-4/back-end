package org.example.mjuteam4.chat.service;

import lombok.RequiredArgsConstructor;
import org.example.mjuteam4.chat.repository.ChatMessageRepository;
import org.example.mjuteam4.chat.repository.ChatParticipantRepository;
import org.example.mjuteam4.chat.repository.ChatRoomRepository;
import org.example.mjuteam4.chat.repository.ReadStatusRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatParticipantRepository chatParticipantRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ReadStatusRepository readStatusRepository;
}
