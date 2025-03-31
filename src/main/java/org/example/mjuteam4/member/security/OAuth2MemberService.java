package org.example.mjuteam4.member.security;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.mjuteam4.global.exception.GlobalException;
import org.example.mjuteam4.member.Member;
import org.example.mjuteam4.member.MemberRepository;
import org.example.mjuteam4.member.exception.MemberAlreadyExistsException;
import org.example.mjuteam4.member.exception.MemberNotFoundException;
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
        boolean alreadySign = memberRepository.findByEmail(oAuth2UserInfo.getEmail()).isPresent();

        if(alreadySign){
            throw new MemberAlreadyExistsException();
        } else {
            Member member = Member.builder()
                    .email(oAuth2UserInfo.getEmail())
                    .username(oAuth2UserInfo.getName())
                    .role(Member.Role.ROLE_USER)
                    .build();
            memberRepository.save(member);
            return new PrincipalDetails(member, userAttribute, userNameAttributeName, isFirstLogin.get());
        }

    }
}

