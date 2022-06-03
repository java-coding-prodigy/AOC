package aoc2017

import io.github.zabuzard.maglev.external.algorithms.ShortestPathComputation
import io.github.zabuzard.maglev.external.algorithms.ShortestPathComputationBuilder
import io.github.zabuzard.maglev.external.graph.Graph
import io.github.zabuzard.maglev.external.graph.simple.SimpleEdge
import io.github.zabuzard.maglev.external.graph.simple.SimpleGraph

fun main() = MS12().run()

class MS12 : MS(12) {
    override fun part1(input: List<String>): Any? {
        val graph = buildGraph(input)
        val computer = ShortestPathComputationBuilder(graph).build()
        return graph.nodes.count { node -> canAccess(computer, node, 0) }
    }


    override fun part2(input: List<String>): Any? {
        val graph = buildGraph(input)
        val computer = ShortestPathComputationBuilder(graph).build()
        val grouped = mutableSetOf<Int>()
        var groupCount = 0
        graph.nodes.forEach { n1 ->
            if (!grouped.contains(n1)) {
                grouped.addAll(graph.nodes.filter { n2 -> canAccess(computer, n1, n2) })
                groupCount++
            }
        }
        return groupCount
    }

    private fun canAccess(
        computer: ShortestPathComputation<Int, SimpleEdge<Int>>,
        n1: Int,
        n2: Int
    ) = computer.shortestPathCost(n1, n2).isPresent

    private fun buildGraph(input: List<String>): Graph<Int, SimpleEdge<Int>> {
        val graph = SimpleGraph<Int, SimpleEdge<Int>>()
        input.forEach { line ->
            val (own, accessible) = line.split(" <-> ")
            val id = own.toInt()
            graph.addNode(id)
            accessible.split(", ").map(String::toInt).forEach {
                graph.addNode(it)
                graph.addEdge(SimpleEdge(id, it, 1.0))
                graph.addEdge(SimpleEdge(it, id, 1.0))
            }
        }
        return graph
    }
}
