package com.tact.io.domain;

import com.tact.io.service.GamePool;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GameMap {
    public static final int LIMIT_BOARD_COLUMN = 18;
    public static final int LIMIT_BOARD_ROW = 12;

    private int[] map;

    public int[] getMap() {
        return map;
    }

    public void calculateMap(int userCount) {
        Random rand = new Random(System.currentTimeMillis());
        Point tileCountRange = getRandomRange(userCount);
        int tileCount = GamePool.LIMIT_USER_COUNT * 2;
        try {
            tileCount = rand.nextInt(tileCountRange.y - tileCountRange.x + 1) + tileCountRange.x;
        } catch (IllegalArgumentException ex) {
            System.out.println(tileCountRange.toString());
            ex.printStackTrace();
        }

        List<Integer> range = IntStream.range(0, LIMIT_BOARD_COLUMN * LIMIT_BOARD_ROW).boxed()
                .collect(Collectors.toCollection(ArrayList::new));
        Collections.shuffle(range);
        this.map = range.subList(0, tileCount).stream().mapToInt(e -> e).toArray();
    }

    private Point getRandomRange(int count) {
        Point range;

        int maxValue = LIMIT_BOARD_COLUMN * LIMIT_BOARD_ROW;

        long seed = System.currentTimeMillis();
        Random random = new Random(seed);
        int value = random.nextInt(20);
        switch (value) {
            case 0:
                range = new Point(count / 2, count);
                break;
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
                range = new Point(count * 2, count * 3);
                break;
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
                range = new Point(count * 3, count * 4);
                break;
            case 14:
            case 15:
            case 16:
                range = new Point(count * 4, count * 5);
                break;
            case 17:
            case 18:
                range = new Point(maxValue - count, maxValue);
                break;
            default:
                range = new Point(count / 2, count + (count / 2));
        }
        return range;
    }
}
