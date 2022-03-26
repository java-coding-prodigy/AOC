package aoc2021

fun main() = Day8().part2()

class Day8 : Day(8) {
    override fun part1(input: List<String>): Any {
        return input.flatMap { it.split(" | ")[1].split(" ") }
            .filter { it.length == 2 || it.length == 7 || it.length == 4 || it.length == 3 }.size
    }

    override fun part2(input: List<String>): Any {
        return input.map {
            getCode(it)
        }.reduce { a, b -> a + b }
    }


    private fun getCode(s: CharSequence): Int {
        val split = s.split(" | ")
        val input = split[0].split(" ").filter { it != " " && it.isNotEmpty() }
        val output = split[1].split(" ").filter { it != " " && it.isNotEmpty() }
        for (map in mappingPermutations) {
            try {
                return possibleAnswer(map,input, output)
            } catch (ignore: NumberFormatException) {
            }
        }
        throw UnsupportedOperationException()
    }


    private val mappingPermutations: List<Map<Char, Char>> =
        permutations(listOf('a', 'b', 'c', 'd', 'e', 'f', 'g'))

    private val mappingToInts: Map<Set<Char>, Int> = mapOf(
        Pair(setOf('a', 'b', 'c', 'e', 'f', 'g'), 0),
        Pair(setOf('c', 'f'), 1),
        Pair(setOf('a', 'c', 'd', 'e', 'g'), 2),
        Pair(setOf('a', 'c', 'd', 'f', 'g'), 3),
        Pair(setOf('b', 'c', 'd', 'f'), 4),
        Pair(setOf('a', 'b', 'd', 'f', 'g'), 5),
        Pair(setOf('a', 'b', 'd', 'e', 'f', 'g'), 6),
        Pair(setOf('a', 'c', 'f'), 7),
        Pair(setOf('a', 'b', 'c', 'd', 'e', 'f', 'g'), 8),
        Pair(setOf('a', 'b', 'c', 'd', 'f', 'g'), 9)
    )

    private fun possibleAnswer(
        map: Map<Char, Char>,
        input: List<String>,
        output: List<String>
    ): Int {

        val bool = input.map { str -> mappingToInts[str.toSet().map { map[it] }.toSet()] }
            .all { it != null }
        if (!bool)
            throw NumberFormatException()
        return output.map { str -> mappingToInts[str.toSet().map { map[it] }.toSet()] }
            .joinToString("").toInt()
    }

    private fun permute(input: List<Char>): List<List<Char>> {
        if (input.size == 1) return listOf(input)
        val perms = mutableListOf<List<Char>>()
        val toInsert = input[0]
        for (perm in permute(input.drop(1))) {
            for (i in 0..perm.size) {
                val newPerm = perm.toMutableList()
                newPerm.add(i, toInsert)
                perms.add(newPerm)
            }
        }
        return perms
    }

    private fun permutations(chars: List<Char>): List<Map<Char, Char>> {
        val list: MutableList<Map<Char, Char>> = ArrayList()
        val permutations = permute(chars)
        for (permutation in permutations) {
            val map: MutableMap<Char, Char> = HashMap()
            for (i in chars.indices) {
                map[chars[i]] = permutation[i]
            }
            list.add(map)
        }
        return list
    }

    private fun commonChar(vararg strings: String): List<Char> =
        listOf('a', 'b', 'c', 'd', 'e', 'f', 'g').filter { ch -> strings.all { it.contains(ch) } }


    private fun uncommonChar(vararg strings: String): List<Char> =
        listOf('a', 'b', 'c', 'd', 'e', 'f', 'g').filter { ch -> !strings.all { it.contains(ch) } }

    private fun notPresentChar(vararg strings: String): List<Char> =
        listOf('a', 'b', 'c', 'd', 'e', 'f', 'g').filter { ch ->
            strings.none { it.contains(ch) }
        }

    private fun digitFromString(s: String): String {
        return when (s.length) {
            2 -> "1"
            3 -> "7"
            4 -> "4"
            5 -> if (s.contains("g")) "2" else if (s.contains("a")) "3" else "5"
            6 -> if (s.contains("a")) "0" else "6"
            7 -> "8"
            else -> {
                println(s);throw IllegalArgumentException(s)
            }
        }
    }
}
