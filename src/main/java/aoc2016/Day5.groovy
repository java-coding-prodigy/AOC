package aoc2016

import java.security.MessageDigest
import java.util.stream.IntStream

class Day5 extends Day {
    Day5() {
        super(5)
    }

    static void main(String[] args) {
        new Day5()
    }

    @Override
    void run(List<String> input) {
        def md5 = MessageDigest.getInstance("MD5")
        def doorId = input[0]
        def part1 = ""
        def part2 = new StringBuilder("-".repeat(8))
        IntStream.iterate(0) { it + 1 }.mapToObj {
            def hash = new BigInteger(1, md5.digest("$doorId$it".bytes)).toString(16)
            while (hash.length() < 32)
                hash = '0' + hash
            hash
        }.filter { str -> (str.substring(0, 5) == '00000') }
               // .map { it.toList() }
                .takeWhile { part2.contains('-') }
                .forEach {  str ->

                    def sixthChar = str[5]
                    if(part1.length() < 8) {
                        part1 += sixthChar
                    }
                    if (sixthChar < '8' && !part2.charAt(sixthChar as int).letterOrDigit) {
                        part2.setCharAt sixthChar as int, str[6] as char
                    }
                }
        println "Part 1: $part1"
        println "Part 2: $part2"

    }
}
