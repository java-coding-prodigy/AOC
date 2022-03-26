package aoc2021

fun main() = Day12().part2()/*println(Day12().part2Test(
    """start-A
start-b
A-c
A-b
b-d
A-end
b-end""".split("\n"), 36
))*/

class Day12 : Day(12) {
    override fun part1(input: List<String>): Any {
        val v = Caves(input).recursePath()
        println(v)
        return v
    }

    enum class CaveType { SMALL, BIG }

    class Caves(input: List<String>) {
        private val start: Cave = Cave("start")

        private val end: Cave = Cave("end")

        val map: MutableMap<Cave, MutableSet<Cave>> =
            hashMapOf(Pair(start, mutableSetOf()), Pair(end, mutableSetOf()))

        init {
            for (line in input) {
                val split = line.split("-")
                val cave1 = Cave(split[0])
                val cave2 = Cave(split[1])
                if (map.containsKey(cave1)) {
                    map[cave1]?.add(cave2)
                } else {
                    map[cave1] = mutableSetOf(cave2)
                }
                if (map.containsKey(cave2)) {
                    map[cave2]?.add(cave1)
                } else {
                    map[cave2] = mutableSetOf(cave1)
                }
            }
        }

        private fun getPath(otherPaths: Set<List<Cave>>): List<Cave> {
            var path: MutableList<Cave>
            do {
                path = mutableListOf(start)
                var currentCave = start
                var prevCave = Cave("This initialization is functionally useless")
                while (currentCave != end) {

                    try {
                        prevCave = currentCave
                        currentCave =
                            map[currentCave]!!.filter { !path.contains(it) || it.type != CaveType.SMALL }
                                .random()
                        path.add(currentCave)
                    } catch (e: NoSuchElementException) {
                        currentCave = prevCave
                    }
                }
            } while (otherPaths.contains(path))
            println(path.joinToString(" -> "))
            return path
        }

        fun recursePath() : Int = recursePath(start, ArrayList())

        private fun recursePath(currentCave: Cave, path: MutableCollection<Cave>): Int {
            path.add(currentCave)
            //base case
            if (currentCave == end)
                return 1
            return map[currentCave]!!.filter { !path.contains(it) || it.type != CaveType.SMALL }
                .sumOf {
                    recursePath(it, HashSet(path))
                }
        }

        private fun recursePath2(currentCave: Cave, path: MutableCollection<Cave>, smallFree : Boolean): Int {
            path.add(currentCave)
            //base case
           // val currentFree = if(smallFree) map[currentCave]!!.all{!path.contains(it) || it.type != CaveType.SMALL} else false
            if (currentCave == end)
                return 1
            var total = 0
            for(it in map[currentCave]!!){
                if(!path.contains(it) || it.type != CaveType.SMALL){
                    total += recursePath2(it,
                        ArrayList(path),
                        (!path.contains(it) || it.type != CaveType.SMALL) /*&& currentCave != start*/ && smallFree)
                }else if (smallFree && it != start) {
                    //free = false
                    total += recursePath2(it, ArrayList(path), false)
                }
            }
            return total
        }

        fun recursePath2() : Int = recursePath2(start, ArrayList(),true)
    }


    class Cave(val type: CaveType, val name: String) {

        constructor(name: String) : this(
            if (name.uppercase() == name) CaveType.BIG else CaveType.SMALL,
            name
        )

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Cave) return false

            if (name != other.name) return false

            return true
        }

        override fun hashCode(): Int = name.hashCode()

        override fun toString(): String = name
    }

    override fun part2(input: List<String>): Any {
        val v = Caves(input).recursePath2()
        println(v)
        return Caves(input).recursePath2()
    }
}
