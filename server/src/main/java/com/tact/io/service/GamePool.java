package com.tact.io.service;


import com.tact.io.domain.GameMap;
import com.tact.io.domain.User;
import com.tact.io.domain.socket.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GamePool implements Runnable {
    public static final int MINIMUM_USER_COUNT = 2;
    public static final int LIMIT_USER_COUNT = 10;

    private SimpMessagingTemplate brokerMessagingTemplate;

    private Map<String, User> userMap = new HashMap<>();
    private GameMap gameMap = new GameMap();
    private int poolId;
    private Map<Integer, List<User>> statusMap;
    private BlockingQueue<User> userQueue = new LinkedBlockingQueue<>();

    private GamePool(int poolId, SimpMessagingTemplate brokerMessagingTemplate) {
        this.poolId = poolId;
        this.brokerMessagingTemplate = brokerMessagingTemplate;
    }

    public static GamePool createNewPool(int poolId, SimpMessagingTemplate brokerMessagingTemplate) {
        return new GamePool(poolId, brokerMessagingTemplate);
    }

    public void start() {
        new Thread(this).start();
    }

    @Override
    public void run() {
        while (true) {
            processQueue();
            if (userMap.keySet().size() >= MINIMUM_USER_COUNT && userMap.size() != 0) {
                processGame();
            } else if (userMap.size() != 0) {
                sendMessage(createSocketMessage(new WaitMessage()), this.poolId + "");
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void processQueue() {
        if (!userQueue.isEmpty()) {
            List<User> userList = new ArrayList<>();
            userQueue.drainTo(userList);
            for (User user : userList) {
                System.out.println(String.format("User(%s) is enter room(%d)", user.getName(), poolId));
                sendEnterSignal(user);
                userMap.put(user.getSessionId(), user);
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendEnterSignal(User user) {
        SocketMessage socketMessage = new SocketMessage(HeaderType.POOL_ID, new PoolId(poolId));
        this.brokerMessagingTemplate.convertAndSend("/topic/greetings/" + user.getSessionId(), socketMessage);
    }

    public void processGame() {
        beforeGame();
        inGame();
        afterGame();
    }

    private void beforeGame() {
        statusMap = new HashMap<>();
        gameMap.calculateMap(userMap.size());
        System.out.println("Created map data - " + Arrays.toString(gameMap.getMap()));
        for (int i : gameMap.getMap()) {
            statusMap.put(i, new ArrayList<>());

        }

        doCountDown(3, CountDown.CountDownCase.BEFORE_GAME);
    }

    private void inGame() {
        List<User> rankList = computeRanking(Collections.emptyList());
        sendMessage(createSocketMessage(new MapData(gameMap.getMap(), rankList)), this.poolId + "");
        doCountDown(5, CountDown.CountDownCase.IN_GAME);
    }

    private void afterGame() {
        Map<User, UserGameResult> resultMap = computeGameResult();
        List<String> deadUserList = resultMap.entrySet().stream().filter(e -> !e.getValue().isAlive())
                .map(e -> e.getKey().getSessionId()).collect(Collectors.toList());
        List<User> withoutDeadUser = computeRanking(deadUserList);

        resultMap.entrySet().stream().forEach(e -> {
            UserGameResult result = e.getValue();
            result.setRankingList(withoutDeadUser);
            sendMessage(createSocketMessage(result), e.getKey().getSessionId());
            if (!e.getValue().isAlive()) {
                userMap.remove(e.getKey().getSessionId());
            }
        });

        doCountDown(5, CountDown.CountDownCase.AFTER_GAME);
    }

    private void doCountDown(int countDownStart, CountDown.CountDownCase beforeGame) {
        for (int i = countDownStart; i >= 0; i--) {
            CountDown countDown = new CountDown(i, beforeGame);
            sendMessage(createSocketMessage(countDown), this.poolId + "");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private Map<User, UserGameResult> computeGameResult() {
        Map<User, UserGameResult> resultMap = new HashMap<>();
        Map<String, List<Integer>> aliveUserMap = new HashMap<>();
        Map<String, List<Integer>> deadUserMap = new HashMap<>();
        try {
            statusMap.entrySet().stream().filter(e -> e.getValue() != null).forEach(e -> {
                List<User> userList = e.getValue();
                for (int i = 0; i < userList.size(); i++) {
                    if (i == 0) {
                        aliveUserMap.computeIfAbsent(userList.get(i).getSessionId(), key -> new ArrayList<>()).add(e.getKey());
                    } else {
                        deadUserMap.computeIfAbsent(userList.get(i).getSessionId(), key -> new ArrayList<>()).add(e.getKey());
                    }
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        for (User value : userMap.values()) {
            String sessionId = value.getSessionId();
            int turnScore = 0;
            if (aliveUserMap.containsKey(sessionId)) {
                turnScore = aliveUserMap.get(sessionId).stream().mapToInt(e -> statusMap.get(e).size()).sum();
            }
            userMap.get(sessionId).addScore(turnScore);
            UserGameResult gameResult = new UserGameResult(userMap.get(sessionId).getScore(),
                    aliveUserMap.getOrDefault(sessionId, Collections.EMPTY_LIST),
                    deadUserMap.getOrDefault(sessionId, Collections.EMPTY_LIST));
            resultMap.put(userMap.get(sessionId), gameResult);
        }
        return resultMap;
    }

    private List<User> computeRanking(List<String> deadUserList) {
        return userMap.values().stream().filter(e -> !deadUserList.contains(e.getSessionId()))
                .sorted(Comparator.comparing(User::getScore).reversed()).collect(Collectors.toList());
    }

    private SocketMessage createSocketMessage(SocketBody body) {
        HeaderType socketHeader = null;
        if (body instanceof CountDown) {
            socketHeader = HeaderType.COUNT_DOWN;
        } else if (body instanceof MapData) {
            socketHeader = HeaderType.MAP_DATA;
        } else if (body instanceof UserGameResult) {
            socketHeader = HeaderType.USER_GAME_RESULT;
        } else if (body instanceof WaitMessage) {
            socketHeader = HeaderType.WAIT_MESSAGE;
        }
        return new SocketMessage(socketHeader, body);
    }

    private void sendMessage(SocketMessage message, String target) {
        this.brokerMessagingTemplate.convertAndSend("/topic/greetings/" + target, message);
    }

    public void addUser(User user) {
        System.out.println(String.format("User(%s) is added to queue to Room(%d)", user.getName(), poolId));
        userQueue.add(user);
    }

    public boolean isFull() {
        return userMap.size() + userQueue.size() >= LIMIT_USER_COUNT;
    }

    public void processClick(Integer point, String sessionId) {
        statusMap.computeIfAbsent(point, key -> new ArrayList<>()).add(userMap.get(sessionId));
    }

    public boolean isUserContains(String sessionId) {
        return userMap.containsKey(sessionId);
    }

    public void removeUser(String sessionID) {
        userMap.remove(sessionID);
    }
}

