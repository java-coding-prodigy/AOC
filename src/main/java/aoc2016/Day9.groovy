package aoc2016

class Day9 extends Day {
    Day9() {
        super(9)
    }

    static void main(String[] args) {
        new Day9()
    }

    void run(List<String> input) {
        def file = String.join('', input)
        def result = getDecompressedLength(file)
        println "Part 1: ${result.v1}"
        println "Part 2: ${result.v2}"
    }

    private static Tuple2<Integer, Long> getDecompressedLength(String file) {
        def part1 = 0
        def part2 = 0L
        for (int i = 0; i < file.length(); i++) {
            def curr = file.charAt(i)
            if (curr == '(' as char) {
                def delimIndex = file.indexOf('x', i)
                def take = file.substring(i + 1, delimIndex).toInteger()
                def endIndex = file.indexOf(')', delimIndex)
                def factor = file.substring(delimIndex + 1, endIndex).toInteger()
                part1 += take * factor
                part2 += getDecompressedLength(file.substring(endIndex + 1, endIndex + take + 1)).v2 * factor
                i = endIndex + take
            } else {
                part1++
                part2++
            }
        }
        new Tuple2<>(part1, part2)
    }
}
