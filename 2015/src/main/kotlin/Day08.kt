package advent.of.code

@Files("/Day08.txt")
class Day08 : Puzzle<List<String>, Int> {

    override fun parse(input: List<String>) = input

    override fun partOne(input: List<String>): Int {
        return input.sumOf { line ->
            val after = line.replace("\\\\\\\\|\\\\x[a-z0-9][a-z0-9]|\\\\\"".toRegex(), ".")
            line.length - (after.length - 2)
        }
    }

    override fun partTwo(input: List<String>): Int {
        return input.sumOf { line ->
            val after = line
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .let { "\"$it\"" }

            after.length - line.length
        }
    }
}

fun main() {
    execute(Day08::class)
}
