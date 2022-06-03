package aoc2017

fun main() = MS6().run()

class MS6 : MS(6) {
    override fun part1(input: List<String>): Any? {
        val banks = input[0].split(' ', '	').map(String::toInt).toMutableList()
        val hashes = mutableSetOf<String>()
        while (hashes.add(banks.joinToString(" "))) {
            var idx = 0
            var blocks = 0
            banks.forEachIndexed { i, bl ->
                if (bl > blocks) {
                    idx = i
                    blocks = bl
                }
            }
            banks[idx] = 0
            while (blocks-- > 0) {
                banks[++idx % banks.size]++
            }
        }
        return hashes.size
    }


    override fun part2(input: List<String>): Any? {
        val banks = input[0].split(' ', '	').map(String::toInt).toMutableList()
        var currIdx = 0
        val hashes = mutableMapOf<String, Int>()
        while (hashes.putIfAbsent(banks.joinToString(" "), currIdx++) == null) {
            var idx = 0
            var blocks = 0
            banks.forEachIndexed { i, bl ->
                if (bl > blocks) {
                    idx = i
                    blocks = bl
                }
            }
            banks[idx] = 0
            while (blocks-- > 0) {
                banks[++idx % banks.size]++
            }
        }
        return currIdx - hashes[banks.joinToString(" ")]!! - 1
    }
}
