package aoc2016

class Day8 extends Day {
    Day8() {
        super(8)
    }

    static void main(String[] args) {
        new Day8()
    }

    @Override
    void run(List<String> input) {
        def col = 50
        def row = 6
        boolean[][] screen = new boolean[row][col]
        for (String line : input) {
            def nums = line.split("\\D+").collect { ("0" + it).toInteger() }
            def a = nums[1]
            def b = nums[2]
            if (line.startsWith("re")) {
                for (y in 0..b - 1) {
                    for (x in 0..a - 1) {
                        //println("${a}x${b}")
                        screen[y][x] = true
                    }
                }
            } else if (line.contains('c')) {
                def newCol = new boolean[row]
                for (y in 0..row - 1) {
                    newCol[(y + b) % row] = screen[y][a]
                }
                for (y in 0..row - 1) {
                    screen[(y + b) % row][a] = newCol[(y + b) % row]
                }
            } else {
                def newRow = new boolean[col]
                for (x in 0..col - 1) {
                    newRow[(x + b) % col] = screen[a][x]
                }
                for (x in 0..col - 1) {
                    screen[a][(x + b) % col] = newRow[(x + b) % col]
                }
            }
        }
        def part1 = 0
        def part2 = new StringBuilder('\n')
        for (y in 0..row - 1) {
            for (x in 0..col - 1) {
                if (screen[y][x]) {
                    part1++
                    part2.append('ππ')
                }else{
                    part2.append('  ')
                }
            }
            part2.append('\n')
        }
        println "Part 1: $part1"
        println "Part 2: $part2"
    }
}
