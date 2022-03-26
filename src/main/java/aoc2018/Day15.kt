package aoc2018

import java.awt.Point
import java.util.*
import java.util.stream.IntStream

fun main() = Day15().part2()

class Day15 : Day(15) {


    override fun part1(input: List<String>): Any {
        val grid =
            input.flatMapIndexed { y, line -> line.mapIndexed { x, ch -> Pair(Point(x, y), ch) } }
                .associate { it }
                .toSortedMap(readingOrder)
        return combat(grid).second
    }


    override fun part2(input: List<String>): Any? {
        val grid =
            input.flatMapIndexed { y, line -> line.mapIndexed { x, ch -> Pair(Point(x, y), ch) } }
                .associate { it }
                .toSortedMap(readingOrder)

        var stepSize = 5
        var start = 0
        while (stepSize != 0) {
            start = minWin(start, stepSize, grid) - stepSize + 1
            stepSize /= 5
        }
        return combat(grid, start).second
    }

    private fun minWin(
        start: Int,
        stepSize: Int,
        grid: SortedMap<Point, Char>,
    ) = IntStream.iterate(start) { i -> i + stepSize }
        .filter { combat(TreeMap(grid), it).first }
        .findFirst().orElseThrow()

    private fun combat(grid: SortedMap<Point, Char>, elvesPower: Int = 3): Pair<Boolean, Int> {
        var elves =
            grid.keys.filter { grid[it] == 'E' }
                .map { Unit(Point(it), dmg = elvesPower, type = 'E') }
                .toSortedSet(Comparator.comparing(Unit::position, readingOrder))
        val startingElves = elves.size
        var goblins =
            grid.keys.filter { grid[it] == 'G' }.map { Unit(Point(it), type = 'G') }
                .toSortedSet(Comparator.comparing(Unit::position, readingOrder))
        var rounds = 0



        while (elves.isNotEmpty() && goblins.isNotEmpty()) {
            if (listOf(elves, goblins).flatten()
                    .sortedWith(Comparator.comparing(Unit::position, readingOrder))
                    .all {
                        if (it.type == 'E') {
                            it.takeTurn(grid, goblins)
                        } else {
                            it.takeTurn(grid, elves)
                        }
                    }
            ) {
                rounds++
            }
            elves = elves.toSortedSet(Comparator.comparing(Unit::position, readingOrder))
            goblins = goblins.toSortedSet(Comparator.comparing(Unit::position, readingOrder))
        }
        return Pair(
            startingElves == elves.size,
            rounds * (if (elves.isEmpty()) goblins else elves).sumOf(Unit::hp)
        )
    }

    private data class Unit(
        val position: Point,
        val type: Char,
        val dmg: Int = 3,
    ) {
        var hp: Int = 200

        val x get() = position.x
        val y get() = position.y


        fun takeTurn(
            grid: SortedMap<Point, Char>,
            targets: SortedSet<Unit>,
        ): Boolean {
            if (targets.isEmpty()) {
                return false
            }
            if (hp <= 0) {
                return true
            }



            if (attackIfAdjacent(targets, grid)) {
                return true
            }


            targets.flatMap { target ->
                neighbours(target.x, target.y)
            }.filter {
                grid[it] == '.'
            }.mapNotNull {
                getDistAndDir(it, grid)
            }
                .sortedWith(
                    Comparator.comparingInt(Triple<Int, Int, Point>::first)
                        .thenComparing(Triple<Int, Int, Point>::third, readingOrder)
                        .thenComparingInt(Triple<Int, Int, Point>::second)
                ).forEach { (_, dir, _) ->
                    grid[position] = '.'
                    when (dir) {
                        0 -> position.y--
                        1 -> position.x--
                        2 -> position.x++
                        3 -> position.y++
                        else -> throw IllegalStateException()
                    }
                    grid[position] = type
                    attackIfAdjacent(targets, grid)
                    return@takeTurn true
                }
            return true
        }

        private fun attackIfAdjacent(
            targets: SortedSet<Unit>,
            grid: SortedMap<Point, Char>,
        ): Boolean {
            val neighbours = neighbours(x, y)
            val target = targets.filter { neighbours.contains(it.position) }
                .sortedWith(
                    Comparator.comparingInt(Unit::hp).thenComparing(Unit::position, readingOrder)
                ).firstOrNull()
            if (target != null) {
                target.hp -= dmg
                if (target.hp <= 0) {
                    targets.remove(target)
                    grid.replace(target.position, '.')
                }
            }
            return target != null
        }


        fun getDistAndDir(dest: Point, grid: SortedMap<Point, Char>): Triple<Int, Int, Point>? {
            val distances = mutableMapOf(Pair(position, Pair(0, -1)))
            val q: Queue<Point> = ArrayDeque()
            neighbours(x, y).forEachIndexed { dir, neighbour ->
                if (grid[neighbour] != '.') {
                    return@forEachIndexed
                }
                q.add(neighbour)
                distances[neighbour] = Pair(1, dir)
            }
            while (!q.isEmpty()) {
                val current = q.poll()
                for (neighbour in neighbours(current.x, current.y)) {
                    if (grid[neighbour] != '.') {
                        continue
                    }
                    val (currDist, dir) = distances[current]!!
                    val dist = currDist + 1
                    val (oldDist, oldDir) = distances[neighbour] ?: Pair(Int.MAX_VALUE, 4)
                    if (dist < oldDist || (dist == oldDist && dir < oldDir)) {
                        distances[neighbour] = Pair(dist, dir)
                        q.add(neighbour)
                    }
                }
            }
            val pair = distances[dest]
            return if (pair == null) null else Triple(pair.first, pair.second, dest)
        }

    }


}

private val readingOrder =
    Comparator.comparingInt(Point::y).thenComparingInt(Point::x)

private fun neighbours(x: Int, y: Int) = listOf(
    Point(x, y - 1),
    Point(x - 1, y),
    Point(x + 1, y),
    Point(x, y + 1),
)
