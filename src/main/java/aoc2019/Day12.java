package aoc2019;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class Day12 {

    public static void main(String[] args) {
        String input = getInput();
        List<Moon> moons = Arrays.stream(input.split("\n")).map(Moon::new).toList();
        List<Data> xAxis = moons.stream().map(moon -> new Data(moon.x, moon.xV)).toList();
        List<Data> yAxis = moons.stream().map(moon -> new Data(moon.y, moon.yV)).toList();
        List<Data> zAxis = moons.stream().map(moon -> new Data(moon.z, moon.zV)).toList();
        Set<String> xHashes = new HashSet<>();
        Set<String> yHashes = new HashSet<>();
        Set<String> zHashes = new HashSet<>();
        xHashes.add(hash(xAxis));
        yHashes.add(hash(yAxis));
        zHashes.add(hash(zAxis));
        for (int i = 0; i < 1000; i++) {
            gravity(moons);
            moons.forEach(Moon::velocity);
        }
        int part1 = moons.stream().mapToInt(Moon::energy).sum();
        System.out.println("Part 1: " + part1);

        long x = 0;
        while (true) {
            xAxis = update(xAxis);
            if (!xHashes.add(hash(xAxis))) {
                x++;
                break;
            }
            x++;
        }

        long y = 0;
        while (true) {
            yAxis = update(yAxis);
            if (!yHashes.add(hash(yAxis))) {
                y++;
                break;
            }
            y++;
        }

        long z = 0;
        while (true) {
            zAxis = update(zAxis);
            if (!zHashes.add(hash(zAxis))) {
                z++;
                break;
            }
            z++;
        }
        long part2 = lcm(x, y, z);
        System.out.println("Part 2: " + part2);
    }

    private static long lcm(long x, long y, long z) {
        long lcm = Math.max(Math.max(x,y),z);
        long step = lcm;
        while (lcm % x != 0 || lcm % y  != 0 || lcm % z  != 0) {
            lcm += step;
        }
        return lcm;
    }

    private static String hash(List<Data> data) {
        return data.stream().map(Data::toString).collect(Collectors.joining("&"));
    }

    private static List<Data> update(List<Data> axis) {
        List<Data> next = new ArrayList<>(4);
        for (Data data : axis) {
            int count = 0;
            for (Data other : axis) {
                count += Integer.compare(other.pos, data.pos);
            }
            int vel = data.vel + count;
            next.add(new Data(data.pos + vel, vel));
        }
        return next;
    }

    private static void gravity(List<Moon> moons) {
        moons.forEach(moon -> moons.stream().filter(m -> m != moon).forEach(m -> {
            if (moon.x < m.x) {
                moon.xV++;
                m.xV--;
            }
            if (moon.y < m.y) {
                moon.yV++;
                m.yV--;
            }
            if (moon.z < m.z) {
                moon.zV++;
                m.zV--;
            }
        }));
    }



    private static @NotNull String getInput() {
        return """
                <x=14, y=4, z=5>
                <x=12, y=10, z=8>
                <x=1, y=7, z=-10>
                <x=16, y=-5, z=3>""";
    }


    private static final class Data {
        private final int pos;
        private final int vel;

        private Data(int pos, int vel) {
            this.pos = pos;
            this.vel = vel;
        }

        @Override public String toString() {
                return pos + "_" + vel;
            }

        public int pos() {
            return pos;
        }

        public int vel() {
            return vel;
        }

        @Override public boolean equals(Object obj) {
            if (obj == this)
                return true;
            if (obj == null || obj.getClass() != this.getClass())
                return false;
            var that = (Data) obj;
            return this.pos == that.pos && this.vel == that.vel;
        }

        @Override public int hashCode() {
            return Objects.hash(pos, vel);
        }
    
        }



    private static final class Moon {
        int x;
        int y;
        int z;
        int xV;
        int yV;
        int zV;

        Moon(String line) {
            String[] subs = line.replaceAll("[<xyz=> ]", "").split(",");
            this.x = Integer.parseInt(subs[0]);
            this.y = Integer.parseInt(subs[1]);
            this.z = Integer.parseInt(subs[2]);
            this.xV = 0;
            this.yV = 0;
            this.zV = 0;
        }

        void velocity() {
            x += xV;
            y += yV;
            z += zV;
        }

        int energy() {
            return (Math.abs(x) + Math.abs(y) + Math.abs(z)) * (Math.abs(xV) + Math.abs(yV)
                    + Math.abs(zV));
        }

        @Override public boolean equals(Object obj) {
            if (obj == this)
                return true;
            if (obj == null || obj.getClass() != this.getClass())
                return false;
            var that = (Moon) obj;
            return this.x == that.x && this.y == that.y && this.z == that.z;
        }

        @Override public int hashCode() {
            return Objects.hash(x, y, z);
        }

        @Override public String toString() {
            return "pos=<x=%d, y=%d, z=%d>, vel=<x=%d, y=%d, z=%d>".formatted(x, y, z, xV, yV, zV);
        }
    }

}
