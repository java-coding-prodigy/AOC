package aoc2021

import org.junit.jupiter.api.Test
import java.math.BigInteger
import kotlin.test.assertEquals

class Day14Test : Day14(){

    @Test
    fun test(){
        val input = """NNCB

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
CN -> C""".split("\n")
        val polymer = Polymer(input)
        assertEquals("NBCCNBBBCBHCB".groupBy{it}.mapValues{ entry -> entry.value.size.toBigInteger()}, polymer.polymerize(3))
    }
}

