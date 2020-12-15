package com.tact.io.config;

import com.tact.io.service.GamePoolManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.stereotype.Component;

@Component
public class ConnectionHandler extends ChannelInterceptorAdapter {
    @Autowired
    private GamePoolManager gamePoolManager;

    @Override
    public void postSend(Message message, MessageChannel channel, boolean sent) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String sessionId = accessor.getSessionId();
        switch (accessor.getCommand()) {
            case DISCONNECT:
                gamePoolManager.removeUser(sessionId);
                break;
        }
    }
}