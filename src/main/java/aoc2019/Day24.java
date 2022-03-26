package aoc2019;

import kotlin.Pair;

import java.awt.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day24 {

    public static void main(String[] args) {
        int part1 = part1();
        System.out.println("Part 1: " + part1);
        int part2 = part2();
        System.out.println("Part 2: " + part2);
    }

    private static int part1() {
        Map<Point, Boolean> grid = getGrid();
        Set<String> hashes = new HashSet<>();
        while (hashes.add(hash(grid))) {
            grid = nextState(grid);
        }

        return grid.keySet().stream().filter(grid::get).mapToInt(p -> 1 << (p.y * 5 + p.x)).sum();
    }

    private static Map<Point, Boolean> getGrid() {
        Map<Point, Boolean> grid = new HashMap<>();
        String[] lines = input().split("\n");
        for (int y = 0; y < lines.length; y++) {
            for (int x = 0; x < lines[y].length(); x++) {
                grid.put(new Point(x, y), lines[y].charAt(x) == '#');
            }
        }
        return grid;
    }

    private static int part2() {

        Map<Integer, Map<Point, Boolean>> grid = new TreeMap<>(Comparator.naturalOrder());
        for (int i1 = -100; i1 <= 100; i1++) {
            grid.put(i1, emptyGrid());
        }
        grid.put(0, getGrid());

        for (int i = 0; i < 200; i++) {
            grid = nextGrids(grid);
        }

        //printMap(grid);

        return (int) grid.values()
                .stream()
                .map(Map::values)
                .flatMap(Collection::stream)
                .filter(Boolean::booleanValue)
                .count();
    }

    private static Map<Integer, Map<Point, Boolean>> nextGrids(
            Map<Integer, Map<Point, Boolean>> grid) {
        Map<Integer, Map<Point, Boolean>> next = new TreeMap<>();

        grid.forEach((depth, map) -> {
            Map<Point, Boolean> newMap = new HashMap<>();
            next.put(depth, newMap);
            map.forEach((k, v) -> {
                long count = onNeighbours(depth, k, grid);
                newMap.put(k, count == 1 || (count == 2 && !v));
            });
        });

        return next;
    }

    private static int onNeighbours(int depth, Point current,
            Map<Integer, Map<Point, Boolean>> grid) {

        if(current.x == 2 && current.y == 2){
            return -1;
        }

        Collection<Pair<Integer, Point>> neighbours = Direction.neighbours(current)
                .map(p -> new Pair<>(depth, p))
                .collect(Collectors.toCollection(ArrayList::new));

        if (current.x == 0) {
            neighbours.add(new Pair<>(depth - 1, new Point(1, 2)));
        }
        if (current.y == 0) {
            neighbours.add(new Pair<>(depth - 1, new Point(2,1)));
        }
        if (current.x == 4) {
            neighbours.add(new Pair<>(depth - 1, new Point(3, 2)));
        }
        if (current.y == 4) {
            neighbours.add(new Pair<>(depth - 1, new Point(2, 3)));
        }
        if (current.x == 2 && current.y == 1) {
            IntStream.range(0, 5)
                    .mapToObj(x -> new Pair<>(depth + 1, new Point(x, 0)))
                    .forEach(neighbours::add);
        }
        if (current.x == 1 && current.y == 2) {
            IntStream.range(0, 5)
                    .mapToObj(y -> new Pair<>(depth + 1, new Point(0, y)))
                    .forEach(neighbours::add);
        }
        if (current.x == 2 && current.y == 3) {
            IntStream.range(0, 5)
                    .mapToObj(x -> new Pair<>(depth + 1, new Point(x, 4)))
                    .forEach(neighbours::add);
        }
        if (current.x == 3 && current.y == 2) {
            IntStream.range(0, 5)
                    .mapToObj(y -> new Pair<>(depth + 1, new Point(4, y)))
                    .forEach(neighbours::add);
        }

        return (int) neighbours.stream()
                .map(pair -> grid.getOrDefault(pair.getFirst(),Collections.emptyMap())
                        .get(pair.getSecond()))
                .filter(bool -> bool != null && bool)
                .count();
    }

    private static Map<Point, Boolean> emptyGrid() {
        return IntStream.range(0, 5)
                .mapToObj(x -> IntStream.range(0, 5).mapToObj(y -> new Point(x, y)))
                .flatMap(Function.identity())
                .collect(Collectors.toMap(Function.identity(), p -> Boolean.FALSE));
    }

    private static String input() {

        return """
                #####
                .....
                ....#
                #####
                .###.""";
    }

    private static void printMap(Map<Integer, Map<Point, Boolean>> grids) {

        for (int depth = -5; depth <= 5; depth++) {
            Map<Point, Boolean> grid = grids.get(depth);
            System.out.println("Depth = " + depth);
            for (int y = 0; y < 5; y++) {
                for (int x = 0; x < 5; x++) {
                    System.out.print(grid.get(new Point(x, y)) ? '#' : '.');
                }
                System.out.println();
            }
        }
        System.out.println();
    }

    private static Map<Point, Boolean> nextState(Map<Point, Boolean> grid) {
        Map<Point, Boolean> next = new HashMap<>();
        grid.forEach((k, v) -> {
            long count = Direction.neighbours(k)
                    .map(grid::get)
                    .filter(bool -> bool != null && bool)
                    .count();
            if (v) {
                next.put(k, count == 1);
            } else {
                next.put(k, count == 1 || count == 2);
            }

        });
        return next;
    }

    private static String hash(Map<Point, Boolean> grid) {
        return grid.values().stream().map(bool -> bool ? "#" : ".").collect(Collectors.joining(""));
    }

}
