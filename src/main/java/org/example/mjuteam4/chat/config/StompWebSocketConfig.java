package org.example.mjuteam4.chat.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class StompWebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final StompHandler stompHandler;

    // 웹소켓 연결을 위한 엔드포인트 /connect 등록
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/connect")
                .setAllowedOrigins("http://localhost:3000")
                .withSockJS(); // ws:가 아닌 http: 엔드포인트를 사용할수 있게 해주는 sockJs라이브러리를 통한 요청을 허용하는 설정.
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
