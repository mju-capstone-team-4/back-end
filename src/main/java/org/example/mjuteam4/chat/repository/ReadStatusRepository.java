package org.example.mjuteam4.chat.repository;

import org.example.mjuteam4.chat.domain.ChatRoom;
import org.example.mjuteam4.chat.domain.ReadStatus;
import org.example.mjuteam4.mypage.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReadStatusRepository extends JpaRepository<ReadStatus, Long> {
    List<ReadStatus> findByChatRoomAndMember(ChatRoom chatRoom, Member member);
    Long countByChatRoomAndMemberAndIsReadFalse(ChatRoom chatRoom, Member member);
}
