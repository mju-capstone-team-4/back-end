package org.example.mjuteam4.mypage.security;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.mjuteam4.mypage.entity.Member;
import org.example.mjuteam4.mypage.repository.MemberRepository;
import org.example.mjuteam4.mypage.exception.MemberAlreadyExistsException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@RequiredArgsConstructor
public class OAuth2MemberService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        Map<String, Object> userAttribute = super.loadUser(userRequest).getAttributes();

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        OAuth2MemberInfo oAuth2UserInfo = OAuth2MemberInfo.of(registrationId, userAttribute);

        AtomicBoolean isFirstLogin = new AtomicBoolean(false);

        // 이메일 중복 체크 및 기존 사용자 처리
        Member member = memberRepository.findByEmail(oAuth2UserInfo.getEmail())
                .orElseGet(() -> {
                    isFirstLogin.set(true); // 처음 로그인 플래그 설정
                    return memberRepository.save(oAuth2UserInfo.toMember());
                });

        return new PrincipalDetails(member, userAttribute, userNameAttributeName, isFirstLogin.get());
    }
}

