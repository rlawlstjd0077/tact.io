package com.tact.io.domain;

import org.junit.Assert;
import org.junit.Test;

import java.awt.*;
import java.util.Random;

public class GamePoolTest {
    @Test
    public void testPoint() {
        Point point = new Point(1,1);
        Assert.assertTrue(point.equals(new Point(1,1)));
    }

    @Test
    public void testCreateMapData() throws InterruptedException {
        GameMap gameMap = new GameMap();
        for (int i = 0; i < 50; i++) {
            gameMap.calculateMap(2);
            System.out.println(gameMap.getMap().length);
            Thread.sleep(100);
        }

    }

    @Test
    public void testRand() throws InterruptedException {
        for (int i = 0; i < 50; i++) {
            System.out.println(new Random(System.currentTimeMillis()).nextInt(2));
            Thread.sleep(100);
        }
    }
}
