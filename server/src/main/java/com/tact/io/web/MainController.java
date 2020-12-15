package com.tact.io.web;

import com.tact.io.domain.ClickInfo;
import com.tact.io.domain.User;
import com.tact.io.service.GamePoolManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class MainController {
    @Autowired
    private GamePoolManager gamePoolManager;

    @MessageMapping("click")
    public void clickPoint(ClickInfo clickInfo, SimpMessageHeaderAccessor messageHeaderAccessor) {
        gamePoolManager.clickEvent(clickInfo, messageHeaderAccessor.getSessionId());
    }

    @MessageMapping("enter")
    public void enterGamePool(String name, SimpMessageHeaderAccessor messageHeaderAccessor) {
        gamePoolManager.joinUser(new User(name, messageHeaderAccessor.getSessionId()));
    }
}
