package org.example.mjuteam4.chat.config;

import lombok.RequiredArgsConstructor;
import org.example.mjuteam4.chat.exception.UnauthorizedChatAccess;
import org.example.mjuteam4.chat.service.ChatService;
import org.example.mjuteam4.security.provider.TokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

    private final TokenProvider tokenProvider;
    private final ChatService chatService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        final StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if(StompCommand.CONNECT == accessor.getCommand()){
            System.out.println("connect요청시 토큰 유효성 검증");
            String bearerToken = accessor.getFirstNativeHeader("Authorization");
            String token = bearerToken.substring(7);

            //토큰 검증
            Authentication authentication = tokenProvider.getAuthentication(token);
            if(authentication == null) {
                throw new UnauthorizedChatAccess();
            }
            System.out.println("토큰 검증 완료");
        }

        if(StompCommand.SUBSCRIBE == accessor.getCommand()){
            System.out.println("subscribe 검증");
            String bearerToken = accessor.getFirstNativeHeader("Authorization");
            String token = bearerToken.substring(7);
            Authentication authentication = tokenProvider.getAuthentication(token);
            if(authentication == null) {
                throw new UnauthorizedChatAccess();
            }
            String email = authentication.getName();
            String roomId = accessor.getDestination().split("/")[2];
            if(!chatService.isRoomPaticipant(email, Long.parseLong(roomId))){
                throw new RuntimeException("해당 room에 권한이 없습니다.");
            }
        }
        return message;
    }
}
