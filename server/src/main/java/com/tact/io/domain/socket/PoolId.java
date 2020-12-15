package com.tact.io.domain.socket;

public class PoolId implements SocketBody {
    private int poolId;

    public PoolId() {
    }

    public PoolId(int poolId) {
        this.poolId = poolId;
    }

    public int getPoolId() {
        return poolId;
    }
}
