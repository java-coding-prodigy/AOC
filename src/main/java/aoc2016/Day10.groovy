package aoc2016


import java.util.function.IntConsumer

class Day10 extends Day {

    Day10() {
        super(10)
    }

    class Bot implements IntConsumer {
        Integer low = null
        Integer high = null

        void accept(int num) {
            if (low == null) {
                low = num
            } else if (high == null) {
                high = Math.max(low, num)
                low = Math.min(low, num)
            } else {
                throw new IllegalStateException('more than 2 inputs given to bot')
            }
        }

        boolean isReady() { low != null && high != null }
    }

    static void main(String[] args) {
        new Day10()
    }

    @Override
    void run(List<String> input) {
        List<Integer> output = []
        def partitioned = input.groupBy{it[0] =='v'}
        def bots = partitioned[true].collect { (it =~ /\w+ (\d+) (?:\w+ )+(\d+)/)[0] }.collect { [it[2].toInteger(), it[1].toInteger()] }.groupBy { it[0] }.collectEntries { k, v -> [k + 0, v.collect { it[1] }] }.withDefault { new ArrayList<Integer>() }
        def left = partitioned[false].toSet()
        while (!left.empty) {
            for (line in left) {
                def matcher = (line =~ /\w+ (\d+) (?:\w+ )+(\w+) (\d+) (?:\w+ )+(\w+) (\d+)/)[0]

                def bot = bots.computeIfAbsent(matcher[1].toInteger(), { new Bot() })
                if (bot.size() != 2) {
                    continue
                }
                def lowIdx = matcher[3].toInteger()
                def highIdx = matcher[5].toInteger()
                if (matcher[2][0] == 'b') {
                    bots[lowIdx].add(bot.min())
                } else {
                    output[lowIdx] = bot.min()
                }
                if (matcher[4][0] == 'b') {
                    bots[highIdx].add(bot.max())
                } else {
                    output[highIdx] = bot.max()
                }
                if (bot.sort() == [17, 61])
                    println "Part 1: ${matcher[1]}"
                left -= line
            }
        }
        println "Part 2: ${output[0] * output[1] * output[2]}"
    }

}
