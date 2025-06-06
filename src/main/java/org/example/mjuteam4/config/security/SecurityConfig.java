package org.example.mjuteam4.config.security;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.mjuteam4.mypage.security.OAuth2MemberService;
import org.example.mjuteam4.security.filter.TokenAuthenticationFilter;
import org.example.mjuteam4.security.handler.CustomAccessDeniedHandler;
import org.example.mjuteam4.security.handler.CustomAuthenticationEntryPoint;
import org.example.mjuteam4.security.handler.OAuth2SuccessHandler;
import org.example.mjuteam4.security.provider.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final TokenAuthenticationFilter tokenAuthenticationFilter;
    private final OAuth2MemberService oAuth2MemberService;
    private final OAuth2SuccessHandler oauth2SuccessHandler;
    private final TokenProvider tokenProvider;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() { // security를 적용하지 않을 리소스
        return web -> web.ignoring()
                .requestMatchers("/error", "/favicon.ico", "/api-docs", "swagger-ui/**", "/connect");
    }

    @PostConstruct
    public void enableAsyncSecurityContextPropagation() {
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
        System.out.println("SecurityContextHolder strategy set to: " + SecurityContextHolder.getContextHolderStrategy()); // 확인용 로그
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // rest api 설정
                .csrf(AbstractHttpConfigurer::disable) // csrf 비활성화 -> cookie를 사용하지 않으면 꺼도 된다. (cookie를 사용할 경우 httpOnly(XSS 방어), sameSite(CSRF 방어)로 방어해야 한다.)
                .cors(cors -> {
                    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                    CorsConfiguration config = new CorsConfiguration();

                    config.addAllowedOriginPattern("http://localhost:3000"); // 로컬 개발 환경
                    config.addAllowedOriginPattern("http://localhost:8081"); // 로컬 개발 환경

                    config.addAllowedMethod("*"); // 모든 HTTP 메서드 허용
                    config.addAllowedHeader("*"); // 모든 헤더 허용
                    config.setAllowCredentials(true); // 인증 정보 허용

                    config.addExposedHeader("Access-Control-Allow-Private-Network");

                    source.registerCorsConfiguration("/**", config);
                    cors.configurationSource(source);
                })
                .httpBasic(AbstractHttpConfigurer::disable) // 기본 인증 로그인 비활성화
                .formLogin(AbstractHttpConfigurer::disable) // 기본 login form 비활성화
                .logout(AbstractHttpConfigurer::disable) // 기본 logout 비활성화
                .headers(c -> c.frameOptions(
                        HeadersConfigurer.FrameOptionsConfig::disable).disable()) // X-Frame-Options 비활성화
                .sessionManagement(c ->
                        c.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 사용하지 않음

                .authorizeHttpRequests(request -> request
                        .requestMatchers(
                                "/oauth2/**",
                                "/auth/login",
                                "/auth/success",
                                "/rank",
                                "/connect",
                                "/api/mypage/token/**",
                                "/predict"
                        ).permitAll() // 인증 없이 접근 허용할 경로들
                        .anyRequest().authenticated()
                )// 그 외는 인증 필요
//                 oauth2 설정
                .oauth2Login(oauth -> // OAuth2 로그인 기능에 대한 여러 설정의 진입점
                        oauth.userInfoEndpoint(c -> c.userService(oAuth2MemberService))
                                .successHandler(oauth2SuccessHandler)
                )

                .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                // 인증 예외 핸들링
                .exceptionHandling((exceptions) -> exceptions
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                        .accessDeniedHandler(new CustomAccessDeniedHandler()));


        return http.build();
    }
}
