package org.example.mjuteam4.global.uitl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.mjuteam4.mypage.entity.Member;
import org.example.mjuteam4.mypage.repository.MemberRepository;
import org.example.mjuteam4.mypage.exception.MemberNotFoundException;
import org.example.mjuteam4.mypage.security.PrincipalDetails;
import org.example.mjuteam4.security.provider.TokenProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtUtil {

    private final MemberRepository memberRepository;

    /**
     * 현재 로그인 관련 코드가 구현이 안되어있어서 username이 "testUser"라는 유저를 가지고옴.
     * -> db에 sql로 testUser라는 Member를 먼저 만들고 테스트해야함.
     * 현재 주석처리 되어있는 로직이 원래 사용될 현재 로그인 된 유저 가져오는 코드
     * @return
     */
    public Member getLoginMember(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof PrincipalDetails principal) {
            String email = principal.getEmail();
            log.info("email = {}", email);
            return memberRepository.findByEmail(email)
                    .orElseThrow(MemberNotFoundException::new);
        } else {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            return memberRepository.findByEmail(email)
                    .orElseThrow(MemberNotFoundException::new);
        }
    }
}
