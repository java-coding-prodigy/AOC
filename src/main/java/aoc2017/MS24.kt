package aoc2017

fun main() = MS24().run()

class MS24 : MS(24) {
    override fun part1(input: List<String>): Any? = computeBridge(
        input.map { it.split('/').map(String::toInt) },
        0,
        listOf(listOf(0))
    ).maxOf { it.flatten().sum() }

    fun computeBridge(
        portsLeft: List<List<Int>>,
        prevPort: Int,
        prevPath: List<List<Int>>
    ): List<List<List<Int>>> {
        return portsLeft.filter { it.contains(prevPort) }
            .flatMap { list ->
                computeBridge(
                    portsLeft.filterNot { it == list },
                    if (list[0] == prevPort) list[1] else list[0],
                    prevPath.toMutableList().also { it.add(list) }
                )
            }.ifEmpty { listOf(prevPath) }
    }

    override fun part2(input: List<String>): Any? = computeBridge(
        input.map { it.split('/').map(String::toInt) },
        0,
        listOf(listOf(0))
    ).maxWithOrNull(
        Comparator.comparingInt(List<List<Int>>::size).thenComparingInt { it.flatten().sum() })!!
        .flatten().sum()
}
