package aoc2017

import kotlin.math.abs
import kotlin.math.sqrt

fun main() = MS20().run()

class MS20 : MS(20) {

    data class Particle(val p: MutableList<Int>, val v: MutableList<Int>, val a: List<Int>) {

        constructor(data: List<List<Int>>) : this(
            data[0].toMutableList(),
            data[1].toMutableList(),
            data[2]
        )

        constructor(line: String) : this(line.split(", ")
            .map { it.substringAfter('<').substringBefore('>').split(',').map(String::toInt) }
        )

        fun move() {
            (0 until 3).forEach {
                p[it] += v[it]
                v[it] += a[it]
            }
        }

        val dist get() = p.sumOf(::abs)
    }

    override fun part1(input: List<String>): Any {
        val particles = input.map(::Particle)
        repeat(1000) {
            particles.forEach(Particle::move)
        }
        return particles.indexOf(particles.minByOrNull(Particle::dist))
    }

    override fun part2(input: List<String>): Any {
        fun getPossibleRoot(a: Double, b: Double, c: Double): Set<Int>? {
            return (if (a != 0.0) {
                val disc = sqrt((b * b - 4 * a * c))
                listOf(
                    (-b + disc) / (2 * a),
                    (-b - disc) / (2 * a)
                ).filter { !it.isNaN() }

            } else if (b != 0.0) {
                listOf(-c / b)
            } else if (c != 0.0) {
                emptyList()
            } else {
                null
            })?.filter { it == it.toInt().toDouble() }?.map(Double::toInt)?.toSet()
        }


        fun solveAll(coeffs: List<List<Double>>): Set<Int>? {
            return coeffs.map { (a, b, c) -> getPossibleRoot(a, b, c) }
                .reduce { sol: Set<Int>?, sol2: Set<Int>? ->
                    if (sol == null) {
                        sol2
                    } else {
                        if (sol2 == null)
                            sol
                        else
                            sol.intersect(sol2)
                    }
                }
        }

        infix fun Particle.collisionResult(other: Particle) = solveAll(
            (0 until 3).map {
                listOf(
                    (a[it] - other.a[it]) / 2.0,
                    (v[it] + a[it] / 2.0) - (other.v[it] + other.a[it] / 2.0),
                    (p[it] - other.p[it]).toDouble()
                )
            }
        )

        fun deleteCollisions(collisions: List<Triple<Int, Int, Int>>, ps: Set<Int>): Set<Int> =
            if (collisions.isEmpty()) ps else {
                val (_, _, t) = collisions.first()
                val tSet = collisions.flatMap { (p1, p2, p3) ->
                    if (t == p3) setOf(
                        p1,
                        p2
                    ) else emptySet()
                }.toSet()
                deleteCollisions(
                    collisions.subList(1, collisions.size)
                        .filterNot { (p1, p2) -> tSet.contains(p1) || tSet.contains(p2) }, ps - tSet
                )
            }


        val particles = input.map(::Particle).toMutableList()
        val collisions = particles.flatMapIndexed { i, p ->
            particles.mapIndexed { j, q -> Triple(i, j, p collisionResult q) }
                .mapNotNull { (i, j, res) ->
                    (res ?: setOf(0)).minOrNull()?.let { if (i == j) null else Triple(i, j, it) }
                }
        }.sortedBy(Triple<Int, Int, Int>::third)

        return deleteCollisions(collisions, particles.indices.toSet()).size
    }
}
