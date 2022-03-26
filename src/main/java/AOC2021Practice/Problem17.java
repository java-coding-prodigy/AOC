package AOC2021Practice;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Problem17 implements AoC2020Puzzle {

    public static void main(String[] args) throws IOException {
        System.out.println(new Problem17().part2());
    }


    @Override public Object part1() throws IOException {
        List<String> input = Files.readAllLines(Path.of("src/main/resources/2020/Day17.txt"));
        Cubes cubes = new Cubes(input);
        for (int i = 0; i < 6; i++) {
            cubes = new Cubes(cubes);
        }
        System.out.println();
        return (int) cubes.cubes.stream().filter(Cube::isActive).count();
    }




    public static final class Cube {
        private final int x;
        private final int y;
        private final int z;
        private boolean active;

        public Cube(int x, int y, int z, boolean active) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.active = active;
        }

        public Cube(int x, int y, int z) {
            this(x, y, z, false);
        }


        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public boolean isNeighbourOf(int x, int y, int z) {
            return !equals(x, y, z) && Math.abs((this.x - x)) <= 1 && Math.abs((this.y - y)) <= 1
                    && Math.abs((this.z - z)) <= 1;
        }

        public boolean isNeighbourOf(Cube cube) {
            return isNeighbourOf(cube.x, cube.y, cube.z);
        }

        public boolean equals(int x, int y, int z) {
            return this.x == x && this.y == y && this.z == z;
        }

        @Override public boolean equals(Object obj) {
            if (obj == this)
                return true;
            if (!(obj instanceof Cube that))
                return false;
            return equals(that.x, that.y, that.z);
        }

        @Override public int hashCode() {
            return Objects.hash(x, y, z);
        }

        @Override public String toString() {
            return "Cube[" + "x=" + x + ", " + "y=" + y + ", " + "z=" + z + ", active=" + active
                    + ']';
        }
    }


    public static class Cubes {
        static int cycles = 0;
        public Set<Cube> cubes;
        int lowestX = 0;
        int lowestY = 0;
        int lowestZ = 0;
        int highestX;
        int highestY;
        int highestZ = 0;

        public Cubes(Cubes previousState) {
            cubes = new HashSet<>();
            lowestX = previousState.lowestX - 1;
            lowestY = previousState.lowestY - 1;
            lowestZ = previousState.lowestZ - 1;
            highestX = previousState.highestX + 1;
            highestY = previousState.highestY + 1;
            highestZ = previousState.highestZ + 1;
            for (int i = lowestX; i <= highestX; i++) {
                for (int j = lowestY; j <= highestY; j++) {
                    cubes.add(new Cube(i, j, lowestZ));
                    cubes.add(new Cube(i, j, highestZ));
                }
            }
            for (int i = lowestX; i <= highestX; i++) {
                for (int j = lowestZ; j <= highestZ; j++) {
                    cubes.add(new Cube(i, lowestY, j));
                    cubes.add(new Cube(i, highestY, j));
                }
            }
            for (int i = lowestY; i <= highestY; i++) {
                for (int j = lowestZ; j <= highestZ; j++) {
                    cubes.add(new Cube(lowestX, i, j));
                    cubes.add(new Cube(highestX, i, j));
                }
            }
            cubes.add(new Cube(lowestX, lowestY, lowestZ));
            cubes.add(new Cube(highestX, lowestY, lowestZ));
            cubes.add(new Cube(lowestX, highestY, lowestZ));
            cubes.add(new Cube(lowestX, lowestY, highestZ));
            cubes.add(new Cube(highestX, highestY, lowestZ));
            cubes.add(new Cube(highestX, lowestY, highestZ));
            cubes.add(new Cube(lowestX, highestY, highestZ));
            cubes.add(new Cube(highestX, highestY, highestZ));



            var previousCubes = previousState.cubes;
            for (Cube previousCube : previousCubes) {
                var activeNeighbours = previousCubes.stream()
                        .filter(Cube::isActive)
                        .filter(previousCube::isNeighbourOf)
                        .count();
                cubes.add(new Cube(previousCube.x, previousCube.y, previousCube.z,
                        (previousCube.isActive()) ?

                                activeNeighbours == 2 || activeNeighbours == 3 :
                                activeNeighbours == 3));
            }
            cubes.stream()
                    .filter(cube -> !previousCubes.contains(cube))
                    .filter(cube -> previousCubes.stream()
                            .filter(Cube::isActive)
                            .filter(cube::isNeighbourOf)
                            .count() == 3)
                    .forEach(cube -> cube.setActive(true));
            System.out.printf("The %dth cycle has %d active cubes%n ", ++cycles,
                    cubes.stream().filter(Cube::isActive).count());
        }

        public Cubes(List<String> initialState) {
            System.out.println("running the initial constructor");
            cubes = new HashSet<>();
            highestX = initialState.get(0).length() - 1;
            highestY = initialState.size() - 1;
            for (int i = 0; i < initialState.size(); i++) {
                String line = initialState.get(i);
                for (int j = 0; j < line.length(); j++) {
                    cubes.add(new Cube(j, i, 0, line.charAt(j) == '#'));
                }
            }
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            Set<Cube> activeCubes =
                    cubes.stream().filter(Cube::isActive).collect(Collectors.toSet());
            for (int z = lowestZ; z <= highestZ; z++) {
                sb.append("z = ").append(z).append('\n');
                for (int y = lowestY; y <= highestY; y++) {
                    for (int x = lowestX; x <= highestX; x++) {
                        sb.append(activeCubes.contains(new Cube(x, y, z)) ? '#' : '.');
                    }
                    sb.append('\n');
                }
            }
            return sb.toString();
        }
    }

    @Override public Object part2() throws IOException {
        List<String> input = Files.readAllLines(Path.of("src/main/resources/2020/Day17.txt"));
        HyperCubes hyperCubes = new HyperCubes(input);
        for (int i = 1; i <= 6; i++) {
            hyperCubes = new HyperCubes(hyperCubes);

            System.out.printf("The %dth cycle has %d active cubes%n ", i,
                    hyperCubes.cubes.stream().filter(HyperCube::isActive).count());
        }
        System.out.println();
        return (int) hyperCubes.cubes.stream().filter(HyperCube::isActive).count();
    }

    public static final class HyperCube {
        private final int x;
        private final int y;
        private final int z;
        private final int w;
        private boolean active;

        public HyperCube(int x, int y, int z, int w, boolean active) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.w = w;
            this.active = active;
        }

        public HyperCube(int x, int y, int z, int w) {
            this(x, y, z, w, false);
        }


        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public boolean isNeighbourOf(int x, int y, int z, int w) {
            return !equals(x, y, z, w) && Math.abs((this.x - x)) <= 1 && Math.abs((this.y - y)) <= 1
                    && Math.abs((this.z - z)) <= 1 && Math.abs((this.w - w)) <= 1;
        }

        public boolean isNeighbourOf(HyperCube cube) {
            return isNeighbourOf(cube.x, cube.y, cube.z, cube.w);
        }

        public boolean equals(int x, int y, int z, int w) {
            return this.x == x && this.y == y && this.z == z && this.w == w;
        }

        @Override public boolean equals(Object obj) {
            if (obj == this)
                return true;
            if (!(obj instanceof HyperCube that))
                return false;
            return equals(that.x, that.y, that.z, that.w);
        }

        @Override public int hashCode() {
            return Objects.hash(x, y, z);
        }

        @Override public String toString() {
            return "Cube[" + "x=" + x + ", " + "y=" + y + ", " + "z=" + z + ", active=" + active
                    + ']';
        }
    }


    public static final class HyperCubes {


        public Set<HyperCube> cubes;
        int lowestX = 0;
        int lowestY = 0;
        int lowestZ = 0;
        int lowestW = 0;
        int highestX;
        int highestY;
        int highestZ = 0;
        int highestW = 0;

        public HyperCubes(HyperCubes previousState) {
            cubes = new HashSet<>();
            lowestX = previousState.lowestX - 1;
            lowestY = previousState.lowestY - 1;
            lowestZ = previousState.lowestZ - 1;
            lowestW = previousState.lowestZ - 1;
            highestX = previousState.highestX + 1;
            highestY = previousState.highestY + 1;
            highestZ = previousState.highestZ + 1;
            highestW = previousState.highestW + 1;
            cubes.add(new HyperCube(lowestX, lowestY, lowestZ, lowestW));
            cubes.add(new HyperCube(highestX, lowestY, lowestZ, lowestW));
            cubes.add(new HyperCube(lowestX, highestY, lowestZ, lowestW));
            cubes.add(new HyperCube(lowestX, lowestY, highestZ, lowestW));
            cubes.add(new HyperCube(highestX, highestY, lowestZ, lowestW));
            cubes.add(new HyperCube(highestX, lowestY, highestZ, lowestW));
            cubes.add(new HyperCube(lowestX, highestY, highestZ, lowestW));
            cubes.add(new HyperCube(highestX, highestY, highestZ, lowestW));
            cubes.add(new HyperCube(lowestX, lowestY, lowestZ, highestW));
            cubes.add(new HyperCube(highestX, lowestY, lowestZ, highestW));
            cubes.add(new HyperCube(lowestX, highestY, lowestZ, highestW));
            cubes.add(new HyperCube(lowestX, lowestY, highestZ, highestW));
            cubes.add(new HyperCube(highestX, highestY, lowestZ, highestW));
            cubes.add(new HyperCube(highestX, lowestY, highestZ, highestW));
            cubes.add(new HyperCube(lowestX, highestY, highestZ, highestW));
            cubes.add(new HyperCube(highestX, highestY, highestZ, highestW));

            for (int x = lowestX; x <= highestX; x++) {
                for (int y = lowestY; y <= highestY; y++) {
                    for (int z = lowestZ; z <= highestZ; z++) {
                        cubes.add(new HyperCube(x, y, z, lowestW));
                        cubes.add(new HyperCube(x, y, z, highestW));
                    }
                }
            }

            for (int x = lowestX; x <= highestX; x++) {
                for (int y = lowestY; y <= highestY; y++) {
                    for (int w = lowestW; w <= highestW; w++) {
                        cubes.add(new HyperCube(x, y, lowestZ, w));
                        cubes.add(new HyperCube(x, y, highestZ, w));
                    }
                }
            }

            for (int x = lowestX; x <= highestX; x++) {
                for (int z = lowestZ; z <= highestZ; z++) {
                    for (int w = lowestW; w <= highestW; w++) {
                        cubes.add(new HyperCube(x, lowestY, z, w));
                        cubes.add(new HyperCube(x, highestY, z, w));
                    }
                }
            }

            for (int y = lowestY; y <= highestY; y++) {
                for (int z = lowestZ; z <= highestZ; z++) {
                    for (int w = lowestW; w <= highestW; w++) {
                        cubes.add(new HyperCube(lowestX, y, z, w));
                        cubes.add(new HyperCube(highestX, y, z, w));
                    }
                }
            }

            var previousHyperCubes = previousState.cubes;
            for (HyperCube previousHyperCube : previousHyperCubes) {
                var activeNeighbours = previousHyperCubes.stream()
                        .filter(HyperCube::isActive)
                        .filter(previousHyperCube::isNeighbourOf)
                        .count();
                cubes.add(
                        new HyperCube(previousHyperCube.x, previousHyperCube.y, previousHyperCube.z,
                                previousHyperCube.w, (previousHyperCube.isActive()) ?

                                activeNeighbours == 2 || activeNeighbours == 3 :
                                activeNeighbours == 3));
            }
            cubes.stream()
                    .filter(HyperCube -> !previousHyperCubes.contains(HyperCube))
                    .filter(HyperCube -> previousHyperCubes.stream()
                            .filter(Problem17.HyperCube::isActive)
                            .filter(HyperCube::isNeighbourOf)
                            .count() == 3)
                    .forEach(HyperCube -> HyperCube.setActive(true));
        }

        public HyperCubes(List<String> initialState) {
            cubes = new HashSet<>();
            highestX = initialState.get(0).length() - 1;
            highestY = initialState.size() - 1;
            for (int i = 0; i < initialState.size(); i++) {
                String line = initialState.get(i);
                for (int j = 0; j < line.length(); j++) {
                    cubes.add(new HyperCube(j, i, 0, 0, line.charAt(j) == '#'));
                }
            }
        }
    }
}
