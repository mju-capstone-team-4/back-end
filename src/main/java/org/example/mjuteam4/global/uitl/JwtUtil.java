package org.example.mjuteam4.global.uitl;

import lombok.RequiredArgsConstructor;
import org.example.mjuteam4.member.Member;
import org.example.mjuteam4.member.MemberRepository;
import org.example.mjuteam4.member.exception.MemberNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final MemberRepository memberRepository;


    /**
     * 현재 로그인 관련 코드가 구현이 안되어있어서 username이 "testUser"라는 유저를 가지고옴.
     * -> db에 sql로 testUser라는 Member를 먼저 만들고 테스트해야함.
     * 현재 주석처리 되어있는 로직이 원래 사용될 현재 로그인 된 유저 가져오는 코드
     * @return
     */
    public Member getLoginMember(){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return memberRepository.findByUsername(name)
                .orElseThrow(MemberNotFoundException::new);
    }
}
