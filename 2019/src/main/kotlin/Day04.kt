package advent.of.code

@Files("/Day04.txt")
class Day04 : Puzzle<List<String>, Int> {

    override fun parse(input: List<String>): List<String> {
        return input.first().split("-").map(String::toInt).let { (a,b) -> a..b }.map(Int::toString)
    }

    override fun partOne(input: List<String>): Int = input.count {
        val double = it.zipWithNext().any { (a, b) -> a == b }
        val increasing = it.zipWithNext().all { (a, b) -> a <= b }

        double && increasing
    }

    override fun partTwo(input: List<String>): Int = input.count {
        val doubles = it.groupingBy { a -> a }.eachCount().any { c -> c.value == 2 }
        val increasing = it.zipWithNext().all { (a, b) -> a <= b }

        doubles && increasing
    }

}

fun main() {
    execute(Day04::class)
}
