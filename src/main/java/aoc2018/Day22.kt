package aoc2018

import io.github.zabuzard.maglev.external.algorithms.ShortestPathComputationBuilder
import io.github.zabuzard.maglev.external.graph.Graph
import io.github.zabuzard.maglev.external.graph.simple.SimpleEdge
import io.github.zabuzard.maglev.external.graph.simple.SimpleGraph

import java.awt.Point

fun main() = Day22().run()

class Day22 : Day(22) {
    override fun part1(input: List<String>): Any {
        val depth = input[0].substringAfter(' ').toInt()
        val (tx, ty) = input[1].substringAfter(' ').split(',').map(String::toInt)
        return getRegionTypes(depth, tx, ty).values.sum()
    }

    private fun getRegionTypes(
        depth: Int,
        tx: Int,
        ty: Int,
        offX: Int = 0,
        offY: Int = 0
    ): Map<Point, Int> {
        data class Region(
            private val geologicalIndex: Int,
            private val depth: Int,
            private val erosionLevel: Int = (geologicalIndex + depth) % 20183,
            val type: Int = erosionLevel % 3
        ) {


            constructor(x: Int, y: Int, depth: Int, regions: MutableMap<Point, Region>) : this(
                if (y == 0) x * 16807 else if (x == 0) y * 48271 else regions[Point(
                    x - 1, y
                )]!!.erosionLevel * regions[Point(
                    x, y - 1
                )]!!.erosionLevel, depth
            ) {
                regions[Point(x, y)] = this
            }

        }

        val regions = mutableMapOf<Point, Region>()
        (0..tx + offX).forEach { x ->
            (0..ty + offY).forEach { y -> Region(x, y, depth, regions) }
        }
        regions[Point(tx, ty)] = Region(0, depth)
        return regions.mapValues { (_, v) -> v.type }
    }

    private enum class Equipment(val c1: Int, val c2: Int) {
        TORCH(0, 2), CLIMBING_GEAR(0, 1), NEITHER(1, 2);

        fun equipmentCheck(type: Int) = type == c1 || type == c2

    }

    override fun part2(input: List<String>): Any? {


        data class Region(val equipment: Equipment, val loc: Point, val type: Int)


        val depth = input[0].substringAfter(' ').toInt()
        val (tx, ty) = input[1].substringAfter(' ').split(',').map(String::toInt)
        val graph: Graph<Region, SimpleEdge<Region>> = SimpleGraph()


        for ((loc, type) in getRegionTypes(depth, tx, ty, 50, 70)) {
            val currNodes =
                Equipment.values().filter { it.equipmentCheck(type) }
                    .map { Region(it, loc, type) }
            currNodes.forEach(graph::addNode)

            currNodes.forEach { n1 ->
                currNodes.forEach { n2 ->
                    if (n1 != n2) {
                        graph.addEdge(SimpleEdge(n1, n2, 7.0))
                        graph.addEdge(SimpleEdge(n2, n1, 7.0))
                    }
                }
            }
            val neighbours = setOf(
                Point(loc.x - 1, loc.y),
                Point(loc.x, loc.y - 1),
                Point(loc.x + 1, loc.y),
                Point(loc.x, loc.y + 1),
            )

            currNodes.forEach { n1 ->
                graph.nodes.filter { neighbours.contains(it.loc) }
                    .filter { n1.equipment == it.equipment }
                    .forEach { n2 ->
                        graph.addEdge(SimpleEdge(n1, n2, 1.0))
                        graph.addEdge(SimpleEdge(n2, n1, 1.0))
                    }
            }
        }

        val start = graph.nodes.filter { it.loc.x == 0 && it.loc.y == 0 }
            .filter { it.equipment == Equipment.TORCH }[0]
        val target = graph.nodes.filter { it.loc.x == tx && it.loc.y == ty }
            .filter { it.equipment == Equipment.TORCH }[0]


        val pathComputer = ShortestPathComputationBuilder(graph).build()
        return pathComputer.shortestPathCost(start, target).map(Double::toInt)
            .orElse(null)


    }
}
