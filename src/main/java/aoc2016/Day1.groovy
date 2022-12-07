package aoc2016

import java.awt.*
import java.util.List


class Day1 extends Day {

    Day1() {
        super(1)
    }

    static void main(String[] args) {
        new Day1()
    }


    @Override
    void run(List<String> input) {
        def instructions = input[0].split(', ').collect {
            new Tuple2(it[0] == 'R', it.substring(1).toInteger())
        }
        def x = 0
        def y = 0
        def visitedPoints = new HashSet<Point>()
        def part2 = -1
        def commands = [{
                            (1..it).collect { new Point(x, ++y) }
                        }, {
                            (1..it).collect { new Point(++x, y) }
                        }, {
                            (1..it).collect { new Point(x, --y) }
                        }, {
                            (1..it).collect { new Point(--x, y) }
                        }]
        instructions.each { right, move ->
            Collections.rotate(commands, right ? -1 : 1)
            commands.first().call(move).each {
                if (!visitedPoints.add(it) && part2 == -1)
                    part2 = Math.abs(it.x.toInteger()) + Math.abs(it.y.toInteger())
            }
        }
        def part1 = Math.abs(x) + Math.abs(y)
        println "Part 1: $part1"
        println "Part 2: $part2"

    }

}
