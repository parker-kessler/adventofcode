package advent.of.code

@Files("/Day03.txt")
class Day03 : Puzzle<List<String>, Int> {

    override fun parse(input: List<String>): List<String> = input

    override fun partOne(input: List<String>): Int = input.sumOf { line ->
        "mul\\([0-9]+,[0-9]+\\)".toRegex().findAll(line).sumOf { it.value.evaluateMultiply() }
    }

    override fun partTwo(input: List<String>): Int = input.let { lines ->
        var active = true
        lines.sumOf { line ->
            "mul\\([0-9]+,[0-9]+\\)|do\\(\\)|don't\\(\\)".toRegex().findAll(line).map { it.value }.sumOf { match ->

                var sum = 0
                when {
                    match.startsWith("don't") -> active = false
                    match.startsWith("do") -> active = true
                    active -> sum = match.evaluateMultiply()
                }
                sum
            }
        }
    }

    private fun String.evaluateMultiply(): Int = "[0-9]+".toRegex().findAll(this).map { it.value.toInt() }.reduce { acc, i -> acc * i }

}

fun main() {
    execute(Day03::class)
}
