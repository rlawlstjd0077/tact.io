package com.tact.io.domain.socket;

import com.tact.io.domain.User;

import java.util.List;
import java.util.Map;

public class MapData implements SocketBody {
    private int[] spotList;
    private List<User> rankingList;

    public MapData(int[] spotList, List<User> rankingList) {
        this.spotList = spotList;
        this.rankingList = rankingList;
    }

    public MapData() { }

    public int[] getSpotList() {
        return spotList;
    }

    public List<User> getRankingList() {
        return rankingList;
    }
}
