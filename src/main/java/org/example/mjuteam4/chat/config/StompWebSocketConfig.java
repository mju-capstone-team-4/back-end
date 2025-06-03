package org.example.mjuteam4.chat.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.mjuteam4.chat.config.interceptor.AuthHandshakeInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
@Slf4j
public class StompWebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final StompHandler stompHandler;
    private final AuthHandshakeInterceptor authHandshakeInterceptor;


    @Bean
    public HandshakeHandler handshakeHandler() {
        return new DefaultHandshakeHandler() {
            @Override
            protected Principal determineUser(ServerHttpRequest request,
                                              WebSocketHandler wsHandler,
                                              Map<String, Object> attributes) {
                log.debug("handshakeHandler start");

                log.debug("handshakeHandler principal {}", (Principal) attributes.get("principal"));

                return (Principal) attributes.get("principal");
            }
        };
    }



    // 웹소켓 연결을 위한 엔드포인트 /connect 등록
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/connect")
                .setAllowedOrigins("*")
                .addInterceptors(authHandshakeInterceptor) // 인증
                .setHandshakeHandler(handshakeHandler());
    }

    // STOMP 메시지 전송 경로(/publish) 및 구독 경로(/topic) 설정
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/publish");
        registry.enableSimpleBroker("/topic");
    }

    //들어오는 메시지에 대해서 인증을 처리하는 인터셉터 등록
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompHandler);
    }
}
