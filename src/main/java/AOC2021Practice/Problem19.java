package AOC2021Practice;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Problem19 implements AoC2020Puzzle {

    List<String> rules = new ArrayList<>();
    List<String> messages = new ArrayList<>();


    public Problem19() throws IOException {
        List<String> lines = Files.readAllLines(
                Path.of("C:\\Users\\Abc\\IdeaProjects\\AOC2021\\src\\main\\resources\\2020\\Messages.txt"));

        int i;
        for (i = 0; ; i++) {
            if (lines.get(i).isEmpty())
                break;
            rules.add(lines.get(i));
        }

        for (i++; i < lines.size(); i++) {
            messages.add(lines.get(i));
        }

    }

    public static void main(String[] args) throws IOException {
        System.out.println(new Problem19().part2());
    }

    @Override public Object part1() throws IOException {
        Map<Integer, String> ruleStringMap = rules.stream()
                .map(line -> line.split(": "))
                .collect(Collectors.toMap(split -> Integer.valueOf(split[0]), split -> split[1]));
        Map<Integer, Collection<String>> listMap = new HashMap<>();
        while (!listMap.containsKey(0)) {
            ruleStringMap.forEach((key, value) -> {
                if (!listMap.containsKey(key)) {
                    if (value.matches("\\d+")) {
                        Integer num = Integer.valueOf(value);
                        if (listMap.containsKey(num)) {
                            listMap.put(key, listMap.get(num));
                        }
                    } else if (value.matches("\"\\w\"")) {
                        listMap.put(key, Set.of(String.valueOf(value.charAt(1))));
                    } else if (value.matches("\\d+ \\| \\d+")) {
                        String[] split = value.split(" \\| ");
                        Integer first = Integer.valueOf(split[0]);
                        Integer second = Integer.valueOf(split[1]);
                        if (listMap.containsKey(first) && listMap.containsKey(second)) {
                            listMap.put(key, combine(listMap.get(first), listMap.get(second)));
                        }
                    } else if (value.matches("(\\d+\s?)*")) {
                        String[] split = value.split(" ");
                        List<Integer> ints = Arrays.stream(split).map(Integer::valueOf).toList();
                        if (ints.stream().allMatch(listMap::containsKey)) {
                            listMap.put(key, ints.stream()
                                    .map(listMap::get)
                                    .reduce(this::possibilities)
                                    .orElseThrow());
                        }
                    } else if (value.matches("\\d+ \\d+ \\| \\d+ \\d+")) {
                        String[] split = value.split(" ");
                        Integer first = Integer.valueOf(split[0]);
                        Integer second = Integer.valueOf(split[1]);
                        Integer third = Integer.valueOf(split[3]);
                        Integer fourth = Integer.valueOf(split[4]);
                        if (Stream.of(first, second, third, fourth)
                                .allMatch(listMap::containsKey)) {
                            listMap.put(key,
                                    combine(possibilities(listMap.get(first), listMap.get(second)),
                                            possibilities(listMap.get(third),
                                                    listMap.get(fourth))));
                        }
                    } else {
                        throw new IllegalStateException(value);
                    }
                }
            });
            System.out.println(listMap.size());
        }
        Predicate<String> predicate = listMap.get(0)::contains;
        return messages.stream().filter(predicate).count();
    }

    @Override public Object part2() throws IOException {
        Map<Integer, String> ruleStringMap = rules.stream()
                .map(line -> line.split(": "))
                .collect(Collectors.toMap(split -> Integer.valueOf(split[0]), split -> split[1]));
        Map<Integer, Collection<String>> collectionMap = new HashMap<>();
        while (!collectionMap.containsKey(0)) {
            ruleStringMap.forEach((key, value) -> {
                if (!collectionMap.containsKey(key)) {
                    if (value.matches("\\d+")) {
                        Integer num = Integer.valueOf(value);
                        if (collectionMap.containsKey(num)) {
                            collectionMap.put(key, collectionMap.get(num));
                        }
                    } else if (value.matches("\"\\w\"")) {
                        collectionMap.put(key, Set.of(String.valueOf(value.charAt(1))));
                    } else if (value.matches("\\d+ \\| \\d+")) {
                        String[] split = value.split(" \\| ");
                        Integer first = Integer.valueOf(split[0]);
                        Integer second = Integer.valueOf(split[1]);
                        if (collectionMap.containsKey(first) && collectionMap.containsKey(second)) {
                            collectionMap.put(key,
                                    combine(collectionMap.get(first), collectionMap.get(second)));
                        }
                    } else if (value.matches("(\\d+\s?)*")) {
                        String[] split = value.split(" ");
                        List<Integer> ints = Arrays.stream(split).map(Integer::valueOf).toList();
                        if (ints.stream().allMatch(collectionMap::containsKey)) {
                            collectionMap.put(key, ints.stream()
                                    .map(collectionMap::get)
                                    .reduce(this::possibilities)
                                    .orElseThrow());
                        }
                    } else if (value.matches("\\d+ \\d+ \\| \\d+ \\d+")) {
                        String[] split = value.split(" ");
                        Integer first = Integer.valueOf(split[0]);
                        Integer second = Integer.valueOf(split[1]);
                        Integer third = Integer.valueOf(split[3]);
                        Integer fourth = Integer.valueOf(split[4]);
                        if (Stream.of(first, second, third, fourth)
                                .allMatch(collectionMap::containsKey)) {
                            collectionMap.put(key, combine(possibilities(collectionMap.get(first),
                                            collectionMap.get(second)),
                                    possibilities(collectionMap.get(third),
                                            collectionMap.get(fourth))));
                        }
                    } else {
                        throw new IllegalStateException(value);
                    }
                }
            });
            //System.out.println(collectionMap.size());
        }

        Collection<String> $42 = collectionMap.get(42);
        Collection<String> $31 = collectionMap.get(31);

        int length42 = elementLength($42);
        int length31 = elementLength($31);



        return messages.stream()
                .filter(message -> matches(message, $42, length42, $31, length31))
                .peek(System.out::println)
                .count();
    }

                private boolean matches(String message, Collection<String> $42, int length42,
                        Collection<String> $31, int length31) {
                    for (int i = 0; i < message.length() - length42; i += length42) {
                        if (!$42.contains(message.substring(i, i + length42))) {
                            return false;
                        } else if (matches11(message.substring(i + length42), $42, length42, $31, length31))
                            return true;
                    }
                    return false;
                }

                private boolean matches11(String message, Collection<String> $42, int length42,
                        Collection<String> $31, int length31) {
                    return message.isEmpty() || (message.length() >= length42 + length31 && $42.contains(
                            message.substring(0, length42)) && $31.contains(message.substring(message.length() - length31))
                            && matches11(message.substring(length42, message.length() - length31), $42,
                            length42, $31, length31));
                }

    private int elementLength(Collection<String> collection) {
        return collection.stream().mapToInt(String::length).min().orElse(-1);
    }

    private Collection<String> possibilities(Collection<String> coll1, Collection<String> coll2) {
        return coll1.stream()
                .flatMap(str -> coll2.stream().map(line -> str + line))
                .collect(Collectors.toSet());
    }

    @SafeVarargs private Collection<String> combine(Collection<String>... collections) {
        return Stream.of(collections).flatMap(Collection::stream).collect(Collectors.toSet());
    }

}
