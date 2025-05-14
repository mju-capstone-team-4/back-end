package org.example.mjuteam4.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class TokenService {

    private final TokenRepository tokenRepository;
    private final StringRedisTemplate redisTemplate;

    public void saveAccessToken(String accessToken) {
        Token token = new Token(accessToken); // Token 객체 생성
        tokenRepository.save(token); // Redis에 저장
    }

    public void deleteAccessToken(String id) {
        tokenRepository.deleteById(id); // Redis에서 ID로 삭제
    }

    public boolean validAccessToken(String accessToken) {
        log.info("validAccessToken = {}", tokenRepository.findByAccessToken(accessToken).isPresent());
        return tokenRepository.findByAccessToken(accessToken).isPresent();
    }


}
