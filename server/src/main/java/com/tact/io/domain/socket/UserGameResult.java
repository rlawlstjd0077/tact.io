package com.tact.io.domain.socket;

import com.tact.io.domain.User;

import java.util.List;

public class UserGameResult implements SocketBody {
    private boolean isAlive;
    private int score;
    private List<Integer> aliveSpots;
    private List<Integer> deadSpots;
    private List<User> rankingList;

    public UserGameResult() { }

    public UserGameResult(int score, List<Integer> aliveSpots, List<Integer> deadSpots) {
        this.score = score;
        this.aliveSpots = aliveSpots;
        this.deadSpots = deadSpots;
        isAlive = !(aliveSpots.isEmpty() && deadSpots.isEmpty()) && deadSpots.isEmpty();
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setRankingList(List<User> rankingList) {
        this.rankingList = rankingList;
    }

    public int getScore() {
        return score;
    }

    public List<Integer> getAliveSpots() {
        return aliveSpots;
    }

    public List<Integer> getDeadSpots() {
        return deadSpots;
    }

    public List<User> getRankingList() {
        return rankingList;
    }
}
