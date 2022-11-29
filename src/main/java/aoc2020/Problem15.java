package aoc2020;

import org.openjdk.jmh.annotations.Benchmark;

import java.util.*;

public class Problem15 implements AoC2020Puzzle {

    List<Integer> starting =
            Arrays.stream("1,0,15,2,10,13".split(",")).map(Integer::valueOf).toList();

    public static void main(String[] args) {
        System.out.println(new Problem15().part2());
    }

    @Override
    public Object part1() {
        return getNumber(2020);
    }

    private int getNumber(int ordinal) {
        List<Integer> numbers = new ArrayList<>(starting);
        Map<Integer, Integer> numberLastPos = new HashMap<>();
        for (int i = 0; i < starting.size() - 1; i++) {
            numberLastPos.put(starting.get(i), i);
        }
        int prev = numbers.get(numbers.size() - 1);
        for (int i = numbers.size() - 1; i < ordinal; i++) {
            int current = i - numberLastPos.getOrDefault(prev, i);
            numberLastPos.put(prev, i);
            prev = current;
            numbers.add(current);
        }
        return numbers.get(ordinal - 1);
    }



    @Override
    public Object part2() {
        return getNumber(30_000_000);
    }
}
