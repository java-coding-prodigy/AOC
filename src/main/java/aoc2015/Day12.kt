package aoc2015

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.json.JsonMapper
import kotlin.collections.ArrayDeque

fun main() = Day12().run()

class Day12 : Day(12) {
    override fun compute(input: List<String>): Pair<Any?, Any?> {
        val root = JsonMapper().readTree(input[0])
        val q = ArrayDeque<Pair<JsonNode, Boolean>>()
        var p1 = 0
        var p2 = 0
        q.add(root to true)
        while (!q.isEmpty()) {
            val (current, noRed) = q.removeFirst()
            p1 += current.asInt()
            if (noRed)
                p2 += current.asInt()
            val childNoRed = noRed && if (current.isObject) current.asIterable()
                .none { it.asText() == "red" } else true
            current.forEach { q.addLast(it to childNoRed) }

        }
        return p1 to p2
    }
}
