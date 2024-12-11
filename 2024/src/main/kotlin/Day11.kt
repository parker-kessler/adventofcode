package advent.of.code

@Files("/Day11.txt")
class Day11 : Puzzle<List<Long>, Long> {

    override fun parse(input: List<String>): List<Long> = input.flatMap { it.split(" ") }.map(String::toLong)

    override fun partOne(input: List<Long>): Long = countStones(input, 25)

    override fun partTwo(input: List<Long>): Long = countStones(input, 75)

    private fun countStones(input: List<Long>, iterations: Int): Long {
        val initialFrequencyMap = input.map { it to 1L }.merge()

        val finalFrequencyMap = (1..iterations).fold(initialFrequencyMap) { frequencyMap, _ ->
            frequencyMap.flatMap { (beforeStone, amount) ->
                nextStones(beforeStone).map { afterStone -> afterStone to amount }
            }.merge()
        }

        return finalFrequencyMap.values.sum()
    }

    private fun List<Pair<Long, Long>>.merge() = groupBy({ it.first }, { it.second }).mapValues { it.value.sum() }

    private fun nextStones(stone: Long): List<Long> {
        val stoneString = stone.toString()
        return when {
            stone == 0L -> listOf(1L)
            stoneString.length % 2 == 0 -> stoneString.chunked(stoneString.length / 2).map(String::toLong)
            else -> listOf(stone * 2024)
        }
    }
}

fun main() {
    execute(Day11::class)
}
