package org.example.mjuteam4.chat.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class StompEventListener {
    private final Set<String> sessions = ConcurrentHashMap.newKeySet();

    @EventListener
    public void connectHandle(SessionConnectEvent event){
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        sessions.add(accessor.getSessionId());
        log.info("🔌 WebSocket CONNECT - sessionId: {}", accessor.getSessionId());
        log.info("📊 Total sessions after connect: {}", sessions.size());
    }

    @EventListener
    public void disconnectHandle(SessionDisconnectEvent event){
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        sessions.remove(accessor.getSessionId());
        log.info("❌ WebSocket DISCONNECT - sessionId: {}", accessor.getSessionId());
        log.info("📉 Total sessions after disconnect: {}", sessions.size());
    }
}
