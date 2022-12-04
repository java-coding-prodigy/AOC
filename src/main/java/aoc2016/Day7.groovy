package aoc2016

class Day7 extends Day {
    Day7() {
        super(7)
    }

    static void main(String[] args) {
        new Day7()
    }

    @Override
    void run(List<String> input) {
        def part1 = input.count {
            def (a, b, c) = it.split('[\\[\\]]')
            println("$a $b $c")
            (abba(a) || abba(c)) && !abba(b)
        }
        println "Part 1: $part1"
    }

    private static boolean abba(String str) {
        for (int i = 0; i + 4 <= str.length(); i++) {
            if (str[i] != str[i + 1] && str[i] == str[i + 3] && str[i + 1] == str[i + 2])
                return true
        }
        return false
    }
}
