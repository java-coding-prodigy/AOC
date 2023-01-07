package aoc2022

import scala.math.max

object Day19 extends Day(19) {

  private val blueprints: Seq[Blueprint] = input.map(parseBlueprint)

  def main(args: Array[String]): Unit = run()

  override def part1(): Any = blueprints.map(bluePrint => bluePrint.id * bluePrint.findMaxGeodes(24)).sum

  override def part2(): Any = blueprints.take(3).map(_.findMaxGeodes(32)).product

  private def parseBlueprint(data: String): Blueprint = {
    val numData = data.replace(':', ' ').split(' ').filter(_.matches("\\d+")).map(_.toInt)
    Blueprint(numData(0), numData(1), numData(2), numData(3), numData(4), numData(5), numData(6))
  }

  private class Blueprint(val id: Int, val oreBotCost: Int, val clayBotCost: Int, val obsidianBotOreCost: Int, val obsidianBotClayCost: Int, val geodeBotOreCost: Int, val geodeBotObsidianCost: Int) {

    private val maxOreCost = List(oreBotCost, clayBotCost, obsidianBotOreCost, geodeBotOreCost).max
    private var maxGeodes = Int.MinValue

    override def toString: String = s"Blueprint $id: Each ore robot costs $oreBotCost ore. Each clay robot costs $clayBotCost ore. Each obsidian robot costs $obsidianBotOreCost ore and $obsidianBotClayCost clay. Each geode robot costs $geodeBotOreCost ore and $geodeBotObsidianCost obsidian."

    def findMaxGeodes(minutes: Int): Int = {
      maxGeodes = Int.MinValue
      dfs(minutes, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, false)
      maxGeodes
    }

    private def dfs(minutes: Int = 24, ore: Int = 0,
                    clay: Int = 0, obsidian: Int = 0, geode: Int = 0, oreBots: Int = 1, clayBots: Int = 0, obsidianBots: Int = 0, geodeBots: Int = 0
                    , prevOre: Int, prevClay: Int, prevObsidian: Int, botMade: Boolean): Unit = {
      val minutesLeft = minutes - 1
      if (geode + geodeBots * minutes + minutesLeft * minutes / 2 < maxGeodes) {
        return
      }
      minutesLeft match {
        case -1 => maxGeodes = max(maxGeodes, geode)
        case 0 => dfs(minutesLeft, ore, clay, obsidian, geode + geodeBots, oreBots, clayBots, obsidianBots, geodeBots, ore, clay, obsidian, false);
        case _ =>
          val newOre = ore + oreBots
          val newClay = clay + clayBots
          val newObsidian = obsidian + obsidianBots
          val newGeodes = geode + geodeBots
          if (ore >= geodeBotOreCost && obsidian >= geodeBotObsidianCost && !(prevOre >= geodeBotOreCost && prevObsidian >= geodeBotObsidianCost && !botMade)) {
            dfs(minutesLeft, newOre - geodeBotOreCost, newClay, newObsidian - geodeBotObsidianCost, newGeodes, oreBots, clayBots, obsidianBots, geodeBots + 1, ore, clay, obsidian, true)
          }
          if (ore >= obsidianBotOreCost && clay >= obsidianBotClayCost && !(prevOre >= obsidianBotOreCost && prevClay >= obsidianBotClayCost && !botMade) && minutesLeft != 1) {
            dfs(minutesLeft, newOre - obsidianBotOreCost, newClay - obsidianBotClayCost, newObsidian, newGeodes, oreBots, clayBots, obsidianBots + 1, geodeBots, ore, clay, obsidian, true)
          }
          if (ore >= clayBotCost && !(prevOre >= oreBotCost && !botMade) && minutesLeft != 1 && minutesLeft != 2) {
            dfs(minutesLeft, newOre - clayBotCost, newClay, newObsidian, newGeodes, oreBots, clayBots + 1, obsidianBots, geodeBots, ore, clay, obsidian, true)
          }
          if (ore >= oreBotCost && !(prevOre >= oreBotCost && !botMade) && minutesLeft != 1) {
            dfs(minutesLeft, newOre - oreBotCost, newClay, newObsidian, newGeodes, oreBots + 1, clayBots, obsidianBots, geodeBots, ore, clay, obsidian, true)
          }
          if (ore < maxOreCost || clay < obsidianBotClayCost || obsidian < geodeBotObsidianCost)
            dfs(minutesLeft, newOre, newClay, newObsidian, newGeodes, oreBots, clayBots, obsidianBots, geodeBots, ore, clay, obsidian, false)
      }
    }
  }
}
