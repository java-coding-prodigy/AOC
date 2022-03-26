package aoc2021

import kotlin.math.max
import kotlin.math.min

fun main() =
    Day22().part2()

class Day22 : Day(22) {
    override fun part1(input: List<String>): Any {
        val reactor = Reactor()
        input.forEach(reactor::run)
        return reactor.onCubes.size
    }

    class Reactor {
        val onCubes: MutableSet<Cube> = HashSet()

        fun run(line: String) {
            val on = line[1] == 'n'
            val ranges = line.split(" ")[1].split("x=", ",y=", ",z=").filter(String::isNotEmpty)
                .map {
                    it.split("..")[0].toInt()..
                            it.split("..")[1].toInt()
                }
            if (ranges[0].first >= -50 && ranges[0].last <= 50 && ranges[1].first >= -50 && ranges[1].last <= 50 && ranges[2].first >= -50 && ranges[2].last <= 50) {
                for (x in ranges[0]) {
                    for (y in ranges[1]) {
                        for (z in ranges[2]) {
                            if (on) {
                                onCubes.add(Cube(x, y, z))
                            } else {
                                onCubes.remove(Cube(x, y, z))
                            }
                        }
                    }
                }
            }
        }
    }


    data class Cube(val x: Int, val y: Int, val z: Int)


    data class Cuboid(
        private val x1: Int,
        private val x2: Int,
        private val y1: Int,
        private val y2: Int,
        private val z1: Int,
        private val z2: Int,
        val on: Boolean,
    ) {


        constructor(iterator: Iterator<Int>, on: Boolean) : this(iterator.next(), iterator.next(),
            iterator.next(), iterator.next(),
            iterator.next(), iterator.next(),
            on)

        constructor(line: String) : this(line.split("off x=", "on x=", "..", ",y=", ",z=")
            .filter(String::isNotEmpty)
            .map(String::toInt).iterator(), line[1] == 'n')

        fun size() = (if (on) 1 else -1) * (x2 - x1 + 1L) * (y2 - y1 + 1L) * (z2 - z1 + 1L)


        fun intersects(c: Cuboid): Boolean =
            max(x1, c.x1) <= min(x2, c.x2)
                    && max(y1, c.y1) <= min(y2, c.y2)
                    && max(z1, c.z1) <= min(z2, c.z2)

        fun intersectingCuboid(c: Cuboid, on: Boolean): Cuboid = Cuboid(max(x1, c.x1),
            min(x2, c.x2),
            max(y1, c.y1),
            min(y2, c.y2),
            max(z1, c.z1),
            min(z2, c.z2), on)

        override fun toString(): String {
            return "Cuboid(x1=$x1, x2=$x2, y1=$y1, y2=$y2, z1=$z1, z2=$z2)"
        }


    }

    override fun part2(input: List<String>): Any {
        val cuboids = HashSet<Cuboid>()
        for (line in input) {
            val cuboid = Cuboid(line)
            cuboids.filter(cuboid::intersects).forEach {
                cuboids.add(cuboid.intersectingCuboid(it, !it.on))
            }
            if (cuboid.on) {
                cuboids.add(cuboid)
            }
        }
        return cuboids.sumOf(Cuboid::size)
    }
}
