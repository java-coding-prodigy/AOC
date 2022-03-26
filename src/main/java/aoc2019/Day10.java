package aoc2019;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day10 {
    public static void main(String[] args) {
        String input = getInput();
        Set<Point> grid = getGrid(input);
        Map<Point, Integer> result = grid.stream()
                .collect(Collectors.toMap(Function.identity(), point -> visible(grid, point)));
        //printMap(result);
        Map.Entry<Point, Integer> tuple = result.entrySet()
                .stream()
                .max(Comparator.comparingInt(Map.Entry::getValue))
                .orElseThrow();
        int part1 = tuple.getValue();
        System.out.println("Part 1: " + part1);
        Point station = tuple.getKey();
        List<Point> vaporized = new ArrayList<>();
        SortedMap<Double, SortedSet<Point>> angleMap = new TreeMap<>(Comparator.naturalOrder());
        angleMap.putAll(grid.stream()
                .collect(Collectors.groupingBy(p -> angle((p.y - station.y), (p.x - station.x)),
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparingInt(
                                p -> (int) station.distanceSq(p.x, p.y)))))));



        while (!angleMap.values().stream().allMatch(Collection::isEmpty)) {
            angleMap.forEach((k, set) -> {
                if (set.isEmpty())
                    return;
                Point first = set.first();
                vaporized.add(first);
                set.remove(first);
            });

        }

        Point p = vaporized.get(200 - 1);
        int part2 = p.x * 100 + p.y;
        System.out.println("Part 2: " + part2);

    }

    private static double angle(double y, double x) {
        double deg = Math.toDegrees(Math.atan2(-y, x));

        if (deg <= 90 && deg >= 0) {
            deg = Math.abs(deg - 90);
        } else if (deg < 0) {
            deg = Math.abs(deg) + 90;
        } else {
            deg = 450 - deg;
        }

        return deg;
    }

    private static boolean inRange(double start, double end, double key) {
        return (start >= key && end < key);
    }

    private static <V> void printMap(Map<Point, V> grid) {
        StringBuilder sb = new StringBuilder();
        int maxX = grid.keySet().stream().mapToInt(p -> p.x).max().orElse(0);
        int maxY = grid.keySet().stream().mapToInt(p -> p.y).max().orElse(0);
        for (int y = 0; y <= maxY; y++) {
            for (int x = 0; x <= maxX; x++) {
                Object value = grid.get(new Point(x, y));
                if (value == null)
                    value = ' ';
                sb.append(value);
            }
            sb.append('\n');
        }
        System.out.println(sb);
    }

    private static int visible(Set<Point> grid, Point point) {
        return (int) grid.stream()
                .filter(p -> !point.equals(p))
                .mapToDouble(p -> Math.atan2((p.y - point.y), (p.x - point.x)))
                .distinct()
                .count();
    }

    private static Set<Point> getGrid(String input) {
        Set<Point> grid = new HashSet<>();
        String[] rows = input.split("\n");
        for (int y = 0; y < rows.length; y++) {
            char[] row = rows[y].trim().toCharArray();
            for (int x = 0; x < row.length; x++) {
                if (row[x] == '#')
                    grid.add(new Point(x, y));
            }
        }
        return grid;
    }

    private static String getInput() {
        return """
                .#..#..#..#...#..#...###....##.#....
                              .#.........#.#....#...........####.#
                              #..##.##.#....#...#.#....#..........
                              ......###..#.#...............#.....#
                              ......#......#....#..##....##.......
                              ....................#..............#
                              ..#....##...#.....#..#..........#..#
                              ..#.#.....#..#..#..#.#....#.###.##.#
                              .........##.#..#.......#.........#..
                              .##..#..##....#.#...#.#.####.....#..
                              .##....#.#....#.......#......##....#
                              ..#...#.#...##......#####..#......#.
                              ##..#...#.....#...###..#..........#.
                              ......##..#.##..#.....#.......##..#.
                              #..##..#..#.....#.#.####........#.#.
                              #......#..........###...#..#....##..
                              .......#...#....#.##.#..##......#...
                              .............##.......#.#.#..#...##.
                              ..#..##...#...............#..#......
                              ##....#...#.#....#..#.....##..##....
                              .#...##...........#..#..............
                              .............#....###...#.##....#.#.
                              #..#.#..#...#....#.....#............
                              ....#.###....##....##...............
                              ....#..........#..#..#.......#.#....
                              #..#....##.....#............#..#....
                              ...##.............#...#.....#..###..
                              ...#.......#........###.##..#..##.##
                              .#.##.#...##..#.#........#.....#....
                              #......#....#......#....###.#.....#.
                              ......#.##......#...#.#.##.##...#...
                              ..#...#.#........#....#...........#.
                              ......#.##..#..#.....#......##..#...
                              ..##.........#......#..##.#.#.......
                              .#....#..#....###..#....##..........
                              ..............#....##...#.####...##.""";
    }
}
