package aoc2016

class Day11 extends Day {
    Day11() {
        super(11)
    }

    static void main(String[] args) {
        new Day11()
    }


    @Override
    void run(List<String> input) {
        def floors = input.collect {
            (it =~ /(\w+)(?:-compatible)? ((generator)|(microchip))/)
                    .collect { [it[2], it[1]] }.groupBy { it[0] }
                    .collectEntries { k, v -> [k + 's', v.collect { it[1] }] }
                    .withDefault { [] as HashSet<String> }
        }
        def q = [Tuple.tuple(0, floors)] as ArrayDeque
        def distances = [(floors): 0]/*.withDefault { Integer.MAX_VALUE }*/
        def destination = [[:], [:], [:], floors.inject([:].withDefault { [] as HashSet<String> })
                { a, b -> a.tap { b.each { k, v -> a[k].addAll(v) } } }]
        def checkFloor = { curr -> curr.generators.containsAll(curr.microchips) || curr.generators.isEmpty() }
        println distances[floors]
        while (!q.empty) {
            def (floor, state) = q.pop()
            //TODO connect to next states, try as much memoization as possible and add them to queue
            def curr = state[floor]
            println distances[floors]
            assert checkFloor(curr)
            def nextFloors = [floor + 1, floor - 1].findAll { it in 0..<4 }
            nextFloors.each { nextFloorNo ->
                def nextFloor = state[nextFloorNo]
                println distances[floors]
                def temp = curr.generators.collect() as Set<String>
                def gensToTake = curr.generators.findAll { (it !in curr.microchips || curr.generators.size() == 1)}
                assert temp == curr.generators
                println distances[floors]
                def chipsToTake = curr.microchips.findAll { it in nextFloor.generators || nextFloor.generators.isEmpty() }*.concat('-m')
                println distances[floors]
                def combosToTake = (curr.microchips.intersect(curr.generators)).collect { [it + '-g', it + '-m'] }
                println distances[floors]
                [*([gensToTake] * 2).combinations()*.toSet().findAll { nextFloor.microchips.isEmpty() || it.containsAll(nextFloor.microchips) }.collect { it*.concat('-g') }, *([chipsToTake] * 2).combinations()*.toSet(), *combosToTake].collect { additions ->
                    //println additions
                    assert additions.size() <= 2

                    def nextState = state.collect { it.collectEntries { k, v -> [k, v.collect()] }.withDefault { [] } }
                    println distances[floors]
                    additions.each {
                        def key = it[-1] == 'g' ? 'generators' : 'microchips'
                        nextState[nextFloorNo][key] += it[0..-3]
                        nextState[floor][key] -= it[0..-3]

                    }
                    println distances[floors]
                    //println nextState
                    //nextState[nextFloorNo].each { k, v -> nextState[nextFloorNo][k] += additions.findAll { it[-1] == k[0] }*.tap { println "$k, $it" }*.dropRight(2) }
                    nextState
                }.findAll { checkFloor(it[nextFloorNo]) }.each { nextState ->
                    if ((distances[nextState] ?: Integer.MAX_VALUE)> distances[state] + 1) {
                        //println "Old: ${distances[nextState]} New: ${distances[state] + 1}"
                        distances[nextState] = distances[state] + 1
                        q << Tuple.tuple(nextFloorNo, nextState)
                    }
                }
            } //println q
        }
        println distances[floors]
        println "Part 1: ${distances.find { it.key[0..2].every { it.every { it.value.empty } } }}"
    }

}
