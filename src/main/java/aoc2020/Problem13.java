package aoc2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Problem13 implements AoC2020Puzzle {

    List<String> input;

    public Problem13() throws IOException {
        input = Files.readAllLines(
                Path.of("C:\\Users\\Abc\\IdeaProjects\\AOC2021\\src\\main\\resources\\2020\\Buses.txt"));
    }

    public static void main(String[] args) throws IOException {
        System.out.println(new Problem13().part2());
    }

    @Override public Object part1() throws IOException {
        int time = Integer.parseInt(input.get(0));
        List<Integer> busIds = Arrays.stream(input.get(1).split(","))
                .filter(str -> !str.equals("x"))
                .map(Integer::valueOf)
                .toList();
        for (int i = 0; ; i++) {
            int depTime = time + i;
            Optional<Integer> opt = busIds.stream().filter(id -> depTime % id == 0).findAny();
            if (opt.isPresent())
                return i * opt.get();
        }
        //return null;
    }

    @Override public Object part2() throws IOException {
        Map<Integer, Integer> map = new HashMap<>();
        String[] array = input.get(1).split(",");
        for (int i = 0; i < array.length; i++) {
            if (!array[i].equals("x")) {
                int id = Integer.parseInt(array[i]);
                map.put(id, (-(i + 1) % id + id) % id);
            }
        }

        return chineseRemainderTheorem(map) + 1;
    }

    long chineseRemainderTheorem(Map<Integer, Integer> map){
        long N = map.keySet().stream().mapToLong(Long::valueOf).reduce(1L,Math::multiplyExact);
        return map.entrySet().stream().mapToLong(
                entry -> {
            int ni = entry.getKey();
            int bi = entry.getValue();
            long Ni = N / ni;
            long xCoeff = Ni % ni;
            long x = 1;
            while((xCoeff * x) % ni != 1){
                x++;
            }
            return x * bi * Ni;
        }).sum() % N;
    }
}
