package aoc2016

class Day3 extends Day {
    Day3() {
        super(3)
    }

    static void main(String[] args) {
        new Day3()
    }

    @Override
    void run(List<String> input) {
        def lengths = input.collect { it.split(' ').findAll { it }*.toInteger() }
        def part1 = possible(lengths)
        def newLengths = new ArrayList<List<Integer>>()
        (0..lengths.size() - 1).findAll { !(it % 3) }.each { i ->
            (0..2).each { j ->
                newLengths.add([lengths[i][j], lengths[i + 1][j], lengths[i + 2][j]] )
            }
        }
        def part2 = possible(newLengths)
        println "Part 1: $part1"
        println "Part 2: $part2"

    }

    private static Number possible(Collection<? extends Collection<Integer>> lengths) {
        lengths.count {
            def lengthSum = it.sum() as int
            !it.any { side -> (2 * side) >= lengthSum }
        }
    }
}
