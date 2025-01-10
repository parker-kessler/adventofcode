package advent.of.code

@Files("/Day02.txt")
class Day02 : Puzzle<List<Triple<Int, Int, Int>>, Int> {

    override fun parse(input: List<String>): List<Triple<Int, Int, Int>> = input.map { line ->
        line.split("x").map { it.toInt() }.let { Triple(it[0], it[1], it[2]) }
    }

    override fun partOne(input: List<Triple<Int, Int, Int>>): Int {
        return input.sumOf { (length, width, height) ->
            val surface = 2 * length * width + 2 * length * height + 2 * width * height
            val extra = listOf(length * width, length * height, width * height).min()

            surface + extra
        }
    }

    override fun partTwo(input: List<Triple<Int, Int, Int>>): Int {
        return input.sumOf { (length, width, height) ->
            val ribbon = (listOf(length, width, height) - listOf(length, width, height).max()).sum() * 2
            val bow = length * width * height

            ribbon + bow
        }
    }
}

fun main() {
    execute(Day02::class)
}
