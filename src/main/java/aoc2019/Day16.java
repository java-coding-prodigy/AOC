package aoc2019;

import java.util.Arrays;
import java.util.stream.IntStream;

public class Day16 {


    private static final int[] pattern = new int[] {0, 1, 0, -1};

    public static void main(String[] args) {

        String message =
                "59791875142707344554745984624833270124746225787022156176259864082972613206097260696475359886661459314067969858521185244807128606896674972341093111690401527976891268108040443281821862422244152800144859031661510297789792278726877676645835805097902853584093615895099152578276185267316851163313487136731134073054989870018294373731775466754420075119913101001966739563592696702233028356328979384389178001923889641041703308599918672055860556825287836987992883550004999016194930620165247185883506733712391462975446192414198344745434022955974228926237100271949068464343172968939069550036969073411905889066207300644632441054836725463178144030305115977951503567";
        int[] phase = message.chars().map(i -> i - '0').toArray();


        for (int i = 0; i < 100; i++) {
            phase = getOutput(phase);
        }

        int part1 = Arrays.stream(phase).limit(8).reduce(0, (a, b) -> 10 * a + b);
        System.out.println("Part 1: " + part1);
        int offset = message.chars().limit(7).map(i -> i - '0').reduce(0, (a, b) -> 10 * a + b);

        phase = message.repeat(10_000).chars().map(i -> i - '0').skip(offset).toArray();

        for (int i = 0; i < 100; i++) {
            phase = getOutput2(phase);
        }

        int part2 = Arrays.stream(phase).limit(8).reduce(0, (a, b) -> 10 * a + b);
        System.out.println("Part 2: " + part2);
    }

    private static int[] getOutput2(int[] input) {
        int ans = 0;
        int[] output = new int[input.length];
        for (int i = input.length - 1; i >= 0; i--) {
            ans += input[i];
            output[i] = Math.abs(ans % 10);
        }
        return output;
    }

    private static int[] getOutput(int[] input) {
        int[] output = new int[input.length];
        for (int a = 0; a < input.length; a++) {
            output[a] = getDigit(input, a);
        }
        return output;
    }

    private static int getDigit(int[] input, int count) {
        int idx = 1;
        int repeat = count + 1;
        int ans = 0;
        for (int i = count; i < input.length; i++) {
            repeat--;
            if (repeat < 0) {
                repeat = count;
                idx = (idx + 1) % pattern.length;
            }
            ans += input[i] * pattern[idx];
        }
        return Math.abs(ans % 10);
    }


    private static int[] pattern(int count) {
        return IntStream.of(0, 1, 0, -1).flatMap(i -> {
            int[] arr = new int[count];
            Arrays.fill(arr, i);
            return Arrays.stream(arr);
        }).toArray();
    }


}
