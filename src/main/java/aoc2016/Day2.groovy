package aoc2016

class Day2 extends Day {
    Day2() {
        super(2)
    }

    static void main(String[] args) {
        new Day2()
    }

    @Override
    void run(List<String> input) {
        def current1 = 5
        def part1 = new StringBuilder()
        def current2 = 5
        def part2 = new StringBuilder()
        def moves =
                ['U': { ->
                    if (current1 > 3) current1 -= 3
                    current2 -= switch (current2) {
                        case 1, 2, 4, 5, 9 -> 0
                        case 3, 13 -> 2
                        case 6, 7, 8, 10, 11, 12 -> 4
                        default -> throw new IllegalStateException()
                    }
                },
                 'R': { ->
                     if (current1 % 3 > 0) current1++
                     if (current2 != 1 && current2 != 4 && current2 != 9 && current2 != 12 && current2 != 13) {
                         current2++
                     }
                 },
                 'D': { ->
                     if (current1 < 7) current1 += 3
                     current2 += switch (current2) {
                         case 5, 9, 10, 12, 13 -> 0
                         case 1, 11 -> 2
                         case 2, 3, 4, 6, 7, 8 -> 4
                         default -> throw new IllegalStateException()
                     }
                 },
                 'L': { ->
                     if (current1 % 3 != 1) current1--
                     if (current2 != 1 && current2 != 2 && current2 != 5 && current2 != 10 && current2 != 13) {
                         current2--
                     }
                 }]
        input.each { line ->
            line.split('').each { moves[it].call() }
            part1.append(current1)
            part2.append(Integer.toString(current2, 16).toUpperCase())
        }
        println "Part 1: $part1"
        println "Part 2: $part2"

    }
}
