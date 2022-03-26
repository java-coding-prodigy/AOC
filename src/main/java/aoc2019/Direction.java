package aoc2019;

import java.awt.*;
import java.util.Arrays;
import java.util.stream.Stream;

public enum Direction {

    NORTH(1), SOUTH(2), EAST(3), WEST(4);


    final int value;

    Direction(int value) {
        this.value = value;
    }

    public Point move(Point other) {
        return switch (this) {
            case NORTH -> new Point(other.x, other.y + 1);
            case SOUTH -> new Point(other.x, other.y - 1);
            case EAST -> new Point(other.x + 1, other.y);
            case WEST -> new Point(other.x - 1, other.y);
        };
    }

    public static Stream<Point> neighbours(Point p){
        return Arrays.stream(values()).map(dir -> dir.move(p));
    }

    public Point backtrack(Point other) {
        return switch (this) {
            case NORTH -> new Point(other.x, other.y - 1);
            case SOUTH -> new Point(other.x, other.y + 1);
            case EAST -> new Point(other.x - 1, other.y);
            case WEST -> new Point(other.x + 1, other.y);
        };
    }

    int reverse() {
        return ((value - 1) ^ 1) + 1;
    }

}
