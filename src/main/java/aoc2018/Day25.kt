package aoc2018

import io.github.zabuzard.maglev.external.algorithms.ShortestPathComputationBuilder
import io.github.zabuzard.maglev.external.graph.Graph
import io.github.zabuzard.maglev.external.graph.simple.SimpleEdge
import io.github.zabuzard.maglev.external.graph.simple.SimpleGraph
import kotlin.math.abs

fun main() = Day25().part1()

class Day25 : Day(25) {
    override fun part1(input: List<String>): Any? {
        val points = input.map { it.split(',').map(String::toInt) }
        val graph: Graph<List<Int>, SimpleEdge<List<Int>>> = SimpleGraph()
        points.forEach(graph::addNode)
        points.forEach { p1 ->
            points.forEach { p2 ->
                if (p1 != p2 && p1.zip(p2) { a, b -> abs(a - b) }.sum() <= 3) {
                    graph.addEdge(SimpleEdge(p1, p2, 1.0))
                    graph.addEdge(SimpleEdge(p2, p1, 1.0))
                }
            }
        }
        val computer = ShortestPathComputationBuilder(graph).build()
        val grouped = mutableSetOf<List<Int>>()
        var groupCount = 0
        graph.nodes.forEach { n1 ->
            if (!grouped.contains(n1)) {
                grouped.addAll(graph.nodes.filter { n2 ->
                    computer.shortestPathCost(
                        n1,
                        n2
                    ).isPresent
                }.also { constellation ->
                    println(
                        constellation.joinToString(
                            "\n",
                            "\n"
                        ) { it.joinToString(" ") })
                })
                groupCount++
            }
        }
        return groupCount
    }

    override fun part2(input: List<String>): Any? {
        TODO("Not yet implemented")
    }
}
