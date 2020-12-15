package com.tact.io.domain.socket;

public class CountDown implements SocketBody {
    private int count;
    //CountDown 상황 정의
    //1: 게임 전
    //2: 게임 중
    //3: 게임 후
    private CountDownCase countCase;

    public CountDown(int count, CountDownCase countCase) {
        this.count = count;
        this.countCase = countCase;
    }

    public int getCount() {
        return count;
    }

    public CountDownCase getCountCase() {
        return countCase;
    }

    public enum CountDownCase {
        BEFORE_GAME,
        IN_GAME,
        AFTER_GAME
    }
}
