package AOC2021Practice;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Problem7 implements AoC2020Puzzle {
    public static void main(String[] args) throws IOException {
        String rule = "muted tomato bags contain no bags.";
        String[] split = rule.split(" bags contain ");
        System.out.println(Arrays.toString(split));
        String colorName = split[0];
        System.out.println(Arrays.toString(split[1].split("bags?,?[\\s.]?")));
        Set<String> colorsContained = Arrays.stream(split[1].split("bags?,?[\\s.?]"))
                .filter(str -> !str.equals("no"))
                .map(str -> str.substring(2, str.length() - 1))
                .collect(Collectors.toSet());
        System.out.println(colorsContained);

    }

    private static int bagsNeeded(String color, Map<String, Set<BagContained>> map) {
        return map.get(color).isEmpty() ?
                0 :
                map.get(color)
                        .stream()
                        .mapToInt(bc -> bc.number() * (bagsNeeded(bc.color(), map) + 1))
                        .sum();
    }

    @Override public Object part1() throws IOException {
        List<String> rules = (Files.readAllLines(Path.of("src/main/resources/Rules.txt")));
        Map<String, Set<String>> map = new HashMap<>();
        for (String rule : rules) {
            String[] split = rule.split(" bags contain ");
            String colorName = split[0];

            Set<String> colorsContained = Arrays.stream(split[1].split("bags?,?[\\s.]?"))
                    .filter(str -> !str.equals("no"))
                    .map(str -> str.substring(2, str.length() - 1))
                    .collect(Collectors.toSet());
            map.put(colorName, colorsContained);
        }
        return (int) Stream.iterate(map.entrySet()
                        .stream()
                        .filter(entry -> entry.getValue().contains("shiny gold"))
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toSet()), strings -> map.entrySet()
                        .stream()
                        .filter(entry -> entry.getValue().stream().anyMatch(strings::contains))
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toSet()))
                .limit(20)
                .flatMap(Collection::stream)
                .distinct()
                .count();
    }

    @Override public Object part2() throws IOException {
        List<String> rules = (Files.readAllLines(Path.of("src/main/resources/Rules.txt")));
        Map<String, Set<BagContained>> map = new HashMap<>();
        for (String rule : rules) {
            String[] split = rule.split(" bags contain ");
            String colorName = split[0];

            Set<BagContained> colorsContained = Arrays.stream(split[1].split("bags?,?[\\s.]?"))
                    .filter(str -> !str.contains("no"))
                    .map(str -> {
                        System.out.println(str);
                        return new BagContained(Integer.parseInt(str.substring(0, 1)),
                                str.substring(2, str.length() - 1));
                    })
                    .collect(Collectors.toSet());
            map.put(colorName, colorsContained);

        }
        return bagsNeeded("shiny gold", map);
    }


    private record BagContained(int number, String color) {
    }

}
