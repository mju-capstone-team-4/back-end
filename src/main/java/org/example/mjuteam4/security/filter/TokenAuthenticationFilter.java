package org.example.mjuteam4.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.mjuteam4.security.exception.TokenException;
import org.example.mjuteam4.security.provider.TokenProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RequiredArgsConstructor
@Component
@Slf4j
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        // 로그인 및 공용 엔드포인트를 필터링 대상에서 제외
        return path.equals("/auth/login") || path.startsWith("/oauth2") || path.equals("/rank");
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            String accessToken = resolveToken(request);

            if (StringUtils.hasText(accessToken)) {
                if (tokenProvider.validTokenInRedis(accessToken)) {
                    setAuthentication(accessToken);
                } else {
                    throw new TokenException("유효하지 않은 토큰입니다.");
                }
            } else {
                String reissueAccessToken = tokenProvider.reissueAccessToken(accessToken);

                if (StringUtils.hasText(reissueAccessToken)) {
                    setAuthentication(reissueAccessToken);
                    response.setHeader(AUTHORIZATION, "Bearer " + reissueAccessToken);
                } else {
                    throw new TokenException("토큰이 없거나 재발급에 실패했습니다.");
                }

            }

            filterChain.doFilter(request, response);

        } catch (TokenException e) {
            log.warn("❌ 토큰 인증 실패: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage()); // 또는 SC_BAD_REQUEST
        }
    }


    private void setAuthentication(String accessToken) {
        Authentication authentication = tokenProvider.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String resolveToken(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION);
        if (ObjectUtils.isEmpty(token) || !token.startsWith("Bearer ")) {
            return null;
        }
        return token.substring(7);
    }
}

