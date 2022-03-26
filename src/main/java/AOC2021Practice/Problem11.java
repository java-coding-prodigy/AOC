package AOC2021Practice;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Stream;

public class Problem11 implements AoC2020Puzzle {

    final Map<Point, GridItem> grid = new HashMap<>();



    public Problem11() throws IOException {
        List<String> input = Files.readAllLines(
                Path.of("C:\\Users\\Abc\\IdeaProjects\\AOC2021\\src\\main\\resources\\2020\\Grid.txt"));
        for (int i = 0; i < input.size(); i++) {
            String line = input.get(i);
            for (int j = 0; j < line.length(); j++) {
                grid.put(new Point(j, i),
                        line.charAt(j) == 'L' ? GridItem.EMPTY_SEAT : GridItem.FLOOR);
            }
        }
        Point p = new Point(2, 2);
        System.out.println(
                grid.keySet().stream().filter(point -> areNeighbours(point, p)).toList());
    }

    public static void main(String[] args) throws IOException {
        System.out.println(new Problem11().part2());
    }

    @Override public Object part1() throws IOException {
        Map<Point, GridItem> current = grid;
        int steps = 0;
        Map<Point, GridItem> prev;
        do {
            steps++;
            System.out.println(steps);
            prev = current;
            current = next(current, this::getNeighbours,4);
            //System.out.println(gridToString(current));
        } while (!prev.equals(current));
        return current.values().stream().filter(GridItem.OCCUPIED_SEAT::equals).count();
    }

    private String gridToString(Map<Point, GridItem> grid) {
        StringBuilder sb = new StringBuilder();
        int maxX = grid.keySet().stream().mapToInt(p -> p.x).max().orElse(-1);
        int maxY = grid.keySet().stream().mapToInt(p -> p.y).max().orElse(-1);
        for (int y = 0; y <= maxY; y++) {
            for (int x = 0; x <= maxX; x++) {
                sb.append(grid.get(new Point(x, y)));
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    private Stream<GridItem> getNeighbours(Point p, Map<Point, GridItem> grid) {
        return grid.entrySet()
                .stream()
                .filter(entry -> areNeighbours(entry.getKey(), p))
                .map(Map.Entry::getValue);
    }

    private boolean areNeighbours(Point p1, Point p2) {
        return (Math.abs(p1.x - p2.x) <= 1 && Math.abs(p1.y - p2.y) <= 1) && !p1.equals(p2);
    }

    private Map<Point, GridItem> next(Map<Point, GridItem> prev,
            BiFunction<Point, Map<Point, GridItem>, Stream<GridItem>> getNeighbours,int num) {
        Map<Point, GridItem> next = new HashMap<>();
        prev.forEach((key, value) -> next.put(key, switch (value) {
            case FLOOR -> GridItem.FLOOR;
            case EMPTY_SEAT -> getNeighbours.apply(key, prev)
                    .noneMatch(GridItem.OCCUPIED_SEAT::equals) ?
                    GridItem.OCCUPIED_SEAT :
                    GridItem.EMPTY_SEAT;
            case OCCUPIED_SEAT ->
                    getNeighbours.apply(key, prev).filter(GridItem.OCCUPIED_SEAT::equals).count()
                            >= num ? GridItem.EMPTY_SEAT : GridItem.OCCUPIED_SEAT;
        }));
        return next;
    }



    @Override public Object part2() throws IOException {
        Map<Point, GridItem> current = grid;
        int steps = 0;
        Map<Point, GridItem> prev;
        do {
            steps++;
            System.out.println(steps);
            prev = current;
            current = next(current, this::getNeighbours2,5);
            //System.out.println(gridToString(current));
        } while (!prev.equals(current));
        return current.values().stream().filter(GridItem.OCCUPIED_SEAT::equals).count();
    }

    private Stream<GridItem> getNeighbours2(Point point, Map<Point, GridItem> grid) {
        Collection<GridItem> neighbours = new ArrayList<>();
        for (int y = point.y + 1; true; y++) {
            GridItem item = grid.get(new Point(point.x, y));
            if (item == null)
                break;
            if (item == GridItem.FLOOR)
                continue;
            neighbours.add(item);
            break;
        }
        for (int y = point.y - 1; true; y--) {
            GridItem item = grid.get(new Point(point.x, y));
            if (item == null)
                break;
            if (item == GridItem.FLOOR)
                continue;
            neighbours.add(item);
            break;
        }
        for (int x = point.x + 1; true; x++) {
            GridItem item = grid.get(new Point(x, point.y));
            if (item == null)
                break;
            if (item == GridItem.FLOOR)
                continue;
            neighbours.add(item);
            break;
        }
        for (int x = point.x - 1; true; x--) {
            GridItem item = grid.get(new Point(x, point.y));
            if (item == null)
                break;
            if (item == GridItem.FLOOR)
                continue;
            neighbours.add(item);
            break;
        }
        for (int x = point.x - 1, y = point.y - 1; true; x--, y--) {
            GridItem item = grid.get(new Point(x, y));
            if (item == null)
                break;
            if (item == GridItem.FLOOR)
                continue;
            neighbours.add(item);
            break;
        }
        for (int x = point.x - 1, y = point.y + 1; true; x--, y++) {
            GridItem item = grid.get(new Point(x, y));
            if (item == null)
                break;
            if (item == GridItem.FLOOR)
                continue;
            neighbours.add(item);
            break;
        }
        for (int x = point.x + 1, y = point.y + 1; true; x++, y++) {
            GridItem item = grid.get(new Point(x, y));
            if (item == null)
                break;
            if (item == GridItem.FLOOR)
                continue;
            neighbours.add(item);
            break;
        }
        for (int x = point.x + 1, y = point.y - 1; true; x++, y--) {
            GridItem item = grid.get(new Point(x, y));
            if (item == null)
                break;
            if (item == GridItem.FLOOR)
                continue;
            neighbours.add(item);
            break;
        }
        assert(!neighbours.contains(GridItem.FLOOR));
        return neighbours.stream();
    }

    private enum GridItem {
        FLOOR('.'), EMPTY_SEAT('L'), OCCUPIED_SEAT('#');

        final char display;

        GridItem(char display) {
            this.display = display;
        }

        @Override public String toString() {
            return String.valueOf(display);
        }
    }

}
