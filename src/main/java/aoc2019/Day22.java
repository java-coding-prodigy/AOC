package aoc2019;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

public class Day22 {

    private static final LinearFunction identity =
            new LinearFunction(BigInteger.ONE, BigInteger.ZERO);

    public static void main(String[] args) {


        int part1 = computeSlow(2019, 10_007);
        System.out.println("Part 1: " + part1);

        BigInteger part2 =
                computeFinal(BigInteger.valueOf(2020), BigInteger.valueOf(119315717514047L),
                        BigInteger.valueOf(101741582076661L));

        System.out.println("Part 2: " + part2);

    }

    private static int computeSlow(int value, int size) {
        int index = value;
        for (String line : input()) {
            Scanner sc = new Scanner(line);

            if (sc.next().equals("cut")) {

                index = (index + size - sc.nextInt()) % size;
            } else {
                sc.next();
                sc.next();
                if (sc.hasNextInt()) {

                    index = (index * sc.nextInt()) % size;
                } else {
                    index = size - 1 - index;
                }
            }
        }

        return index;
    }

    private static BigInteger computeFinal(BigInteger value, BigInteger size,
            BigInteger repetitions) {
        LinearFunction f = getFunction(size);
        return reduced(f.k, f.m, size, repetitions).apply(value).mod(size);
    }

    private static String[] input() {
        return """
                cut 4753
                deal with increment 64
                cut 9347
                deal with increment 64
                cut 4913
                deal with increment 33
                cut 6371
                deal with increment 53
                cut -7660
                deal with increment 65
                cut 7992
                deal with increment 29
                cut 7979
                deal with increment 28
                cut -6056
                deal with increment 5
                cut -3096
                deal with increment 13
                cut -4315
                deal with increment 52
                cut 2048
                deal into new stack
                cut 9126
                deal with increment 67
                deal into new stack
                cut -4398
                deal with increment 29
                cut 5230
                deal with increment 30
                cut 1150
                deal with increment 41
                cut 668
                deal into new stack
                cut -7265
                deal with increment 69
                deal into new stack
                deal with increment 38
                cut -8498
                deal with increment 68
                deal into new stack
                deal with increment 30
                cut -1108
                deal with increment 7
                cut 5875
                deal with increment 13
                cut -8614
                deal with increment 44
                cut -9866
                deal with increment 2
                cut 2582
                deal with increment 43
                cut 6628
                deal with increment 59
                deal into new stack
                cut 7514
                deal into new stack
                cut -115
                deal with increment 14
                cut 2844
                deal with increment 4
                cut 6564
                deal with increment 23
                cut -8148
                deal with increment 12
                cut -81
                deal with increment 2
                cut 9928
                deal with increment 8
                cut 3174
                deal with increment 28
                deal into new stack
                cut 6259
                deal with increment 3
                cut 1863
                deal with increment 34
                deal into new stack
                cut 4751
                deal into new stack
                cut -7394
                deal with increment 59
                deal into new stack
                deal with increment 28
                deal into new stack
                deal with increment 59
                cut -848
                deal with increment 19
                deal into new stack
                cut -575
                deal with increment 60
                deal into new stack
                deal with increment 74
                cut 514
                deal into new stack
                cut 8660
                deal with increment 3
                cut 5325
                deal with increment 41
                deal into new stack
                deal with increment 10
                deal into new stack""".split("\n");
    }

    private static LinearFunction getFunction(BigInteger size) {

        List<String> input = Arrays.asList(input());
        Collections.reverse(input);
        return input.stream()
                .map(line -> new LinearFunction(line.trim(), size))
                .reduce(identity, LinearFunction::compose);
    }


    private static LinearFunction reduced(BigInteger k, BigInteger m, BigInteger size,
            BigInteger repetitions) {
        if (repetitions.equals(BigInteger.ZERO)) {
            return identity;
        }
        if (repetitions.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
            return reduced(k.multiply(k).mod(size), k.multiply(m).add(m).mod(size), size,
                    repetitions.divide(BigInteger.TWO));
        }
        LinearFunction cd = reduced(k, m, size, repetitions.subtract(BigInteger.ONE));
        return new LinearFunction(k.multiply(cd.k).mod(size), k.multiply(cd.m).add(m).mod(size));
    }

    private static class LinearFunction {

        final BigInteger k;
        final BigInteger m;

        public LinearFunction(BigInteger k, BigInteger m) {
            this.k = k;
            this.m = m;
        }

        public LinearFunction(String s, BigInteger nCards) {
            if (s.contains("deal into new stack")) {
                this.k = ONE.negate();
                this.m = ONE.negate().subtract(nCards);
            } else if (s.startsWith("cut")) {
                this.k = ONE;
                this.m = argOf(s).mod(nCards);
            } else if (s.startsWith("deal with increment")) {
                BigInteger z = argOf(s).modInverse(nCards);
                this.k = ONE.multiply(z).mod(nCards);
                this.m = ZERO;
            } else {
                throw new RuntimeException(s);
            }
        }

        private BigInteger argOf(String s) {
            return new BigInteger(s.replaceAll("[^-?0-9]+", ""));
        }


        public BigInteger apply(BigInteger x) {
            return k.multiply(x).add(m);
        }



        public LinearFunction compose(LinearFunction g) {
            return new LinearFunction(k.multiply(g.k), g.k.multiply(m).add(g.m));
        }


    }


}
