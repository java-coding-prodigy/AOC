package AOC2021Practice


import kotlin.io.path.Path
import kotlin.io.path.readLines

fun main() {
    val numbers = Path("src/main/resources/2020/ExpenseReport.txt").readLines()
            .map{ it.toInt()}.toSet()
    val map = numbers.associateBy ({ it },{2020 - it})
    val number = map.entries.filter { numbers.contains(it.value) }.map{ it.key}[0]
    println(number * (2020 - number))
    numbers.forEach { i ->
        numbers.forEach {
            if (i + it == 2020)
                println(i * it)
        }
    }
}

