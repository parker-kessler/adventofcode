package advent.of.code

@Files("/Day19.txt")
class Day19 : Puzzle<Int, Int> {

    override fun parse(input: List<String>) = input.first().toInt()

    override fun partOne(input: Int) = (input - input.takeHighestOneBit()) * 2 + 1

    override fun partTwo(input: Int): Int {
        val queue = (0 until input).map { it }.toMutableList()
        while (queue.size != 1) {
            queue.removeAt(queue.size / 2)
            queue.add(queue.removeFirst())
        }
        return queue.first() + 1
    }
}

fun main() {
    execute(Day19::class)
}
