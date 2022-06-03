package aoc2017

import io.github.zabuzard.maglev.external.algorithms.ShortestPathComputationBuilder
import io.github.zabuzard.maglev.external.graph.Graph
import io.github.zabuzard.maglev.external.graph.simple.SimpleEdge
import io.github.zabuzard.maglev.external.graph.simple.SimpleGraph
import java.awt.Point
import kotlin.math.abs

fun main() = MS14().run()

class MS14 : MS(14) {


    override fun part1(input: List<String>): Any? =
        (0 until 128).sumOf { hash(input[0], it).count { bool -> bool } }


    private val ms10 = MS10()
    fun hash(line: String, row: Int) =
        ms10.part2(listOf("${line}-$row")).toString().map { ch ->
            val sb = StringBuilder(ch.digitToInt(16).toString(2))
            while (sb.length < 4)
                sb.insert(0, '0')
            sb.map { it == '1' }
        }.flatten()


    override fun part2(input: List<String>): Any? {
        val graph: Graph<Point, SimpleEdge<Point>> = SimpleGraph()

        (0 until 128).forEach { y ->
            hash(input[0], y).forEachIndexed { x, bool -> if (bool) graph.addNode(Point(x, y)) }
        }
        graph.nodes.forEach { n1 ->
            graph.nodes.forEach { n2 ->
                if ((abs(n1.x - n2.x) <= 1) && (abs(n1.y - n2.y) <= 1) && !(abs(n1.x - n2.x) == abs(
                        n1.y - n2.y
                    ))
                ) {
                    graph.addEdge(SimpleEdge(n1, n2, 1.0))
                    graph.addEdge(SimpleEdge(n2, n1, 1.0))
                }
            }
        }
        val computer = ShortestPathComputationBuilder(graph).build()
        val grouped = mutableSetOf<Point>()
        var groupCount = 0
        graph.nodes.forEach { n1 ->
            if (!grouped.contains(n1)) {
                grouped.addAll(graph.nodes.filter { n2 ->
                    computer.shortestPathCost(
                        n1,
                        n2
                    ).isPresent
                })
                groupCount++
            }
        }
        return groupCount
    }

}
