package org.example.mjuteam4.chat.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class StompHandler implements ChannelInterceptor {

    private final TokenProvider tokenProvider;
    private final ChatService chatService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        final StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if(StompCommand.CONNECT == accessor.getCommand()){
            log.info("🔐 WebSocket CONNECT: Checking token validity...");
            String bearerToken = accessor.getFirstNativeHeader("Authorization");
            String token = bearerToken.substring(7);

            //토큰 검증
            Authentication authentication = tokenProvider.getAuthentication(token);
            if(authentication == null) {
                log.warn("❌ Invalid token during WebSocket CONNECT.");
                throw new UnauthorizedChatAccess();
            }
            log.info("✅ Token verified successfully for CONNECT.");
        }

        if(StompCommand.SUBSCRIBE == accessor.getCommand()){
            log.info("📥 WebSocket SUBSCRIBE: Verifying token and room access...");
            String bearerToken = accessor.getFirstNativeHeader("Authorization");
            String token = bearerToken.substring(7);
            Authentication authentication = tokenProvider.getAuthentication(token);
            if(authentication == null) {
                log.warn("❌ Invalid token during SUBSCRIBE.");
                throw new UnauthorizedChatAccess();
            }
            String name = authentication.getName();
            String roomId = accessor.getDestination().split("/")[2];
            log.info("🔍 SUBSCRIBE request by user '{}' for room '{}'", name, roomId);
            if(!chatService.isRoomParticipant(name, Long.parseLong(roomId))){
                log.warn("🚫 Access denied: User '{}' is not a participant of room '{}'", name, roomId);
                throw new RuntimeException("해당 room에 권한이 없습니다.");
            }
        }
        return message;
    }
}
