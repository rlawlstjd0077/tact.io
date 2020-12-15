package com.tact.io;

import java.util.List;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.stream.Collectors;

public class DoTheThest {

    public static void main(String[] args) throws Exception {
        System.out.println(work("1 2 3 4 5 6"));
        System.out.println(work("1 3 5 7 7 9"));
        System.out.println(work("1 2 4 5 6"));
        System.out.println(work("2 1 3 5 7 9"));
        System.out.println(work("46 1 3 5 7 9"));
    }

    private static boolean work(String input) {
        List<Integer> numberList = Arrays.stream(input.split(" ")).mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());
        return isAsc(numberList) && isDistinct(numberList) && isValidNumbers(numberList);
    }

    private static boolean isDistinct(List<Integer> list) {
        return list.stream().distinct().count() == 6;
    }

    private static boolean isAsc(List<Integer> list) {
        return list.stream().sorted().collect(Collectors.toList()).equals(list);
    }

    private static boolean isValidNumbers(List<Integer> list) {
        int max = list.stream().max(Comparator.comparing(e -> e)).get();
        int min = list.stream().min(Comparator.comparing(e -> e)).get();
        return max < 46 && min > 0;
    }
}
