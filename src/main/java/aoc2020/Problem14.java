package aoc2020;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Problem14 implements AoC2020Puzzle {

    private final static Pattern pattern = Pattern.compile("mem\\[(\\d+)]\\s=\\s(\\d+)");
    List<String> input = Files.readAllLines(
            Path.of("C:\\Users\\Abc\\IdeaProjects\\AOC2021\\src\\main\\resources\\2020\\Bitmask.txt"));
           /* Arrays.asList("""
                    mask = XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X
                    mem[8] = 11
                    mem[7] = 101
                    mem[8] = 0""".split("\n"));*/

    public Problem14() throws IOException {
    }

    public static void main(String[] args) throws IOException {
        System.out.println(new Problem14().part2());
    }

    @Override public Object part1() throws IOException {
        long[] mem = new long[100000];
        String mask = "";
        //long[] arr;
        for (String line : input) {
            if (line.startsWith("ma")) {
                mask = line.substring(7);
            } else {
                try {
                    Matcher matcher = pattern.matcher(line);
                    //noinspection ResultOfMethodCallIgnored
                    matcher.find();
                    int address = Integer.parseInt(matcher.group(1));
                    char[] array = getChars(matcher.group(2));
                    for (int i = 0; i < 36; i++) {
                        array[i] = switch (mask.charAt(i)) {
                            case 'X' -> array[i];
                            case '1' -> '1';
                            case '0' -> '0';
                            default -> throw new IllegalStateException();
                        };
                    }
                    mem[address] = Long.parseLong(new String(array), 2);
                } catch (IllegalStateException e) {
                    System.out.println(line);
                    throw e;
                }
            }
            //arr = Arrays.stream(mem).filter(l -> l != 0L).toArray();
        }
        return Arrays.stream(mem)
                .mapToObj(BigInteger::valueOf)
                .reduce(BigInteger.ZERO, BigInteger::add);
    }

    private char[] getChars(String str) {
        BigInteger bigInt = new BigInteger(str);

        StringBuilder sb = new StringBuilder(bigInt.toString(2));
        int size = 36 - sb.length();
        for (int i = 0; i < size; i++)
            sb.insert(0, "0");
        return sb.toString().toCharArray();
    }

    @Override public Object part2() throws IOException {
        Map<Long, Long> mem = new HashMap<>();
        String mask = "";
        for (String line : input) {
            if (line.startsWith("ma")) {
                mask = line.substring(7);
            } else {

                    Matcher matcher = pattern.matcher(line);
                    //noinspection ResultOfMethodCallIgnored
                    matcher.find();
                    String address = matcher.group(1);
                    long value = Long.parseLong(matcher.group(2));
                    char[] array = getChars(address);
                    for (int i = 0; i < 36; i++) {
                        array[i] = switch (mask.charAt(i)) {
                            case 'X' -> 'X';
                            case '1' -> '1';
                            case '0' -> array[i];
                            default -> throw new IllegalStateException();
                        };
                    }
                    getPermutations(new String(array)).forEach(add -> mem.put(add, value));

            }
            //arr = Arrays.stream(mem).filter(l -> l != 0L).toArray();
        }
        return mem.values().stream().mapToLong(Long::longValue).sum();
    }

    private Stream<Long> getPermutations(String string) {


        return !string.contains("X") ?
                Stream.of(Long.parseLong(string, 2)) :
                Stream.of(string.replaceFirst("X", "0"), string.replaceFirst("X", "1"))
                        .flatMap(this::getPermutations);
    }
}
