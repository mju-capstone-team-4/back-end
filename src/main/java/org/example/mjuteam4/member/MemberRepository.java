package org.example.mjuteam4.member;

import org.example.mjuteam4.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
