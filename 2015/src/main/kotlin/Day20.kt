package advent.of.code

@Files("/Day20.txt")
class Day20 : Puzzle<Int, Int> {


    override fun parse(input: List<String>) = input.first().toInt()

    override fun partOne(input: Int): Int {
        return Array(1_000_000) { 0 }.apply {
            (1..size).forEach {
                var temp = it
                while (temp < size) {
                    this[temp] += it
                    temp += it
                }
            }
        }.indexOfFirst { it >= input / 10 }
    }

    override fun partTwo(input: Int): Int {
        return Array(1_000_000) { 0 }.apply {
            (1..size).forEach { amount ->
                var temp = amount
                repeat(50) {
                    if (temp >= size) return@repeat
                    this[temp] += amount
                    temp += amount
                }
            }
        }.indexOfFirst { it >= input / 11 }
    }
}

fun main() {
    execute(Day20::class)
}
