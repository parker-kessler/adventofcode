package advent.of.code

@Files("/Day01.txt")
class Day01 : Puzzle<List<Int>, Int> {

    override fun parse(input: List<String>): List<Int> = input.map { it.toInt() }

    override fun partOne(input: List<Int>): Int = input.sumOf { it / 3 - 2 }

    override fun partTwo(input: List<Int>): Int = input.sumOf { start ->
        var sum = 0
        var fuel = start
        while (fuel > 0) {
            fuel = fuel / 3 - 2
            sum += maxOf(0, fuel)
        }
        sum
    }
}

fun main() {
    execute(Day01::class)
}
