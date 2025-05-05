package org.example.mjuteam4.chat.repository;

import org.example.mjuteam4.chat.domain.ChatParticipant;
import org.example.mjuteam4.chat.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long> {
    List<ChatParticipant> findByChatRoom(ChatRoom chatRoom);
}
