package AOC2021Practice;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.IntStream;

public class Problem10 implements AoC2020Puzzle {

    private final Map<Integer, Long> checked = new HashMap<>();
    List<String> input;
    List<Integer> ratings;

    public Problem10() throws IOException {
        input = Files.readAllLines(
                Path.of("C:\\Users\\Abc\\IdeaProjects\\AOC2021\\src\\main\\resources\\2020\\Ratings.txt"));
        ratings = new ArrayList<>(input.stream().map(Integer::valueOf).toList());
        ratings.add(0);
        Collections.sort(ratings);
        ratings.add(ratings.get(ratings.size() - 1) + 3);
    }

    public static void main(String[] args) throws IOException {
        System.out.println(new Problem10().part2());
    }

    @Override public Object part1() throws IOException {
        int diff1 = 0, diff3 = 0;
        for (int i = 1; i < ratings.size(); i++) {
            int diff = ratings.get(i) - ratings.get(i - 1);
            if (diff == 1)
                diff1++;
            else if (diff == 3)
                diff3++;
        }
        return diff1 * diff3;
    }

    @Override public Object part2() throws IOException {
       return dfs(0);
    }

    private long dfs(int idx) {
        if (idx == ratings.size() - 1)
            return 1;

        if (checked.containsKey(idx))
            return checked.get(idx);

        long sum = IntStream.range(idx + 1,  ratings.size()).filter(i -> ratings.get(i) - ratings.get(idx) <= 3).mapToLong(this::dfs).sum();
        checked.put(idx, sum);
        return sum;
    }
}
