package aoc2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Problem9 implements AoC2020Puzzle {

    final List<String> input = Files.readAllLines(
            Path.of("C:\\Users\\Abc\\IdeaProjects\\AOC2021\\src\\main\\resources\\2020\\Numbers.txt"));

    public Problem9() throws IOException {
    }

    public static void main(String[] args) throws IOException {
        System.out.println(new Problem9().part2());
    }

    @Override public Object part1() throws IOException {
        List<Long> numbers = input.stream().map(Long::valueOf).toList();
        for (int idx = 25; idx < numbers.size(); idx++) {
            long currentNum = numbers.get(idx);
            Set<Long> previousNum = new HashSet<>(numbers.subList(idx - 25, idx));
            assert(previousNum.size() == 25);
            if(previousNum.stream().map(l -> currentNum - l).noneMatch(previousNum::contains))
                return currentNum;
        }
        return 0;
    }

    @Override public Object part2() throws IOException {
        long number = (Long)part1();
        List<Long> numbers = input.stream().map(Long::valueOf).toList();
        for(int i = 0; i < numbers.size(); i++){
            for(int j = i + 1; j < numbers.size(); j++){
                List<Long> contingencyList = numbers.subList(i,j);
                if(contingencyList.stream().mapToLong(Long::longValue).sum() == number){
                    SortedSet<Long> sorted = new TreeSet<>(contingencyList);
                    return sorted.first() + sorted.last();
                }
            }
        }
        return 0;
    }
}
