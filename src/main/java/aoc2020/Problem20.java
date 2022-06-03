package AOC2021Practice;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Problem20 implements AoC2020Puzzle {

    Map<Integer, String> tiles;

    public Problem20() throws IOException {
        this.tiles = Arrays.stream(String.join("\n", Files.readAllLines(
                                Path.of("C:\\Users\\Abc\\IdeaProjects\\AOC2021\\src\\main\\resources\\2020\\Tiles.txt")))
                        .split("\n\n"))
                .collect(Collectors.toMap(lines -> Integer.valueOf(lines.substring(5, 9)),
                        lines -> lines.substring(11)));
    }

    public static void main(String[] args) throws IOException {
        System.out.println(new Problem20().part2());
    }

    private Object test() {
        return rotations("""
                123
                456
                789""");
    }

    @Override public Object part1() throws IOException {

        long cornersProduct = 1L;

        for (Map.Entry<Integer, String> entry : tiles.entrySet()) {
            int edgeMatches = 0;
            Collection<String> edges = getFrontEdges(entry.getValue());

            for (String tile : tiles.values()) {
                if (tile.equals(entry.getValue()))
                    continue;
                Collection<String> innerEdges = getEdges(tile);
                if (edges.stream().anyMatch(innerEdges::contains))
                    edgeMatches++;
            }
            if (edgeMatches == 2) {
                cornersProduct *= entry.getKey();
            }
        }

        return cornersProduct;
    }

    @Override public Object part2() throws IOException {
        List<Tile> originalTiles = tiles.entrySet()
                .stream()
                .map(entry -> new Tile(entry.getKey(), entry.getValue()))
                .toList();
        Map<Tile, List<Tile>> matchedTiles = matchTiles(originalTiles);
        Tile firstCorner = firstCorner(originalTiles);
        int rows = (int) Math.sqrt(originalTiles.size());
        Map<Point, Tile> arrangement = new HashMap<>();
        arrangement.put(new Point(0, 0), firstCorner);
        while (arrangement.size() != originalTiles.size()) {
            Map<Point, Tile> temp = new HashMap<>();
            for (var entry : arrangement.entrySet()) {
                Point point = entry.getKey();
                int x = point.x;
                int y = point.y;
                Tile current = entry.getValue();
                List<Tile> neighbours = matchedTiles.get(current);
                Point top = new Point(x, y - 1);
                Point right = new Point(x + 1, y);
                Point bottom = new Point(x, y + 1);
                Point left = new Point(x - 1, y);
                for (Tile neighbour : neighbours) {
                    if (arrangement.containsValue(neighbour) || temp.containsValue(neighbour)) {
                        continue;
                    }
                    List<String> edges = neighbour.allEdges;
                    if (edges.contains(current.top)) {
                        temp.put(top, neighbour.rotations()
                                .stream()
                                .filter(rotation -> rotation.bottom.equals(current.top))
                                .findFirst()
                                .orElseThrow());
                    } else if (edges.contains(current.right)) {
                        temp.put(right, neighbour.rotations()
                                .stream()
                                .filter(rotation -> rotation.left.equals(current.right))
                                .findFirst()
                                .orElseThrow());
                    } else if (edges.contains(current.bottom)) {
                        temp.put(bottom, neighbour.rotations()
                                .stream()
                                .filter(rotation -> rotation.top.equals(current.bottom))
                                .findFirst()
                                .orElseThrow());
                    } else {
                        temp.put(left, neighbour.rotations()
                                .stream()
                                .filter(rotation -> rotation.right.equals(current.left))
                                .findFirst()
                                .orElseThrow());
                    }
                }
            }
            arrangement.putAll(temp);
        }
        List<String> rotations = rotations(getString(arrangement));
        int size = rows * 8;
        int monsterLength = 20;
        for(String grid : rotations){
            String[] array = grid.split("\n");
            int count = 0;
            for(int idx = 2; idx < array.length; idx++){
                String prevPrev = array[idx - 2];
                String prev = array[idx - 1];
                String current = array[idx];
                for(int i = 0; i + monsterLength < size; i++){
                    if(prevPrev.charAt(i + monsterLength - 2) == '#' && prev.substring(i, i + monsterLength).matches("#.{4}##.{4}##.{4}#{3}") && current.substring(i + 1, i + monsterLength - 3).matches("#..#..#..#..#..#")){
                        count++;
                    }
                }/*
                if(array[idx - 1].matches(".*#.{4}##.{4}##.{4}#{3}.*") && array[idx].matches(".+#..#..#..#..#..#.+"))
                    count++;*/
            }
            if(count > 0){
                return grid.chars().filter(ch -> ch == '#').count() - 15L * count;
            }
        }
        return null;
    }

    private String getString(Map<Point, Tile> arrangement) {
        int rows = (int) Math.sqrt(arrangement.size());
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < rows; y++) {
            List<String> row = IntStream.range(0, 8).mapToObj(i -> "").toList();
            for (int x = 0; x < rows; x++) {
                row = concatenate(row, arrangement.get(new Point(x, y)).removeEdges());
            }
            sb.append(String.join("\n", row));
            if (y != rows - 1) {
                sb.append('\n');
            }
        }
        return sb.toString();
    }

    private List<String> concatenate(List<String> list1, List<String> list2) {
        int size = list2.size();
        List<String> result = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            result.add(list1.get(i) + list2.get(i));
        }
        return result;
    }

    private Tile firstCorner(List<Tile> tiles) {
        for (int i = 0; i < tiles.size(); i++) {
            Tile tile = tiles.get(i);
            Set<Integer> matchIndices = new HashSet<>();
            for (int j = 0; j < tiles.size(); j++) {
                if (i == j)
                    continue;
                List<String> edges = tiles.get(j).allEdges;
                if (edges.contains(tile.top)) {
                    matchIndices.add(0);
                } else if (edges.contains(tile.right)) {
                    matchIndices.add(1);
                } else if (edges.contains(tile.bottom)) {
                    matchIndices.add(2);
                } else if (edges.contains(tile.left)) {
                    matchIndices.add(3);
                }
            }
            if (matchIndices.size() == 2) {
                while (!matchIndices.contains(1) || !matchIndices.contains(2)) {
                    tile = tile.rotate();
                    matchIndices =
                            matchIndices.stream().map(e -> (e + 1) % 4).collect(Collectors.toSet());
                }
                return tile;
            }
        }
        throw new IllegalStateException();

    }

    public Map<Tile, List<Tile>> matchTiles(List<Tile> originalTiles) {
        Map<Tile, List<Tile>> matchedTiles = originalTiles.stream()
                .collect(Collectors.toMap(Function.identity(), tile -> new ArrayList<>(4)));
        for (var entry : matchedTiles.entrySet()) {
            Tile tile = entry.getKey();
            List<Tile> neighbours = entry.getValue();
            for (Tile innerTile : matchedTiles.keySet()) {
                if (tile.equals(innerTile)) {
                    continue;
                }
                if (Stream.of(tile.top, tile.bottom, tile.right, tile.left)
                        .anyMatch(innerTile.allEdges::contains)) {
                    neighbours.add(innerTile);
                }
            }
        }
        return matchedTiles;
    }

    private Collection<String> getFrontEdges(String tile) {
        Collection<String> edges = new ArrayList<>();
        edges.add(tile.substring(0, 10));

        edges.add(tile.substring(tile.length() - 10));

        StringBuilder str3 = new StringBuilder();
        StringBuilder str4 = new StringBuilder();
        for (int i = 0; i < tile.length(); i += 11) {
            str3.append(tile.charAt(i));
            str4.append(tile.charAt(i + 9));
        }

        edges.add(str3.toString());

        edges.add(str4.toString());



        return edges;
    }

    private Collection<String> getEdges(String tile) {
        Collection<String> edges = new ArrayList<>();
        String str1 = tile.substring(0, 10);
        edges.add(str1);
        edges.add(new StringBuilder(str1).reverse().toString());

        String str2 = tile.substring(tile.length() - 10);
        edges.add(str2);
        edges.add(new StringBuilder(str2).reverse().toString());

        StringBuilder str3 = new StringBuilder();
        StringBuilder str4 = new StringBuilder();
        for (int i = 0; i < tile.length(); i += 11) {
            str3.append(tile.charAt(i));
            str4.append(tile.charAt(i + 9));
        }

        edges.add(str3.toString());
        edges.add(str3.reverse().toString());

        edges.add(str4.toString());
        edges.add(str4.reverse().toString());



        return edges;
    }

    private List<String> rotations(String data) {
        List<String> rotations = new ArrayList<>();
        rotations.add(data);
        for (int i = 0; i < 3; i++) {
            data = rotate(data);
            rotations.add(data);
        }
        data = flip(data);
        rotations.add(data);
        for (int i = 0; i < 3; i++) {
            data = rotate(data);
            rotations.add(data);
        }
        return rotations;
    }

    private String flip(String data) {
        String[] arr = data.split("\n");
        StringBuilder builder = new StringBuilder();
        for (int i = arr.length - 1; i >= 0; i--) {
            for (int j = 0; j < arr[0].length(); j++) {
                builder.append(arr[i].charAt(j));
            }
            if (i != 0) {
                builder.append('\n');
            }
        }
        return builder.toString();
    }

    private String rotate(String data) {
        String[] arr = data.split("\n");
        int size = arr.length;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < arr.length; j++) {

                builder.append(arr[size - j - 1].charAt(i));
            }
            if (i != arr[0].length() - 1) {
                builder.append('\n');
            }
        }
        return builder.toString();
    }

    private static class Tile {

        int id;
        String data;
        String top;
        String bottom;
        String right;
        String left;


        List<String> allEdges = new ArrayList<>();
        Collection<Tile> rotations;

        public Tile(int id, String data) {
            this.id = id;
            this.data = data;
            this.top = data.substring(0, 10);
            this.bottom = data.substring(data.length() - 10);
            StringBuilder builder1 = new StringBuilder();
            StringBuilder builder2 = new StringBuilder();
            for (int i = 0; i < data.length(); i += 11) {
                builder1.append(data.charAt(i));
                builder2.append(data.charAt(i + 9));
            }

            left = builder1.toString();
            right = builder2.toString();

            allEdges.add(top);
            allEdges.add(new StringBuilder(top).reverse().toString());

            allEdges.add(bottom);
            allEdges.add(new StringBuilder(bottom).reverse().toString());

            allEdges.add(left);
            allEdges.add(builder1.reverse().toString());

            allEdges.add(right);
            allEdges.add(builder2.reverse().toString());


        }

        private Collection<Tile> initializeRotations() {
            rotations = new ArrayList<>();
            rotations.add(this);
            Tile tile = this;
            for (int i = 0; i < 3; i++) {
                tile = tile.rotate();
                rotations.add(tile);
            }
            tile = tile.flip();
            rotations.add(tile);
            for (int i = 0; i < 3; i++) {
                tile = tile.rotate();
                rotations.add(tile);
            }
            return rotations;
        }

        public Collection<Tile> rotations() {

            return rotations == null ? initializeRotations() : rotations;
        }

        public Tile flip() {
            String[] arr = data.split("\n");
            StringBuilder builder = new StringBuilder();
            for (int i = arr.length - 1; i >= 0; i--) {
                for (int j = 0; j < arr[0].length(); j++) {
                    builder.append(arr[i].charAt(j));
                }
                if (i != 0) {
                    builder.append('\n');
                }
            }
            return new Tile(id, builder.toString());
        }

        public Tile rotate() {
            String[] arr = data.split("\n");
            int size = arr.length;
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < arr.length; j++) {

                    builder.append(arr[size - j - 1].charAt(i));
                }
                if (i != arr[0].length() - 1) {
                    builder.append('\n');
                }
            }
            return new Tile(id, builder.toString());
        }

        public List<String> removeEdges() {
            String[] arr = data.split("\n");
            int size = arr.length - 1;
            List<String> list = new ArrayList<>();
            for (int i = 1; i < size; i++) {
                StringBuilder sb = new StringBuilder();
                for (int j = 1; j < size; j++) {
                    sb.append(arr[i].charAt(j));
                }
                list.add(sb.toString());
            }
            return list;
        }

        @Override public boolean equals(Object o) {
            if (this == o)
                return true;
            if (!(o instanceof Tile tile))
                return false;
            return Objects.equals(id, tile.id);
        }

        @Override public int hashCode() {
            return Objects.hash(id);
        }

        @Override public String toString() {
            return data;
        }
    }


}
