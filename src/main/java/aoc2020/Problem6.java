package aoc2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Problem6 {
    public static void main(String[] args) throws IOException {
        var streams = Arrays.stream(
                        String.join("\n", Files.readAllLines(Path.of("src/main/resources/Answers.txt")))
                                .split("\n\n"))
                .map(group -> Arrays.stream(group.split("\n")).map(String::chars))
                .map(str -> str.map(IntStream::boxed).map(Stream::toList))
                .map(Stream::toList);
        int sum = 0;
        for (var lists : streams.toList()) {
            Set<Integer> set = new HashSet<>();
            for (int ch : Stream.iterate(97, ch -> ch < 123, x -> x + 1).toList()) {
                if (lists.stream().allMatch(list -> list.contains(ch))) {
                    set.add(ch);
                }
            }
            sum += set.size();
        }
        System.out.print(sum);
                /*.mapToInt(streams -> {
                    Set<Integer> set = new HashSet<>();
                    for (int ch : Stream.iterate(97, ch -> ch < 123, x -> x + 1).toList()) {
                        if (streams.allMatch(stream -> stream.boxed().toList().contains(ch))) {
                            set.add(ch);
                        }
                    }
                    return set.size();
                })
                .sum();*/

    }
}
