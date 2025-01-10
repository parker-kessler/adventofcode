package advent.of.code

@Files("/Day01.txt")
class Day01 : Puzzle<String, Int> {

    override fun parse(input: List<String>): String = input.first()

    override fun partOne(input: String): Int {
        return input.sumOf { if (it == '(') 1 as Int else -1 }
    }

    override fun partTwo(input: String): Int {
        var floor = 0L
        return input.indexOfFirst {
            when (it) {
                '(' -> floor++
                ')' -> floor--
            }
            floor < 0
        } + 1
    }
}

fun main() {
    execute(Day01::class)
}
