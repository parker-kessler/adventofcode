package advent.of.code

@Files("/Day01.txt")
class Day01 : Puzzle<List<String>, Int> {

    override fun parse(input: List<String>): List<String> = input

    override fun partOne(input: List<String>): Int {
        return input.sumOf { line ->
            val digits = "[1-9]".toRegex().findAll(line).toList()
            digits.first().value.parseToNumber() * 10 + digits.last().value.parseToNumber()
        }
    }

    override fun partTwo(input: List<String>): Int {
        return input.sumOf { line ->
            val words = "one|two|three|four|five|six|seven|eight|nine"
            val firstMatch = "[1-9]|$words".toRegex().find(line)?.value ?: "0"
            val lastMatch = "[1-9]|${words.reversed()}".toRegex().find(line.reversed())?.value?.reversed() ?: "0"

            firstMatch.parseToNumber() * 10 + lastMatch.parseToNumber()
        }
    }

    private fun String.parseToNumber(): Int = when (this) {
        "one" -> 1
        "two" -> 2
        "three" -> 3
        "four" -> 4
        "five" -> 5
        "six" -> 6
        "seven" -> 7
        "eight" -> 8
        "nine" -> 9
        else -> toInt()
    }
}

fun main() {
    execute(Day01::class)
}
