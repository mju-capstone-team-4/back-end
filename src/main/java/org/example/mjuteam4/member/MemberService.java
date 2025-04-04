package org.example.mjuteam4.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Member findByMemberId(Long memberId) {
        // 질문 게시판 작성한 임시 코드 -> 예외 처리 필요
        return memberRepository.findById(memberId).get();
    }

}
