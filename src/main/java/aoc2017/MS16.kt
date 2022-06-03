package aoc2017



fun main() = MS16().run()

class MS16 : MS(16) {
    override fun part1(input: List<String>): Any = compute(input)

    override fun part2(input: List<String>): Any = compute(input, 1_000_000_000)


    private fun compactInstructions(
        instructions: List<String>,
        prog: List<Char> = ('a'..'p').toList()
    ): Pair<List<Int>, Map<Char, Char>> {
        val indexMoves = prog.indices.toMutableList()
        val subs = prog.associateWith { it }.toMutableMap()
        instructions.forEach {
            if (it.startsWith('s')) {
                repeat(it.substring(1).toInt()) {
                    indexMoves.add(0, indexMoves.removeLast())
                }
            } else {
                val (a, b) = it.substring(1).split('/')
                if (it.startsWith('x')) {
                    val i = a.toInt()
                    val j = b.toInt()
                    val temp = indexMoves[i]
                    indexMoves[i] = indexMoves[j]
                    indexMoves[j] = temp
                } else {

                    val temp = mutableMapOf<Char, Char>()
                    subs.forEach { (k, v) ->
                        if (v == a[0]) {
                            temp[k] = b[0]
                        }
                        if (v == b[0]) {
                            temp[k] = a[0]
                        }

                    }
                    subs.putAll(temp)
                }
            }
        }
        return Pair(indexMoves, subs)
    }

    private fun compute(input: List<String>, rounds: Int = 1): String {

        var programs = ('a'..'p').toList()
        var (indexMoves, subs) = compactInstructions(input[0].split(','), programs)
        var count = rounds

        while (count > 0) {
            if (count % 2 == 1) {
                programs = indexMoves.map { subs[programs[it]]!! }
            }
            indexMoves = indexMoves.map(indexMoves::get)
            subs = subs.mapValues { (_, v) -> subs[v]!! }

            count = count shr 1
        }
        return programs.joinToString("", transform = Char::toString)
    }


}
