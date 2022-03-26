package aoc2019;

import java.util.Arrays;

public class Day2 {
    public static void main(String[] args) {
        String codes =
                "1,0,0,3,1,1,2,3,1,3,4,3,1,5,0,3,2,1,10,19,2,6,19,23,1,23,5,27,1,27,13,31,2,6,31,35,1,5,35,39,1,39,10,43,2,6,43,47,1,47,5,51,1,51,9,55,2,55,6,59,1,59,10,63,2,63,9,67,1,67,5,71,1,71,5,75,2,75,6,79,1,5,79,83,1,10,83,87,2,13,87,91,1,10,91,95,2,13,95,99,1,99,9,103,1,5,103,107,1,107,10,111,1,111,5,115,1,115,6,119,1,119,10,123,1,123,10,127,2,127,13,131,1,13,131,135,1,135,10,139,2,139,6,143,1,143,9,147,2,147,6,151,1,5,151,155,1,9,155,159,2,159,6,163,1,163,2,167,1,10,167,0,99,2,14,0,0";
        System.out.println("Part 1: " + part1(codes));
        System.out.println("Part 2: " + part2(codes));
    }

    private static int part1(String codes) {
        return compute(Arrays.stream(codes.split(",")).mapToInt(Integer::parseInt).toArray(), 12,
                2);
    }

    private static int part2(String codes) {
        int[] array = Arrays.stream(codes.split(",")).mapToInt(Integer::parseInt).toArray();
        int[] og = Arrays.copyOf(array, array.length);
        for (int noun = 0; noun < 99; noun++) {
            for (int verb = 0; verb < 99; verb++) {
                if (compute(array, noun, verb) == 19690720) {
                    return noun * 100 + verb;
                }
                System.arraycopy(og, 0, array, 0, og.length);
            }
        }
        return -1;
    }

    private static int compute(int[] array, int noun, int verb) {
        array[1] = noun;
        array[2] = verb;
        LOOP:
        for (int i = 0; i < array.length; ) {
            int code = array[i++];
            int first = array[array[i++]];
            int second = array[array[i++]];
            int result = array[i++];
            switch (code) {
                case 1 -> array[result] = first + second;
                case 2 -> array[result] = first * second;
                case 99 -> {
                    break LOOP;
                }
                default -> {
                    return -1;
                }
            }
        }
        return array[0];
    }

}

