package aoc2019;

import java.awt.*;
import java.util.List;
import java.util.*;

public class Day11 {
    private static final String input =
            "3,8,1005,8,309,1106,0,11,0,0,0,104,1,104,0,3,8,102,-1,8,10,101,1,10,10,4,10,1008,8,1,10,4,10,1001,8,0,29,3,8,102,-1,8,10,101,1,10,10,4,10,1008,8,0,10,4,10,102,1,8,51,3,8,102,-1,8,10,1001,10,1,10,4,10,108,0,8,10,4,10,1002,8,1,72,1,1104,8,10,2,1105,15,10,2,1106,0,10,3,8,1002,8,-1,10,1001,10,1,10,4,10,1008,8,1,10,4,10,101,0,8,107,3,8,102,-1,8,10,1001,10,1,10,4,10,108,1,8,10,4,10,101,0,8,128,2,6,8,10,3,8,102,-1,8,10,101,1,10,10,4,10,1008,8,0,10,4,10,102,1,8,155,1006,0,96,2,108,10,10,1,101,4,10,3,8,1002,8,-1,10,101,1,10,10,4,10,1008,8,0,10,4,10,1002,8,1,188,2,1,5,10,3,8,102,-1,8,10,101,1,10,10,4,10,1008,8,0,10,4,10,102,1,8,214,2,6,18,10,1006,0,78,1,105,1,10,3,8,1002,8,-1,10,1001,10,1,10,4,10,1008,8,1,10,4,10,102,1,8,247,2,103,8,10,2,1002,10,10,2,106,17,10,1,1006,15,10,3,8,102,-1,8,10,101,1,10,10,4,10,1008,8,1,10,4,10,101,0,8,285,1,1101,18,10,101,1,9,9,1007,9,992,10,1005,10,15,99,109,631,104,0,104,1,21102,387507921664,1,1,21102,1,326,0,1106,0,430,21102,932826591260,1,1,21102,337,1,0,1106,0,430,3,10,104,0,104,1,3,10,104,0,104,0,3,10,104,0,104,1,3,10,104,0,104,1,3,10,104,0,104,0,3,10,104,0,104,1,21101,206400850983,0,1,21101,0,384,0,1105,1,430,21102,3224464603,1,1,21102,395,1,0,1106,0,430,3,10,104,0,104,0,3,10,104,0,104,0,21102,838433657700,1,1,21102,418,1,0,1106,0,430,21101,825012007272,0,1,21101,429,0,0,1106,0,430,99,109,2,21202,-1,1,1,21101,40,0,2,21101,461,0,3,21102,1,451,0,1105,1,494,109,-2,2105,1,0,0,1,0,0,1,109,2,3,10,204,-1,1001,456,457,472,4,0,1001,456,1,456,108,4,456,10,1006,10,488,1102,1,0,456,109,-2,2106,0,0,0,109,4,1202,-1,1,493,1207,-3,0,10,1006,10,511,21101,0,0,-3,21202,-3,1,1,21201,-2,0,2,21102,1,1,3,21102,1,530,0,1106,0,535,109,-4,2106,0,0,109,5,1207,-3,1,10,1006,10,558,2207,-4,-2,10,1006,10,558,22101,0,-4,-4,1106,0,626,22102,1,-4,1,21201,-3,-1,2,21202,-2,2,3,21101,0,577,0,1106,0,535,22102,1,1,-4,21101,1,0,-1,2207,-4,-2,10,1006,10,596,21102,0,1,-1,22202,-2,-1,-2,2107,0,-3,10,1006,10,618,21201,-1,0,1,21102,618,1,0,105,1,493,21202,-2,-1,-2,22201,-4,-2,-4,109,-5,2105,1,0";

    private static Iterator<Integer> iterator(int i) {
        return new Iterator<>() {
            @Override public boolean hasNext() {
                return true;
            }

            @Override public Integer next() {
                return i;
            }
        };
    }

    public static void main(String[] args) {
        long[] memory = Arrays.stream(input.split(",")).mapToLong(Long::parseLong).toArray();

        Map<Point, Integer> points = getPaintedMap(memory,0);
        int part1 = points.size();
        System.out.println("Part 1: " + part1);
        points = getPaintedMap(memory,1);

        int minX = points.keySet().stream().mapToInt(p -> p.x).min().orElseThrow();
        int maxX = points.keySet().stream().mapToInt(p -> p.x).max().orElseThrow();
        int minY = points.keySet().stream().mapToInt(p -> p.y).min().orElseThrow();
        int maxY = points.keySet().stream().mapToInt(p -> p.y).max().orElseThrow();

        StringBuilder sb = new StringBuilder();

        for(int y = minY; y <= maxY; y++){

            for(int x = minX; x <= maxX; x++){

                Integer value = points.get(new Point(x,y));
                sb.append(value != null && value == 1 ? '▒' : ' ');

            }

            sb.append('\n');
        }

        System.out.println("Part 2:");

        System.out.println(sb);
    }

    private static Map<Point, Integer> getPaintedMap(long[] memory,int initial) {
        Map<Point, Integer> points = new HashMap<>();
        Point current = new Point(0, 0);
        points.put(current, initial);
        char dir = 'N';


        IntCode comp = new IntCode(memory);
        while (true) {


            List<Integer> output = comp.runTill2Output(iterator(points.getOrDefault(current, 0)));
            if (output.size() < 2) {
                break;
            }
            dir = switch (output.get(1)) {
                case 0 -> turnLeft(dir);
                case 1 -> turnRight(dir);
                default -> throw new IllegalStateException();
            };
            Point next = next(current, dir);

            points.put(current, output.get(0));

            current = next;
        }
        return points;
    }

    private static Point next(Point current, char dir) {
        return switch (dir) {
            case 'N' -> new Point(current.x, current.y - 1);
            case 'E' -> new Point(current.x + 1, current.y);
            case 'S' -> new Point(current.x, current.y + 1);
            case 'W' -> new Point(current.x - 1, current.y);
            default -> throw new IllegalArgumentException();
        };
    }

    private static char turnLeft(char dir) {
        return switch (dir) {
            case 'N' -> 'W';
            case 'E' -> 'N';
            case 'S' -> 'E';
            case 'W' -> 'S';
            default -> throw new IllegalArgumentException();
        };
    }

    private static char turnRight(char dir) {
        return switch (dir) {
            case 'N' -> 'E';
            case 'E' -> 'S';
            case 'S' -> 'W';
            case 'W' -> 'N';
            default -> throw new IllegalArgumentException();
        };
    }
}
