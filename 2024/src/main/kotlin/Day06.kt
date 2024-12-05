package advent.of.code

@Input("/Day06-Test.txt", "/Day06.txt")
class Day06 : Puzzle<List<String>, Int> {
    override fun parse(input: List<String>): List<String> {
        return input
    }

    override fun partOne(input: List<String>): Int {
        return 0
    }

    override fun partTwo(input: List<String>): Int {
        return 0
    }
}

fun main() {
    execute(Day06::class)
}
