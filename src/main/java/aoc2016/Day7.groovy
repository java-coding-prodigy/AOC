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
        def part1 = 0
        def part2 = 0
        input.each {
            def ip = it.split('[\\[\\]]')
            def res = null
            Set<String> aba = new HashSet<>()
            Set<String> bab = new HashSet<>()
            ip.eachWithIndex { String text, int i ->
                if (i % 2 == 0) {
                    if (abba(text))
                        res = res == null ? true : res
                    aba.addAll(ssl(text))
                } else {
                    if (abba(text))
                        res = false
                    bab.addAll(ssl(text))
                }
            }
            if (res)
                part1++
            if (aba.any { s -> (bab.contains(s.reverse())) })
                part2++
        }
        println "Part 1: $part1"
        println "Part 2: $part2"
    }

    private static boolean abba(String str) {
        for (int i = 0; i + 4 <= str.length(); i++) {
            if (str[i] != str[i + 1] && str[i] == str[i + 3] && str[i + 1] == str[i + 2])
                return true
        }
        return false
    }

    private static Set<String> ssl(String str) {
        def abas = new HashSet<String>()
        for (int i = 0; i + 3 <= str.length(); i++) {
            if (str[i] == str[i + 2] && str[i] != str[i + 1])
                abas.add("" + str[i] + str[i + 1])
        }
        return abas
    }
}
