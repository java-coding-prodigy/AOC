package aoc2019;


import kotlin.Pair;

import java.awt.Point;
import java.util.List;
import java.util.Queue;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


public class Day20 {



    public static void main(String[] args) {

        String input = """
                             Z L X W       C                \s
                             Z P Q B       K                \s
                  ###########.#.#.#.#######.############### \s
                  #...#.......#.#.......#.#.......#.#.#...# \s
                  ###.#.#.#.#.#.#.#.###.#.#.#######.#.#.### \s
                  #.#...#.#.#...#.#.#...#...#...#.#.......# \s
                  #.###.#######.###.###.#.###.###.#.####### \s
                  #...#.......#.#...#...#.............#...# \s
                  #.#########.#######.#.#######.#######.### \s
                  #...#.#    F       R I       Z    #.#.#.# \s
                  #.###.#    D       E C       H    #.#.#.# \s
                  #.#...#                           #...#.# \s
                  #.###.#                           #.###.# \s
                  #.#....OA                       WB..#.#..ZH
                  #.###.#                           #.#.#.# \s
                CJ......#                           #.....# \s
                  #######                           ####### \s
                  #.#....CK                         #......IC
                  #.###.#                           #.###.# \s
                  #.....#                           #...#.# \s
                  ###.###                           #.#.#.# \s
                XF....#.#                         RF..#.#.# \s
                  #####.#                           ####### \s
                  #......CJ                       NM..#...# \s
                  ###.#.#                           #.###.# \s
                RE....#.#                           #......RF
                  ###.###        X   X       L      #.#.#.# \s
                  #.....#        F   Q       P      #.#.#.# \s
                  ###.###########.###.#######.#########.### \s
                  #.....#...#.....#.......#...#.....#.#...# \s
                  #####.#.###.#######.#######.###.###.#.#.# \s
                  #.......#.......#.#.#.#.#...#...#...#.#.# \s
                  #####.###.#####.#.#.#.#.###.###.#.###.### \s
                  #.......#.....#.#...#...............#...# \s
                  #############.#.#.###.################### \s
                               A O F   N                    \s
                               A A D   M                    \s""";

        Map<String, List<Point>> map = new HashMap<>();

        Map<Point, Boolean> grid = new HashMap<>();
        Point start = null;
        Point end = null;

        String[] lines = input.split("\n");
        for (int y = 0; y < lines.length; y++) {
            for (int x = 0; x < lines[y].length(); x++) {
                Point curr = new Point(x, y);
                char ch = lines[y].charAt(x);
                if (Character.isLetter(ch)) {
                    try {
                        if (Character.isLetter(lines[y].charAt(x + 1))) {
                            String str = "" + ch + lines[y].charAt(x + 1);
                            Point p = new Point(
                                    x == lines[y].length() - 2 || lines[y].charAt(x + 2) == ' ' ?
                                            x - 1 :
                                            x + 2, y);
                            if (str.equals("AA")) {
                                start = p;
                            }
                            if (str.equals("ZZ")) {
                                end = p;
                            }
                            var list = map.computeIfAbsent(str, k -> new ArrayList<>());
                            list.add(p);
                        }
                        if (Character.isLetter(lines[y + 1].charAt(x))) {
                            String str = "" + ch + lines[y + 1].charAt(x);
                            Point p = new Point(x,
                                    y == lines.length - 2 || lines[y + 2].charAt(x) == ' ' ?
                                            y - 1 :
                                            y + 2);
                            if (str.equals("AA")) {
                                start = p;
                            }
                            if (str.equals("ZZ")) {
                                end = p;
                            }
                            var list = map.computeIfAbsent(str, k -> new ArrayList<>());
                            list.add(p);
                        }
                    } catch (IndexOutOfBoundsException ignored) {

                    }
                }
                grid.put(curr, ch == '.');
            }
        }

        Collection<List<Point>> portals = map.values();

        Queue<Point> q = new LinkedList<>();
        Map<Point, Integer> distances = new HashMap<>();
        q.add(start);
        distances.put(start, 0);
        while (!q.isEmpty()) {
            Point current = q.poll();
            movesTo(portals, current, grid).forEach((neighbour, dist) -> {
                int distance = distances.get(current) + dist;
                if (distances.getOrDefault(neighbour, Integer.MAX_VALUE) > distance) {
                    distances.put(neighbour, distance);
                    q.add(neighbour);
                }
            });
        }

        int part1 = distances.get(end);
        System.out.println("Part 1: " + part1);

        int minX = portals.stream().flatMap(List::stream).mapToInt(p -> p.x).min().orElseThrow();
        int maxX = portals.stream().flatMap(List::stream).mapToInt(p -> p.x).max().orElseThrow();
        int minY = portals.stream().flatMap(List::stream).mapToInt(p -> p.y).min().orElseThrow();
        int maxY = portals.stream().flatMap(List::stream).mapToInt(p -> p.y).max().orElseThrow();

        Collection<Point> outerPortals = portals.stream()
                .flatMap(List::stream)
                .filter(p -> p.x == minX || p.x == maxX || p.y == minY || p.y == maxY)
                .collect(Collectors.toSet());


        int part2 = bfs(start, end, portals, grid, outerPortals);
        System.out.println("Part 2: " + part2);

    }

