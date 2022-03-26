package aoc2019;

import java.util.*;
import java.util.stream.Stream;

public class Day7 {
    public static void main(String[] args) {

        int[] array = getInput();
        int part1 = permutations(new int[] {0, 1, 2, 3, 4}).mapToInt(phase -> compute(array, phase))
                .max()
                .orElse(-1);
        System.out.println("Part 1: " + part1);
        int part2 =
                permutations(new int[] {5, 6, 7, 8, 9}).mapToInt(phase -> compute2(array, phase))
                        .max()
                        .orElse(-1);

        System.out.println("Part 2: " + part2);
    }

    private static int compute2(int[] instructions, int[] phase) {
        int signal = 0;
        //final int finalSignal = signal;
        IntCode[] amps = Arrays.stream(phase)
                .mapToObj(setting -> new IntCode(setting,
                        Arrays.copyOf(instructions, instructions.length)))
                .toArray(IntCode[]::new);
        boolean bool = true;
        try {
            while (true) {
                for (IntCode amp : amps) {
                    int res = amp.runTillOutput(signal, bool);
                    if (res == 0) {
                        return signal;
                    }
                    signal = res;
                }
                bool = false;
            }
        }catch(NoSuchElementException e){
            return -1;
        }

    }

    private static int compute(int[] array, int[] phase) {
        int signal = 0;
        for (int v : phase) {
            signal = IntCode.run(array, List.of(v, signal).iterator());
        }
        return signal;
    }

    private static int[] getInput() {
        return Arrays.stream(
                "3,8,1001,8,10,8,105,1,0,0,21,34,47,72,81,94,175,256,337,418,99999,3,9,102,3,9,9,1001,9,3,9,4,9,99,3,9,101,4,9,9,1002,9,5,9,4,9,99,3,9,1001,9,5,9,1002,9,5,9,1001,9,2,9,1002,9,5,9,101,5,9,9,4,9,99,3,9,102,2,9,9,4,9,99,3,9,1001,9,4,9,102,4,9,9,4,9,99,3,9,102,2,9,9,4,9,3,9,101,2,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,102,2,9,9,4,9,3,9,1001,9,2,9,4,9,3,9,101,2,9,9,4,9,3,9,101,2,9,9,4,9,3,9,102,2,9,9,4,9,3,9,101,2,9,9,4,9,3,9,1001,9,1,9,4,9,99,3,9,102,2,9,9,4,9,3,9,1001,9,2,9,4,9,3,9,101,1,9,9,4,9,3,9,1001,9,2,9,4,9,3,9,102,2,9,9,4,9,3,9,101,2,9,9,4,9,3,9,1001,9,1,9,4,9,3,9,1001,9,2,9,4,9,3,9,1001,9,2,9,4,9,3,9,1002,9,2,9,4,9,99,3,9,101,2,9,9,4,9,3,9,101,2,9,9,4,9,3,9,101,2,9,9,4,9,3,9,1001,9,1,9,4,9,3,9,1002,9,2,9,4,9,3,9,101,1,9,9,4,9,3,9,102,2,9,9,4,9,3,9,101,1,9,9,4,9,3,9,1001,9,2,9,4,9,3,9,102,2,9,9,4,9,99,3,9,1001,9,1,9,4,9,3,9,102,2,9,9,4,9,3,9,101,2,9,9,4,9,3,9,102,2,9,9,4,9,3,9,101,1,9,9,4,9,3,9,102,2,9,9,4,9,3,9,1001,9,2,9,4,9,3,9,101,2,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,101,2,9,9,4,9,99,3,9,102,2,9,9,4,9,3,9,1001,9,2,9,4,9,3,9,1002,9,2,9,4,9,3,9,101,2,9,9,4,9,3,9,101,2,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,1001,9,1,9,4,9,3,9,101,2,9,9,4,9,3,9,1001,9,1,9,4,9,3,9,1002,9,2,9,4,9,99".split(
                        ",")).mapToInt(Integer::parseInt).toArray();
    }

    private static Stream<int[]> permutations(int[] arr) {
        List<List<Integer>> list = new ArrayList<>();
        permuteHelper(list, new ArrayList<>(), arr);
        return list.stream().map(phase -> phase.stream().mapToInt(Integer::intValue).toArray());
    }

    private static void permuteHelper(List<List<Integer>> list, List<Integer> resultList,
            int[] arr) {

        // Base case
        if (resultList.size() == arr.length) {
            list.add(new ArrayList<>(resultList));
        } else {
            for (int e : arr) {

                if (resultList.contains(e)) {
                    // If element already exists in the list then skip
                    continue;
                }
                // Choose element
                resultList.add(e);
                // Explore
                permuteHelper(list, resultList, arr);
                // Unchoose element
                resultList.remove(resultList.size() - 1);
            }
        }
    }


}
