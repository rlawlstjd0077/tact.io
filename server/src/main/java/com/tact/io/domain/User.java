package com.tact.io.domain;

import java.util.Objects;

public class User {
    private String name;
    private int score;
    private String sessionId;

    public User(String name, String sessionId) {
        this.name = name;
        this.score = 0;
        this.sessionId = sessionId;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void addScore(int score) {
        this.score += score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return score == user.score &&
            Objects.equals(name, user.name) &&
            Objects.equals(sessionId, user.sessionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, score, sessionId);
    }
}
