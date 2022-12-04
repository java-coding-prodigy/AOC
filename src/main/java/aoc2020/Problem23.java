package aoc2020;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Fork(value = 2, jvmArgs = {"-Xms2G", "-Xmx2G"})
public class Problem23 implements AoC2020Puzzle {

    List<Integer> circle;

    @Setup
    public void setup(){
        circle = "326519478".chars().mapToObj(ch -> ch - '0').toList();
    }

    public static void main(String[] args) throws RunnerException {

        new Runner(new OptionsBuilder()
                .include(Problem23.class.getSimpleName())
                .forks(1)
                .build()).run();
    }

    private int getDestination(int current, int first, int second, int third, int cups) {
        int res = current - 1;
        while (res == 0 || res == first || res == second || res == third){
            res = res == 1 || res == 0 ? cups : res - 1;
        }
        return res;
    }

    @Benchmark
    @Override public Object part2() {
        List<Integer> circle = new ArrayList<>(this.circle);
        int[] cups = new int[1_000_000];
        int max = circle.stream().mapToInt(Integer::intValue).max().orElse(-1);
        for (int i = 1; i < circle.size(); i++) {
            cups[circle.get(i - 1) - 1] = circle.get(i);
        }
        cups[circle.get(circle.size() - 1) - 1] = max + 1;
        for (int i = max + 1; i < 1_000_000; i++) {
            cups[i - 1] = i + 1;
        }
        cups[999_999] = circle.get(0);
        int current = circle.get(0);
        for (int i = 0; i < 10_000_000; i++) {
            int first = cups[current - 1];
            int second = cups[first - 1];
            int third = cups[second - 1];
            int next = cups[third - 1];
            int destination = getDestination(current, first, second, third, 1_000_000);
            cups[current - 1] = next;
            cups[third - 1] = cups[destination - 1];
            cups[destination - 1] = first;
            current = next;
        }

        int c1 = cups[0];
        long c2 = cups[c1 - 1];


        return c1 * c2;
    }

    @Override public Object part1() {

        List<Integer> circle = new ArrayList<>(this.circle);

        int[] cups = new int[circle.size()];
        for (int i = 1; i < circle.size(); i++) {
            cups[circle.get(i - 1) - 1] = circle.get(i);
        }
        cups[circle.get(circle.size() - 1) - 1] = circle.get(0);
        int current = circle.get(0);
        for (int a = 0; a < 100; a++) {
            circle = new ArrayList<>();
            circle.add(current);
            for (int i = 1; i < cups.length; i++) {
                circle.add(cups[circle.get(i - 1) - 1]);
            }
            int first = cups[current - 1];
            int second = cups[first - 1];
            int third = cups[second - 1];
            int next = cups[third - 1];
            int destination = getDestination(current, first, second, third, 9);
            cups[current - 1] = next;
            cups[third - 1] = cups[destination - 1];
            cups[destination - 1] = first;
            current = next;
        }
        circle = new ArrayList<>();
        circle.add(1);
        for (int i = 1; i < cups.length; i++) {
            circle.add(cups[circle.get(i - 1) - 1]);
        }
        int idx1 = circle.indexOf(1);
        StringBuilder builder = new StringBuilder();
        for (int i = 1; i < circle.size(); i++) {
            builder.append(get(idx1 + i, circle));
        }
        return builder;
    }



    private <T> T get(int index, List<T> list) {
        return list.get((index + list.size()) % list.size());
    }
}
