package com.tact.io.domain;


import java.awt.*;

public class ClickInfo {
    private int poolId;
    private int spot;

    public ClickInfo() {}

    public ClickInfo(int poolId, int spot) {
        this.poolId = poolId;
        this.spot = spot;
    }

    public int getPoolId() {
        return poolId;
    }

    public int getSpot() {
        return spot;
    }

}
