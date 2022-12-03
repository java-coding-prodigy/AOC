package aoc2015

import kotlin.math.min
import kotlin.math.max

fun main() = Day9().run()

class Day9 : Day(9) {
    override fun compute(input: List<String>): Pair<Any?, Any?> {
        val cities = mutableSetOf<String>()
        val distances = mutableMapOf<Pair<String, String>, Int>()
        input.forEach {
            val (source, destination, distance) = it.split(" to ", " = ")
            cities.add(source)
            cities.add(destination)
            distances[source to destination] = distance.toInt()
            distances[destination to source] = distance.toInt()
        }
        return dfs(cities, distances)
    }

    private fun dfs(
        cities: Set<String>,
        distances: Map<Pair<String, String>, Int>
    ): Pair<Int, Int> =
        cities.map { city -> dfs(cities.toMutableSet().also { it.remove(city) }, distances, city) }
            .reduce { a, b -> min(a.first, b.first) to max(a.second, b.second) }

    private fun dfs(
        citiesLeft: Set<String>,
        distances: Map<Pair<String, String>, Int>,
        lastCity: String
    ): Pair<Int, Int> {
        return if (citiesLeft.isEmpty()) 0 to 0 else citiesLeft.map { city ->
            val dist = distances[lastCity to city]!!
            val result = dfs(
                citiesLeft.toMutableSet().also { it.remove(city) },
                distances, city
            )
            (result.first + dist) to (result.second + dist)
        }.reduce { a, b -> min(a.first, b.first) to max(a.second, b.second) }
    }

}