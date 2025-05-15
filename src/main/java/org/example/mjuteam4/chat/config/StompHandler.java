package org.example.mjuteam4.chat.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.mjuteam4.chat.exception.UnauthorizedChatAccess;
import org.example.mjuteam4.chat.service.ChatService;
import org.example.mjuteam4.security.provider.TokenProvider;
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
        log.debug("StompHandler presend start");
        final StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        log.debug("StompHandler command: {}", accessor.getCommand());
        if(StompCommand.CONNECT == accessor.getCommand()){
            log.info("🔐 WebSocket CONNECT: Checking token validity...");

            // 핸드셰이크에서 넣어준 인증 정보를 가져옴
            Authentication auth = (Authentication) accessor.getSessionAttributes().get("auth");

            if(auth == null){
                log.warn("❌ Invalid token during WebSocket CONNECT.");
                throw new UnauthorizedChatAccess();
            }
            log.info("auth in connect: " + auth.getName());
            accessor.setUser(auth);
            log.info("accessor auth in connect: " + accessor.getUser().getName());
            log.info("✅ Token verified successfully for CONNECT.");
        }

        if(StompCommand.SUBSCRIBE == accessor.getCommand()) {
            log.info("📥 WebSocket SUBSCRIBE: Verifying token and room access...");
            log.info("user is: " + accessor.getUser());
            if(accessor.getUser() == null) {
                log.warn("❌ Invalid token during SUBSCRIBE.");
                throw new UnauthorizedChatAccess();
            }
            String name = accessor.getUser().getName();
            String roomId = accessor.getDestination().split("/")[2];
            log.info("🔍 SUBSCRIBE request by user '{}' for room '{}'", name, roomId);
            if(!chatService.isRoomParticipant(name, Long.parseLong(roomId))){
                log.warn("🚫 Access denied: User '{}' is not a participant of room '{}'", name, roomId);
                throw new UnauthorizedChatAccess();
            }
        }

        return message;
    }
}
