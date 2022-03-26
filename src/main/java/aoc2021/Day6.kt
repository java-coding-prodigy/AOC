package aoc2021

fun main() = Day6().run()//println(Day6().part2Test("""3,4,3,1,2""".split("\n"),5934))

class Day6 : Day(6) {
    override fun part1(input: List<String>): Any {
        val initialTimers = input.flatMap { it.split(",") }.map { Fish(it.toInt()) }
        var day = Day(initialTimers)
        for (i in 1..80)
            day = day.nextDay()
        return day.fishes.size
    }

    class Fish(var timer: Int) {
        constructor() : this(8)
        constructor(fish: Fish) : this(if (fish.timer == 0) 6 else fish.timer - 1)
    }

    class Day(val fishes: Collection<Fish>) {
        fun nextDay(): Day {
            val fishes: MutableSet<Fish> = HashSet()
            for (fish in this.fishes) {
                if (fish.timer == 0)
                    fishes.add(Fish())
                fishes.add(Fish(fish))
            }
            return Day(fishes)
        }
    }

    class BetterDay(val map : Map<Int, Long>) {
        constructor(initialState: Iterable<Int>) : this(initialState.groupBy { it }.mapValues{it.value.size.toLong()})
        fun nextDay() : BetterDay{
            val map : MutableMap<Int, Long> = HashMap()
            map[0] = if(this.map[1] == null) 0 else this.map[1]!!
            map[1] = if(this.map[2] == null) 0 else this.map[2]!!
            map[2] = if(this.map[3] == null) 0 else this.map[3]!!
            map[3] = if(this.map[4] == null) 0 else this.map[4]!!
            map[4] = if(this.map[5] == null) 0 else this.map[5]!!
            map[5] = if(this.map[6] == null) 0 else this.map[6]!!
            map[6] = if(this.map[7] == null) 0 else this.map[7]!! + if(this.map[0] == null) 0 else this.map[0]!!
            map[7] = if(this.map[8] == null) 0 else this.map[8]!!
            map[8] = if(this.map[0] == null) 0 else this.map[0]!!

            return BetterDay(map)
        }
    }

    override fun part2(input: List<String>): Any {
        val initialTimers = input.flatMap { it.split(",") }.map { it.toInt() }
        println(initialTimers)
        var day = BetterDay(initialTimers)
        for (i in 1..256){
            //println(day.map)
            day = day.nextDay()
        }
        println(day.map)
        println(day.map.values.reduce{a, b -> a + b})
        println(true)
        return day.map.values.reduce{a, b -> a + b}
    }
}
