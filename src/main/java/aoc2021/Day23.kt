package aoc2021

import java.awt.Point
import java.util.*

fun main() {
    Day23().part2()
}

class Day23 : Day(23) {
    override fun part1(input: List<String>): Any {
        val start = Grid(input)
        val queue: Queue<Grid> = LinkedList()
        queue.add(start)
        val map: MutableMap<Grid, Int> = HashMap()
        map[start] = 0
        do {
            val current = queue.poll()
            for (next in current.possibleMoves()) {
                val distance = map[current]!! + next.energy
                if (!map.contains(next) || distance < map[next]!!) {
                    map[next] = distance
                    queue.add(next)
                }
            }
        } while (queue.isNotEmpty())
        return map[end]!!
    }

    enum class GridItem(
        val amphipod: Boolean,
        val movementCost: Int,
    ) {

        WALL(false, 0),
        EMPTY_SPACE(false, 0),
        AMBER(true, 1),
        BRONZE(true, 10),
        COPPER(true, 100),
        DESERT(true, 1000);
    }

    private val end = Grid("""#############
#...........#
###A#B#C#D###
  #A#B#C#D#  
  #########  """.split("\n"))


    open class Grid(
        val grid: MutableMap<Point, GridItem> = HashMap(),
        val energy: Int = 0,
    ) {

        private val rooms: MutableMap<GridItem, Pair<Point, Point>> = EnumMap(GridItem::class.java)

        init {
            rooms[GridItem.AMBER] = Pair(Point(3, 2), Point(3, 3))
            rooms[GridItem.BRONZE] = Pair(Point(5, 2), Point(5, 3))
            rooms[GridItem.COPPER] = Pair(Point(7, 2), Point(7, 3))
            rooms[GridItem.DESERT] = Pair(Point(9, 2), Point(9, 3))
        }

        constructor(input: List<String>) : this(HashMap(), 0) {
            for (i in input.indices) {
                for (j in input[i].indices) {
                    grid[Point(j, i)] = when (input[i][j]) {
                        '#' -> GridItem.WALL
                        '.' -> GridItem.EMPTY_SPACE
                        'A' -> GridItem.AMBER
                        'B' -> GridItem.BRONZE
                        'C' -> GridItem.COPPER
                        'D' -> GridItem.DESERT
                        ' ' -> continue
                        else -> throw UnsupportedOperationException()
                    }
                }
            }

        }

        private fun move(p1: Point, p2: Point, energy: Int): Grid {
            val newGrid = HashMap(grid)
            newGrid[p1] = grid[p2]!!
            newGrid[p2] = grid[p1]!!
            return Grid(newGrid, energy)
        }


        fun possibleMoves(): List<Grid> {
            val grids = ArrayList<Grid>()
            val amphipods = grid.filter { (_, v) -> v.amphipod }
            for ((point, amphipod) in amphipods) {
                val (d1, d2) = rooms[amphipod]!!
                if (d2 == point) {
                    continue
                }
                when (point.y) {
                    1 -> {
                        if (grid[d1] != GridItem.EMPTY_SPACE)
                            continue
                        var dist = 0
                        var bool = true
                        for (x in if (point.x > d1.x) point.x - 1 downTo d1.x else point.x + 1..d1.x) {
                            if (grid[Point(x, 1)] != GridItem.EMPTY_SPACE) {
                                bool = false
                                //break
                            }
                            dist++
                        }
                        if (bool) {
                            if (grid[d2] == GridItem.EMPTY_SPACE)
                                grids.add(move(point,
                                    d2,
                                    (dist + 2) * amphipod.movementCost/*+ energy*/))
                            else
                                grids.add(move(point,
                                    d1,
                                    (dist + 1) * amphipod.movementCost /*+ energy*/))
                        }

                    }
                    2 -> {
                        if (d1 == point && grid[d2] == amphipod)
                            continue
                        val map = HashMap<Point, Int>(8)
                        val y = 1
                        for (x in point.x downTo 1) {
                            val p = Point(x, y)
                            if (grid[p] != GridItem.EMPTY_SPACE)
                                break
                            if (x != 3 && x != 5 && x != 7 && x != 9)
                                map[p] = (point.x - x + 1) * amphipod.movementCost
                        }
                        for (x in point.x ..11) {
                            val p = Point(x, y)
                            if (grid[p] != GridItem.EMPTY_SPACE)
                                break
                            if (x != 3 && x != 5 && x != 7 && x != 9)
                                map[p] = (x - point.x + 1) * amphipod.movementCost
                        }
                        grids.addAll(map.entries.map { (k, v) -> move(point, k, v /*+ energy*/) })

                    }
                    3 -> {
                        if (d2 == point || grid[Point(point.x, 2)] != GridItem.EMPTY_SPACE)
                            continue
                        val map = HashMap<Point, Int>(8)
                        val y = 1
                        for (x in point.x downTo 1) {
                            val p = Point(x, y)
                            if (grid[p] != GridItem.EMPTY_SPACE)
                                break
                            if (x != 3 && x != 5 && x != 7 && x != 9)
                                map[p] = (point.x - x + 2) * amphipod.movementCost
                        }
                        for (x in point.x until 11) {
                            val p = Point(x, y)
                            if (grid[p] != GridItem.EMPTY_SPACE)
                                break
                            if (x != 3 && x != 5 && x != 7 && x != 9)
                                map[p] = (x - point.x + 2) * amphipod.movementCost
                        }
                        grids.addAll(map.entries.map { (k, v) -> move(point, k, v /*+ energy*/) })
                    }
                }
            }
            return grids
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Grid) return false

            if (grid != other.grid) return false

            return true
        }

