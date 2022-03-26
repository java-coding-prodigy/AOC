package AOC2021Practice;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class Problem21 implements AoC2020Puzzle {


    List<String> input = Files.readAllLines(
            Path.of("C:\\Users\\Abc\\IdeaProjects\\AOC2021\\src\\main\\resources\\2020\\Foods.txt"));

    public Problem21() throws IOException {
    }

    public static void main(String[] args) throws IOException {
        System.out.println(new Problem21().part2());
    }

    @Override public Object part1() throws IOException {
        Map<Set<String>, Set<String>> foods = input.stream()
                .collect(Collectors.toMap(line -> Set.of(line.split(" \\(")[0].split(" ")),
                        line -> Set.of(line.split(" \\(contains ")[1].split("[,\\s)]+"))));
        Map<String, Set<String>> potentials = new HashMap<>();
        for (var entry : foods.entrySet()) {
            for (String allergen : entry.getValue()) {
                if (potentials.containsKey(allergen)) {
                    potentials.get(allergen).retainAll(entry.getKey());
                } else {
                    potentials.put(allergen, new HashSet<>());
                    potentials.get(allergen).addAll(entry.getKey());
                }
            }
        }

        Set<String> knowns =
                potentials.values().stream().flatMap(Set::stream).collect(Collectors.toSet());

        return foods.keySet()
                .stream()
                .flatMap(Collection::stream)
                .filter(ingredient -> !knowns.contains(ingredient))
                .count();
    }

    @Override public Object part2() throws IOException {
        Map<Set<String>, Set<String>> foods = input.stream()
                .collect(Collectors.toMap(line -> Set.of(line.split(" \\(")[0].split(" ")),
                        line -> Set.of(line.split(" \\(contains ")[1].split("[,\\s)]+"))));
        Map<String, Set<String>> potentials = new HashMap<>();
        for (var entry : foods.entrySet()) {
            for (String allergen : entry.getValue()) {
                if (potentials.containsKey(allergen)) {
                    potentials.get(allergen).retainAll(entry.getKey());
                } else {
                    potentials.put(allergen, new HashSet<>());
                    potentials.get(allergen).addAll(entry.getKey());
                }
            }
        }

        Map<String, String> known = new HashMap<>();
        while (!potentials.isEmpty()) {
            for (Map.Entry<String, Set<String>> entry : new HashSet<>(potentials.entrySet())) {
                String key = entry.getKey();
                Set<String> value = entry.getValue();
                if (value.size() == 1) {
                    String ingredient = value.iterator().next();
                    known.put(ingredient, key);
                    potentials.remove(key);
                    for (Set<String> otherPossibilities : potentials.values()) {
                        otherPossibilities.remove(ingredient);
                    }
                }
            }
        }

        return known.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .collect(Collectors.joining(","));
    }
}
