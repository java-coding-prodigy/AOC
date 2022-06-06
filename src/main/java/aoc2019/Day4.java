package aoc2019;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day4 {
    public static void main(String[] args) {
        int min = 134792;
        int max = 675810;
        int part1 = IntStream.range(min, max).filter(Day4::check).map(i -> 1).sum();
        System.out.println("Part 1: " + part1);
        int part2 = IntStream.range(min,max).filter(Day4::check2).map(i -> 1).sum();
        System.out.println("Part 2: " + part2);
    }

    private static boolean check(int num) {
        String string = String.valueOf(num);
        if (string.length() != 6) {
            return false;
        }
        boolean bool = false;
        int prev = -1;
        for (int i : string.toCharArray()) {
            int curr = i - '0';
            if (prev == curr) {
                bool = true;
            }
            if (curr < prev) {
                return false;
            }
            prev = curr;
        }
        return bool;
    }

   private static boolean check2(int num) {
        String string = String.valueOf(num);
        if (string.length() != 6) {
            return false;
        }
        int prev = -1;
        for (int i = 0; i < string.length(); i++) {
            int curr = string.charAt(i) - '0';
            if (curr < prev) {
                return false;
            }
            prev = curr;
        }
        return string.chars().boxed()
                .collect(Collectors.groupingBy(i -> i, Collectors.counting())).containsValue(2L);
    }
}