        override fun hashCode(): Int {
            return grid.hashCode()
        }
    }

    class Grid2(private val grid: MutableMap<Point, GridItem> = HashMap(), val energy: Int = 0) {
        private val rooms: MutableMap<GridItem, List<Point>> = EnumMap(GridItem::class.java)

        init {
            rooms[GridItem.AMBER] = (2..5).map { Point(3, it) }
            rooms[GridItem.BRONZE] = (2..5).map { Point(5, it) }
            rooms[GridItem.COPPER] = (2..5).map { Point(7, it) }
            rooms[GridItem.DESERT] = (2..5).map { Point(9, it) }
        }

        constructor(input: String) : this(){
            val list = input.split("\n")
            for (i in list.indices) {
                for (j in list[i].indices) {
                    grid[Point(j, i)] = when (list[i][j]) {
                        '#' -> GridItem.WALL
                        '.' -> GridItem.EMPTY_SPACE
                        'A' -> GridItem.AMBER
                        'B' -> GridItem.BRONZE
                        'C' -> GridItem.COPPER
                        'D' -> GridItem.DESERT
                        ' ' -> continue
                        else -> throw UnsupportedOperationException()
                    }
                }
            }
        }

        constructor(input: List<String>) : this(HashMap(), 0) {
            val list = ArrayList(input)
            list.add(3, "  #D#C#B#A#")
            list.add(4, "  #D#B#A#C#")
            for (i in list.indices) {
                for (j in list[i].indices) {
                    grid[Point(j, i)] = when (list[i][j]) {
                        '#' -> GridItem.WALL
                        '.' -> GridItem.EMPTY_SPACE
                        'A' -> GridItem.AMBER
                        'B' -> GridItem.BRONZE
                        'C' -> GridItem.COPPER
                        'D' -> GridItem.DESERT
                        ' ' -> continue
                        else -> throw UnsupportedOperationException()
                    }
                }
            }
        }

        private fun move(og: Point, new: Point, energy: Int): Grid2 {
            val newGrid = HashMap(grid)
            newGrid[og] = grid[new]
            newGrid[new] = grid[og]
            return Grid2(newGrid, energy)
        }

        fun possibleMoves(): List<Grid2> {
            val grids = ArrayList<Grid2>()
            val amphipods = grid.filter { (_, v) -> v.amphipod }
            for ((point, amphipod) in amphipods) {
                val dest = rooms[amphipod]!!

                if (point.y == 1) {
                    if (dest.any { grid[it] != GridItem.EMPTY_SPACE && grid[it] != amphipod })
                        continue
                    val (d1, d2, d3, d4) = dest

                    var dist = 0
                    var bool = true
                    for (x in if (point.x > d1.x) point.x - 1 downTo d1.x else point.x + 1..d1.x) {
                        if (grid[Point(x, 1)] != GridItem.EMPTY_SPACE) {
                            bool = false
                            //break
                        }
                        dist++
                    }
                    if (bool) {
                        when (GridItem.EMPTY_SPACE) {
                            grid[d4] -> grids.add(move(point,
                                d4,
                                (dist + 4) * amphipod.movementCost))
                            grid[d3] -> grids.add(move(point,
                                d3,
                                (dist + 3) * amphipod.movementCost))
                            grid[d2] -> grids.add(move(point,
                                d2,
                                (dist + 2) * amphipod.movementCost))
                            grid[d1] -> grids.add(move(point,
                                d1,
                                (dist + 1) * amphipod.movementCost))
                            else -> continue
                        }
                    }
                } else {
                    if (grid[Point(point.x, point.y - 1)] != GridItem.EMPTY_SPACE || (dest.contains(
                            point) && dest.subList(dest.indexOf(point), 4)
                            .all { grid[it] == amphipod })
                    ) {
                        continue
                    }
                    val vertical = point.y - 1
                    val map = HashMap<Point, Int>(8)
                    val y = 1
                    for (x in point.x downTo 1) {
                        val p = Point(x, y)
                        if (grid[p] != GridItem.EMPTY_SPACE)
                            break
                        if (x != 3 && x != 5 && x != 7 && x != 9)
                            map[p] = (point.x - x + vertical) * amphipod.movementCost
                    }
                    for (x in point.x ..11) {
                        val p = Point(x, y)
                        if (grid[p] != GridItem.EMPTY_SPACE)
                            break
                        if (x != 3 && x != 5 && x != 7 && x != 9)
                            map[p] = (x - point.x + vertical) * amphipod.movementCost
                    }
                    grids.addAll(map.entries.map { (k, v) -> move(point, k, v) })

                }
            }
            return grids
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Grid2) return false

            if (grid != other.grid) return false

            return true
        }

        override fun hashCode(): Int {
            return grid.hashCode()
        }
    }

    private val end2 = Grid2("""#############
#...........#
###A#B#C#D###
  #A#B#C#D#
  #A#B#C#D#
  #A#B#C#D#
  #########""")

    override fun part2(input: List<String>): Any {
        val start = Grid2(input)
        val queue: Queue<Grid2> = LinkedList()
        queue.add(start)
        val map: MutableMap<Grid2, Int> = HashMap()
        map[start] = 0
        do {
            val current = queue.poll()
            for (next in current.possibleMoves()) {
                val distance = map[current]!! + next.energy
                if (!map.contains(next) || distance < map[next]!!) {
                    map[next] = distance
                    queue.add(next)
                }
            }
        } while (queue.isNotEmpty())
        return map[end2]!!
    }
}
