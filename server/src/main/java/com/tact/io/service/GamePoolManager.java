package com.tact.io.service;


import com.tact.io.domain.ClickInfo;
import com.tact.io.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class GamePoolManager {
    @Autowired
    private SimpMessagingTemplate brokerMessagingTemplate;
    private Map<Integer, GamePool> gamePoolMap = new HashMap<>();

    public void joinUser(User user) {
        GamePool gamePool = findProperRoom();
        gamePool.addUser(user);
    }

    private GamePool findProperRoom() {
        GamePool resultPool = null;
        for (GamePool gamePool : gamePoolMap.values()) {
            if (!gamePool.isFull()) {
                resultPool = gamePool;
            }
        }

        if (resultPool == null) {
            int poolId = gamePoolMap.isEmpty() ? 0 : gamePoolMap.keySet().stream().mapToInt(e -> e).max().getAsInt() + 1;
            resultPool = GamePool.createNewPool(poolId, this.brokerMessagingTemplate);
            gamePoolMap.put(poolId, resultPool);
            resultPool.start();
            System.out.println("Create New Pool: " + poolId);
        }
        return resultPool;
    }

    public void clickEvent(ClickInfo info, String sessionId) {
        gamePoolMap.get(info.getPoolId()).processClick(info.getSpot(), sessionId);
    }

    public void removeUser(String sessionId) {
        for (GamePool gamePool : gamePoolMap.values()) {
            if (gamePool.isUserContains(sessionId)) {
                gamePool.removeUser(sessionId);
            }
        }
    }
}
