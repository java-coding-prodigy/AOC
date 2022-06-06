package aoc2021

import java.math.BigInteger
import java.math.BigInteger.valueOf

fun main() = Day14().part2()/*println(Day14().part2Test("""NNCB

CH -> B
HH -> N
CB -> H
NH -> C
HB -> C
HC -> B
HN -> C
NN -> C
BH -> H
NC -> B
NB -> B
BN -> B
BB -> N
BC -> B
CC -> N
CN -> C""".split("\n"),1588))*/

open class Day14 : Day(14) {
    override fun part1(input: List<String>): Any {
        var polymer = Polymer(input)
        for (i in 1..10) {
            polymer = polymer.nextStep()
        }
        val counts = polymer.polymer.groupBy { it }.values.map { it.size }
        println(counts.maxOrNull()!! - counts.minOrNull()!!)
        return counts.maxOrNull()!! - counts.minOrNull()!!
    }

    class Polymer(val rules: Map<String, String>, val polymer: String) {
        constructor(input: List<String>) : this(input.subList(2, input.size)
            .map { it.split(" -> ") }
            .associateBy({ it[0] }) { it[1] }, input[0])

        private fun getAdjacentChars(line: String): List<String> {
            val list: MutableList<String> = ArrayList()
            for (i in 0..line.length - 2) {
                list.add(line.substring(i, i + 2))
            }
            return list
        }




        fun polymerize(steps: Int): Map<Char, BigInteger> {
            val map: MutableMap<String, BigInteger> = HashMap(getAdjacentChars(polymer).groupBy { it }.mapValues { valueOf(it.value.size.toLong())})
            println(map)
            val map2 : MutableMap<Char, BigInteger> = HashMap(polymer.groupBy { it }.mapValues { valueOf(it.value.size.toLong())})

            for (i in 1..steps) {
                val newChars : MutableMap<Char, BigInteger> = HashMap()
                val newPairs : MutableMap<String, BigInteger> = HashMap()
                map.entries.filter{ it.value != BigInteger.ZERO}.forEach {
                    val value = it.value
                    map[it.key] = BigInteger.ZERO
                    val str =
                        it.key[0].toString() + rules[it.key] + it.key[1]
                    newChars[rules[it.key]!![0]] =
                        (if (newChars[rules[it.key]!![0]] == null) BigInteger.ZERO else newChars[rules[it.key]!![0]]!!) + value
                    val pair1 = str.substring(0 until 2)
                    val pair2 = str.substring(1 until 3)
                    newPairs[pair1] =
                        (if (newPairs[pair1] != null) newPairs[pair1]!! else BigInteger.ZERO) + value
                    newPairs[pair2] =
                        (if (newPairs[pair2] != null) newPairs[pair2]!! else BigInteger.ZERO) + value
                }
                newPairs.forEach{
                        (k, v) -> map[k] = (if(map[k] == null) BigInteger.ZERO else map[k]!!) + v }
                newChars.forEach{
                        (k, v) ->
                   map2[k] = (if(map2[k] == null) BigInteger.ZERO else map2[k]!!) + v
                }
            }
            return map2
        }

        fun nextStep(): Polymer {
            val line: MutableList<Char> = polymer.toMutableList()
            for (i in 0 until polymer.length - 1) {
                line.add(2 * i + 1, rules[polymer[i] + "" + polymer[i + 1]]!![0])
            }
            println(line.joinToString(""))
            return Polymer(rules, line.joinToString(""))
        }
    }

    override fun part2(input: List<String>): Any {
        val counts = Polymer(input).polymerize(40).values/*.map{it.toLong()}*/
        counts.forEach{ println(it)}
        return counts.maxOf{ it} - counts.minOf{ it}
    }
}
