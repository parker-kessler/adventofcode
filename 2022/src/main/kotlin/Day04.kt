package advent.of.code

@Files("/Day04.txt")
class Day04 : Puzzle<List<String>, Int> {

    override fun parse(input: List<String>): List<String> = input

    override fun partOne(input: List<String>): Int {
        return input.count { line ->
            val (left, right) = line.split(",")
            val (leftLower, leftUpper) = left.split("-").map { it.toInt() }
            val (rightLower, rightUpper) = right.split("-").map { it.toInt() }

            (leftLower <= rightLower && rightUpper <= leftUpper) || (rightLower <= leftLower && leftUpper <= rightUpper)
        }
    }

    override fun partTwo(input: List<String>): Int {
        return input.count { line ->
            val (left, right) = line.split(",")
            val (leftLower, leftUpper) = left.split("-").map { it.toInt() }
            val (rightLower, rightUpper) = right.split("-").map { it.toInt() }

            leftLower <= rightUpper && rightLower <= leftUpper
        }
    }
}

fun main() {
    execute(Day04::class)
}