    private static int bfs(Point start, Point end, Collection<List<Point>> portals,
            Map<Point, Boolean> grid, Collection<Point> outerPortals) {
        Map<Pair<Point, Integer>, Integer> ma = new HashMap<>();
        ma.put(new Pair<>(start, 0), 0);

        AtomicInteger ans = new AtomicInteger(-1);
        while (ans.get() <= -1) {

            Map<Pair<Point, Integer>, Integer> temp = new HashMap<>();
            ma.forEach((p1, currDist) -> {



                Point current = p1.getFirst();
                int level = p1.getSecond();

                if (current.equals(end) && level == 0) {
                    ans.set(switch (ans.get()) {
                        case -1 -> -2;
                        case -2 -> -3;
                        case -3 -> currDist;
                        default -> ans.get();
                    });
                    return;
                }
                Map<Point, Pair<Integer, Integer>> m =
                        movesTo(portals, current, grid, level, outerPortals, end, start);
                m.forEach((k, v) -> {
                    if (v.getSecond() >= 0) {
                        temp.put(new Pair<>(k, v.getSecond()), currDist + v.getFirst());
                    }
                });
            });
            ma.clear();
            ma.putAll(temp);
        }
        return ans.get();
    }

    private static Map<Point, Pair<Integer, Integer>> movesTo(Collection<List<Point>> portals,
            Point current, Map<Point, Boolean> grid, int level, Collection<Point> outerPortals,
            Point end, Point start) {

        Map<Point, Pair<Integer, Integer>> map = new HashMap<>();

        portals.forEach(pair -> {

            Point p1 = pair.get(0);

            if ((p1.equals(end) || p1.equals(start)) && level > 1) {
                return;
            }

            if (p1.equals(current)) {
                return;
            }


            int r1 = getDist(grid, current, p1);
            if (pair.size() == 1) {
                if (r1 != Integer.MAX_VALUE) {
                    map.put(p1, new Pair<>(r1, outerPortals.contains(p1) ? 0 : level + 1));
                }
                return;
            }
            Point p2 = pair.get(1);
            if (p2.equals(current)) {
                return;
            }
            int r2 = getDist(grid, current, p2);
            if (r1 < r2) {
                map.put(p2, new Pair<>(r1 + 1, outerPortals.contains(p1) ? level - 1 : level + 1));
            } else if (r1 > r2) {
                map.put(p1, new Pair<>(r2 + 1, outerPortals.contains(p2) ? level - 1 : level + 1));
            }
        });
        return map;
    }


    private static Map<Point, Integer> movesTo(Collection<List<Point>> portals, Point current,
            Map<Point, Boolean> grid) {

        Map<Point, Integer> map = new HashMap<>();

        portals.parallelStream().forEach(pair -> {

            Point p1 = pair.get(0);
            int r1 = getDist(grid, current, p1);
            if (pair.size() == 1) {
                if (r1 != Integer.MAX_VALUE) {
                    map.put(p1, r1);
                }
                return;
            }
            Point p2 = pair.get(1);

            int r2 = getDist(grid, current, p2);
            if (r1 < r2) {
                map.put(p2, r1 + 1);
            } else if (r1 > r2) {
                map.put(p1, r2 + 1);
            }
        });
        return map;
    }

    private static int getDist(Map<Point, Boolean> grid, Point start, Point dest) {
        Queue<Point> q = new LinkedList<>();
        Map<Point, Integer> distances = new HashMap<>();
        q.add(start);
        distances.put(start, 0);
        while (!q.isEmpty()) {
            Point current = q.poll();
            for (Direction dir : Direction.values()) {
                Point neighbour = dir.move(current);
                if (!grid.getOrDefault(neighbour, false)) {
                    continue;
                }
                int distance = distances.get(current) + 1;
                if (distances.getOrDefault(neighbour, Integer.MAX_VALUE) > distance) {
                    distances.put(neighbour, distance);
                    q.add(neighbour);
                }

            }
        }

        return distances.getOrDefault(dest, Integer.MAX_VALUE);
    }

}
