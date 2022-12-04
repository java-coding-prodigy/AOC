package aoc2016

class Day4 extends Day {
    Day4() {
        super(4)
    }

    static void main(String[] args) {
        new Day4()
    }

    @Override
    void run(List<String> input) {
        def rooms = input.collect {
            def lastDash = it.lastIndexOf('-')
            def name = it.substring(0, lastDash).toCharArray().toList()
            def squareBracket = it.indexOf('[')
            def sectorId = it.substring(lastDash + 1, squareBracket).toInteger()
            def checkSum = it.substring(squareBracket + 1, -1)
            new Tuple3<>(name, sectorId, checkSum)
        }

        def realRooms = rooms.findAll { List<Character> name, _, String checkSum ->
            name.findAll { it != '-' }.countBy { it }.collect()
                    .toSorted(Comparator.comparingInt { Map.Entry<Character, Integer> ent -> ent.value }.reversed().thenComparingInt {
                        Map.Entry<Character, Integer> ent -> ent.key as int
                    })*.key.subList(0, 5).join('') == checkSum
        }
        def part1 = realRooms*.v2.sum()
        realRooms.collect { List<Character> name, int sectorId, _ ->
            (1..sectorId).each {
                name.indexed().forEach { int i, char ch ->
                    name.set(i, switch (ch) {
                        case '-', ' ' -> ' '
                        case 'z' -> 'a'
                        default -> (ch + 1) as char
                    } as char)
                }
            }
        }
        def part2 = realRooms.findAll { it.v1.join('').equalsIgnoreCase('northpole object storage') }*.v2.get(0)
        println "Part 1: $part1"
        println "Part 2: $part2"
    }
}
