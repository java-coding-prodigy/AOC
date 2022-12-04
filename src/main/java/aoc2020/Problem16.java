package aoc2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Problem16 implements AoC2020Puzzle {


    List<String> fields = new ArrayList<>();
    Map<String, List<Integer>> fieldRanges;
    String[] fieldNames;
    String myTicket;
    List<String> otherTickets;
    Set<Integer> validFields;

    public Problem16() throws IOException {
        List<String> input =
                Files.readAllLines(Path.of("C:\\Users\\Abc\\IdeaProjects\\AOC2021\\src\\main\\resources\\2020\\Tickets.txt"));
        int i;
        for (i = 0; ; i++) {
            if (input.get(i).isEmpty())
                break;
            fields.add(input.get(i));
        }
        i += 2;
        myTicket = input.get(i);
        otherTickets = input.subList(i + 3, input.size());
        fieldNames = fields.stream().map(field -> field.split(":")[0]).toArray(String[]::new);
        fieldRanges = fields.stream()
                .collect(Collectors.toMap(line -> line.split(":")[0],
                        line -> Arrays.stream(line.split(" "))
                                .filter(str -> !str.matches("[a-z]+:?") && !str.equals("or"))
                                .flatMap(range -> {
                                    String[] split = range.split("-");
                                    return IntStream.rangeClosed(Integer.parseInt(split[0]),
                                            Integer.parseInt(split[1])).boxed();
                                })
                                .toList()));
        validFields = fieldRanges.values()
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    public static void main(String[] args) throws IOException {
        System.out.println(new Problem16().part2());
    }

    @Override public Object part1() throws IOException {


        return otherTickets.stream()
                .flatMapToInt(line -> Arrays.stream(line.split(",")).mapToInt(Integer::parseInt))
                .filter(i -> !validFields.contains(i))
                .sum();


    }

    @Override public Object part2() throws IOException {
        List<int[]> tickets = otherTickets.stream()
                .map(line -> Arrays.stream(line.split(",")).map(Integer::valueOf).toList())
                .filter(validFields::containsAll)
                .map(list -> list.stream().mapToInt(Integer::intValue).toArray())
                .toList();
        int[] myTicket = Arrays.stream(this.myTicket.split(",")).mapToInt(Integer::parseInt)
                .toArray();
        Map<Integer, Integer> indexMapping = new HashMap<>();
        List<Integer> indicesLeft =
                new ArrayList<>(IntStream.range(0, myTicket.length).boxed().toList());
        while (!indicesLeft.isEmpty()) {
            System.out.println(indicesLeft.size());
            List<Integer> uncomputable = new ArrayList<>();
            for (int i : indicesLeft) {
                List<Integer> currentFields = tickets.stream().map(array -> array[i]).toList();
                var possibilities = fieldRanges.entrySet()
                        .stream()
                        .filter(entry -> entry.getValue().containsAll(currentFields))
                        .map(Map.Entry::getKey)
                        .toList();

                if (possibilities.size() == 1) {
                    String fieldName = possibilities.get(0);
                    fieldRanges.remove(fieldName);
                    for (int j = 0; j < fieldNames.length; j++) {
                        if (fieldNames[j].equals(fieldName)) {
                            indexMapping.put(j, i);
                        }
                    }
                } else {
                    uncomputable.add(i);
                }
            }
            indicesLeft = uncomputable;
        }
        System.out.println(indexMapping);
        int[] properTicket = new int[myTicket.length];
        for (int i = 0; i < myTicket.length; i++) {
            properTicket[i] = myTicket[indexMapping.get(i)];
        }
        return Arrays.stream(properTicket)
                .limit(6)
                .mapToLong(Long::valueOf)
                .reduce(1L, Math::multiplyExact);
    }
}
