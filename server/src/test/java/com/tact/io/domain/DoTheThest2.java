package com.tact.io.domain;

import java.util.*;

public class DoTheThest2 {


    static LimitedQueue<String> queue = new LimitedQueue<String>(5);

    public static void main(String[] args) throws Exception {
        work("우리 우리 우리 하나 우리 국민 삼성 농협 농협 농협 국민 저축");
    }

    private static void work(String input) {
        for (String s : input.split(" ")) {
            queue.add(s);
            printContent();
        }
    }

    private static void printContent() {
        List<String> clone = (List<String>) queue.clone();
        Collections.reverse(clone);
        System.out.println(String.join(" ", clone));
    }
}

class LimitedQueue<E> extends LinkedList<E> {
    private int limit;

    public LimitedQueue(int limit) {
        this.limit = limit;
    }

    @Override
    public boolean add(E o) {
        if (super.contains(o)) {
            return true;
        }
        boolean added = super.add(o);
        while (added && size() > limit) {
            super.remove();
        }
        return added;
    }
}
