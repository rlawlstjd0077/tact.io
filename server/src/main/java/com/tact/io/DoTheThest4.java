package com.tact.io;

import java.util.*;
import java.util.stream.Collectors;

public class DoTheThest4 {

    public static void main(String[] args) throws Exception {
        String kimString = "100 300 10 0 40 0 0 70 65";
        String leeString = "40 300 20 10 10 20 100 10 0";
        System.out.println(work(kimString, leeString));
    }

    private static String work(String kimString, String leeString) {
        List<Integer> kim = Arrays.stream(kimString.split(" ")).mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());
        List<Integer> lee = Arrays.stream(leeString.split(" ")).mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());
        List<Integer> result = new ArrayList<>();
        int kimsBud = 0;

        for (int i = 0; i < kim.size(); i++) {
            int kimDo = kim.get(i);
            int leeDo = lee.get(i);

            if (kimDo == leeDo) {
                result.add(0);
            } else if (kimDo > leeDo) {
                int tmp = kimDo - leeDo;
                if (tmp > kimsBud) {

                    result.add(tmp - kimsBud);
                    kimsBud = 0;
                } else {
                    result.add(0);
                    kimsBud -= tmp;
                }
            } else {
                kimsBud += leeDo - kimDo;
                result.add(0);
            }
        }
        return result.stream().map(e -> e + "").collect(Collectors.joining(" "));
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

    class Function {
        Map<Integer, String> map = new HashMap<>();

        public Function() {
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                map.put(i, i + "");
            }
        }

        public String compute(Integer integer) {
            return map.get(integer);
        }
    }
}
