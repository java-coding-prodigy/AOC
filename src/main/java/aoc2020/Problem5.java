package aoc2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Problem5 {
    public static final Pattern pattern = Pattern.compile("([FB]{7})([RL]{3})");

    public static void main(String[] args) throws IOException {
        List<Integer> ids = Files.readAllLines(Path.of("src/main/resources/Passes.txt"))
                .stream()
                .map(Problem5::id)
                .toList();
        ids.forEach(i -> ids.forEach(j -> {
            if(Math.abs(i - j) == 2 && !ids.contains((i + j)/2))
                System.out.println((i + j)/2.);
        }));
    }

    static int id(String str) {
        Matcher matcher = pattern.matcher(str);
        if(!matcher.matches())
            System.out.println("No match found for" + str);
        return row(matcher.group(1)) * 8 + column(matcher.group(2));
    }

    static int row(String str) {
        return Integer.parseInt(str.chars()
                .map(ch -> ch == 'F' ? 0 : 1)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining("")), 2);
    }

    static int column(String str) {
        return Integer.parseInt(str.chars()
                .map(ch -> ch == 'L' ? 0 : 1)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining("")), 2);
    }
}
