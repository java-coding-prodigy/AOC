package aoc2018


import io.github.zabuzard.maglev.external.algorithms.ShortestPathComputationBuilder
import io.github.zabuzard.maglev.external.graph.Graph
import io.github.zabuzard.maglev.external.graph.simple.SimpleEdge
import io.github.zabuzard.maglev.external.graph.simple.SimpleGraph

import java.awt.Point

fun main() = Day20().run()

class Day20 : Day(20) {


    override fun part1(input: List<String>) = getPathCosts(input).maxOrNull()!!

    override fun part2(input: List<String>) = getPathCosts(input).count { it >= 1000 }

    private fun getPathCosts(input: List<String>): Iterable<Int> {
        val regex = input[0].substringAfter('^').substringBefore('$')
        val source = Point(0, 0)
        val graph: Graph<Point, SimpleEdge<Point>> = SimpleGraph()
        computeGraph(graph, regex, source)
        val pathComputer = ShortestPathComputationBuilder(graph).build()
        return graph.nodes.map {
            pathComputer.shortestPathCost(source, it).map(Double::toInt).orElse(-1)
        }
    }

    private fun computeGraph(
        graph: Graph<Point, SimpleEdge<Point>>,
        regex: String,
        prev: Point
    ) {

        var curr = prev
        var idx = 0

        while (idx < regex.length) {
            val ch = regex[idx]
            if (ch == '(') {
                idx++
                val sb = StringBuilder()
                var count = 1
                while (count != 0) {
                    val inner = regex[idx]
                    if (inner == '(') {
                        count++
                    } else if (inner == ')') {
                        count--
                    }
                    if (inner == '|' && count == 1 || count == 0) {
                        //println("$regex, $sb")
                        computeGraph(graph, sb.toString(), curr)
                        sb.clear()
                    } else {
                        sb.append(inner)
                    }
                    idx++
                }
            } else {
                curr = graph.addEdgeFor(curr, ch)
                idx++
            }
        }

    }

    private fun Graph<Point, SimpleEdge<Point>>.addEdgeFor(curr: Point, ch: Char): Point {
        val x = curr.x
        val y = curr.y
        val next = when (ch) {
            'N' -> Point(x, y + 1)
            'E' -> Point(x + 1, y)
            'S' -> Point(x, y - 1)
            'W' -> Point(x - 1, y)
            else -> throw IllegalArgumentException()
        }
        val edge = SimpleEdge(curr, next, 1.0)
        this.addNode(next)
        this.addEdge(edge)

        return next
    }

}


