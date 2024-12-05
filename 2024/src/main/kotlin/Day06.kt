package advent.of.code

data class Input(
    val lines: List<String>
)

@Files("/Day06-Test.txt", "/Day06.txt")
class Day06 : Puzzle<Input, Int> {
    override fun parse(input: List<String>): Input {
        return Input(lines = listOf())
    }

    override fun partOne(input: Input): Int {
        return 0
    }

    override fun partTwo(input: Input): Int {
        return 0
    }
}

fun main() {
    execute(Day06::class)
}
