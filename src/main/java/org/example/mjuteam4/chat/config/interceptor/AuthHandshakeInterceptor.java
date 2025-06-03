package org.example.mjuteam4.chat.config.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.mjuteam4.security.provider.TokenProvider;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthHandshakeInterceptor implements HandshakeInterceptor {
    private final TokenProvider tokenProvider;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {
        /*
        log.debug("Headers check: {}", request.getHeaders());

        log.debug("AuthHandshakeInterceptor Start");
        if (request instanceof ServletServerHttpRequest servletRequest) {
            HttpServletRequest req = servletRequest.getServletRequest();
            String token = req.getParameter("token");
            log.debug("AuthHandshakeInterceptor token: " + token);
            if (token != null && tokenProvider.validateToken(token)) {
                Authentication authentication = tokenProvider.getAuthentication(token);
                log.debug("AuthHandshakeInterceptor authentication: " + authentication.getName());
                attributes.put("auth", authentication); // 세션에 인증정보 저장
                attributes.put("principal", authentication);
                return true;
            }
        }
        log.debug("Headers check before return: {}", request.getHeaders());

        return false;

         */

        log.debug("Headers check: {}", request.getHeaders());

        log.debug("AuthHandshakeInterceptor Start");
        if (request instanceof ServletServerHttpRequest servletRequest) {
            HttpServletRequest req = servletRequest.getServletRequest();

            // 1️⃣ 기존의 쿼리 파라미터 방식 주석 처리 (원래 방식)
            // String token = req.getParameter("token");

            // 2️⃣ Authorization 헤더에서 토큰 추출
            String token = null;
            String authHeader = req.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7); // "Bearer " 부분 제거
            }

            log.debug("AuthHandshakeInterceptor token: " + token);

            // 3️⃣ 토큰 검증 및 인증 처리
            if (token != null && tokenProvider.validateToken(token)) {
                Authentication authentication = tokenProvider.getAuthentication(token);
                log.debug("AuthHandshakeInterceptor authentication: " + authentication.getName());
                attributes.put("auth", authentication); // 세션에 인증정보 저장
                attributes.put("principal", authentication);
                return true;
            }
        }

        log.debug("Headers check before return: {}", request.getHeaders());
        return false;
    }

    @Override
    public void afterHandshake(org.springframework.http.server.ServerHttpRequest request, ServerHttpResponse response, org.springframework.web.socket.WebSocketHandler wsHandler, Exception exception) {

    }
}

