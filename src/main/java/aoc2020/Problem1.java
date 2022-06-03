package AOC2021Practice;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Problem1 {
    public static void main(String[] args) throws IOException {
        List<Integer> list = Files.readAllLines(
                        Path.of("C:\\Users\\Abc\\IdeaProjects\\AOC2021\\src\\main\\resources\\ExpenseReport.txt"))
                .stream()
                .map(Integer::parseInt)
                .toList();
        list.parallelStream().forEach(i -> list.parallelStream().forEach(j -> list.parallelStream().forEach(k -> {
            if(i + j + k == 2020)
                System.out.println(i * j * k);
        })));

    }
}
