package AOC2021Practice;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public class Problem24 implements AoC2020Puzzle {

    List<String> input = Files.readAllLines(
            Path.of("C:\\Users\\Abc\\IdeaProjects\\AOC2021\\src\\main\\resources\\2020\\Paths.txt"));

    public Problem24() throws IOException {
    }

    public static void main(String[] args) throws IOException {
        System.out.println(new Problem24().part2());
    }

    @Override public Object part1() {
        int count = 1000; // something arbitrarily large;
        boolean[][] grid = new boolean[count][count];
        for (String line : input) {
            int q = count / 2;
            int r = count / 2;
            for (int i = 0; i < line.length(); i++) {
                switch (line.charAt(i)) {
                    case 'e' -> q++;
                    case 'w' -> q--;
                    case 'n' -> {
                        if (line.charAt(++i) == 'e') {
                            q++;
                        }
                        r--;
                    }
                    case 's' -> {
                        if (line.charAt(++i) == 'w') {
                            q--;
                        }
                        r++;
                    }
                }
            }
            grid[q][r] = !grid[q][r];
        }
        int black = 0;
        for (boolean[] arr : grid) {
            for (boolean bool : arr) {
                if (bool) {
                    black++;
                }
            }
        }
        return black;
    }

    @Override public Object part2() {

        int count = 1000; // something arbitrarily large;
        boolean[][] grid = new boolean[count][count];
        for (String line : input) {
            int q = count / 2;
            int r = count / 2;
            for (int i = 0; i < line.length(); i++) {
                switch (line.charAt(i)) {
                    case 'e' -> q++;
                    case 'w' -> q--;
                    case 'n' -> {
                        if (line.charAt(++i) == 'e') {
                            q++;
                        }
                        r--;
                    }
                    case 's' -> {
                        if (line.charAt(++i) == 'w') {
                            q--;
                        }
                        r++;
                    }
                }
            }
            grid[q][r] = !grid[q][r];
        }
        for (int i = 0; i < 100; i++) {
            boolean[][] next = new boolean[count][count];
            for (int q = 3; q < count - 1; q++) {
                for (int r = 3; r < count - 1; r++) {
                    int adjacent = (int) Stream.of(grid[q + 1][r], grid[q - 1][r], grid[q][r + 1],
                                    grid[q + 1][r - 1], grid[q - 1][r + 1], grid[q][r - 1])
                            .filter(Boolean::booleanValue)
                            .count();
                    if (grid[q][r]) {
                        if (adjacent == 0 || adjacent > 2)
                            next[q][r] = false;
                        else
                            next[q][r] = grid[q][r];
                    } else {
                        if (adjacent == 2)
                            next[q][r] = true;
                        else
                            next[q][r] = grid[q][r];
                    }
                }
            }
            grid = next;
        }
        int black = 0;
        for (boolean[] arr : grid) {
            for (boolean bool : arr) {
                if (bool) {
                    black++;
                }
            }
        }
        return black;
    }
}
