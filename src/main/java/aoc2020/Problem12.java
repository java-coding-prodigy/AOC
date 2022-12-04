package aoc2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Problem12 implements AoC2020Puzzle {

    List<String> input;

    public Problem12() throws IOException {
        input = Files.readAllLines(
                Path.of("C:\\Users\\Abc\\IdeaProjects\\AOC2021\\src\\main\\resources\\2020\\Movements.txt"));
    }


    public static void main(String[] args) throws IOException {
        System.out.println(new Problem12().part2());
    }


    @Override public Object part1() throws IOException {
        Ship ship = new Ship(true);
        for (String instruction : input)
            ship.run(instruction);
        return ship.manhattan();
    }

    @Override public Object part2() throws IOException {
        Ship ship = new Ship(false);
        for (String instruction : input)
            ship.run(instruction);
        return ship.manhattan();
    }


    private static class Ship {

        private char direction = 'E';
        private int x = 0;
        private int y = 0;
        private final boolean part1;
        private int dx = 10;
        private int dy = 1;

        Ship(boolean part1) {
            this.part1 = part1;
        }


        void run(String instruction) {
            char dir = instruction.charAt(0);
            int num = Integer.parseInt(instruction.substring(1));
            switch (dir) {
                case 'N' -> moveNorth(num);
                case 'E' -> moveEast(num);
                case 'S' -> moveSouth(num);
                case 'W' -> moveWest(num);
                case 'F' -> moveForward(num);
                case 'R' -> turnRight(num);
                case 'L' -> turnLeft(num);
            }
        }

        int manhattan() {
            return Math.abs(x) + Math.abs(y);
        }

        private void moveNorth(int num) {
            if (part1)
                y += num;
            else
                dy += num;
        }

        private void moveEast(int num) {
            if (part1)
                x += num;
            else
                dx += num;
        }

        private void moveSouth(int num) {
            if (part1)
                y -= num;
            else
                dy -= num;
        }

        private void moveWest(int num) {
            if (part1)
                x -= num;
            else
                dx -= num;
        }

        private void moveForward(int num) {
            if (part1)
                switch (direction) {
                    case 'N' -> moveNorth(num);
                    case 'E' -> moveEast(num);
                    case 'S' -> moveSouth(num);
                    case 'W' -> moveWest(num);
                }
            else
                for (int i = 0; i < num; i++) {
                    x += dx;
                    y += dy;
                }
        }

        private void turnLeft(int deg) {
            if (part1)
                for (int i = 0; i < deg; i += 90)
                    direction = switch (direction) {
                        case 'N' -> 'W';
                        case 'E' -> 'N';
                        case 'S' -> 'E';
                        case 'W' -> 'S';
                        default -> throw new IllegalStateException();
                    };
            else
                for (int i = 0; i < deg; i += 90) {
                    int temp = dx;
                    dx = -dy;
                    dy = temp;
                }
        }

        private void turnRight(int deg) {
            if (part1)
                for (int i = 0; i < deg; i += 90)
                    direction = switch (direction) {
                        case 'N' -> 'E';
                        case 'E' -> 'S';
                        case 'S' -> 'W';
                        case 'W' -> 'N';
                        default -> throw new IllegalStateException();
                    };
            else
                for (int i = 0; i < deg; i += 90) {
                    int temp = dx;
                    dx = dy;
                    dy = -temp;

                }
        }
    }
}
