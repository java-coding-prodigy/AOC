package aoc2016

class Day6 extends Day {
    Day6() {
        super(6)
    }

    static void main(String[] args) {
        new Day6()
    }

    @Override
    void run(List<String> input) {
        def columns = input*.toList().transpose() as List<List<String>>
        def part1 = columns.collect { column -> column.countBy { it }.max { it.value }.key }.join('')
        def part2 = columns.collect { column -> column.countBy { it }.min { it.value }.key }.join('')
        println "Part 1: $part1"
        println "Part 2: $part2"
    }
}
