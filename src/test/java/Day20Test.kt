import aoc2021.Day20
import org.junit.jupiter.api.Test
import java.awt.Point

class Day20Test : Day20() {
    @Test
    fun sortingTest() {
        val list = setOf(
            Point(1, 1),
            Point(2, 1),
            Point(3, 1),
            Point(1, 2),
            Point(2, 2),
            Point(3, 2),
            Point(1, 3),
            Point(2, 3),
            Point(3, 3)
            ).stream().sorted { ent1, ent2 ->
            val v = ent1.y.compareTo(ent2.y)
            if (v == 0) {
                ent1.x.compareTo(ent2.y)
            } else {
                v
            }
        }
    }
}
