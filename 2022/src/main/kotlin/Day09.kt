package advent.of.code

@Files("/Day09.txt")
class Day09 : Puzzle<List<String>, Int> {

    override fun parse(input: List<String>): List<String> = input

    override fun partOne(input: List<String>): Int {
        val seen = mutableSetOf<Pair<Int, Int>>()

        var head = 0 to 0
        var tail = 0 to 0

        input.forEach { line ->
            val (direction, distance) = line.split(" ")

            repeat(distance.toInt()) {
                head = when (direction) {
                    "U" -> head.first to head.second + 1
                    "D" -> head.first to head.second - 1
                    "L" -> head.first - 1 to head.second
                    else -> head.first + 1 to head.second
                }

                val dFirst = head.first - tail.first
                val dSecond = head.second - tail.second

//                tail = when {
//                    dFirst > 1 && dSecond > 1  && direction == "U" -> tail.
//                }

                val newFirst = if (head.first - tail.first > 1) {
                    tail.first + 1
                } else if (tail.first - head.first > 1) {
                    tail.first - 1
                } else {
                    tail.first
                }

                val newSecond = if (head.second - tail.second > 1) {
                    tail.second + 1
                } else if (tail.second - head.second > 1) {
                    tail.second - 1
                } else {
                    tail.second
                }

                tail = newFirst to newSecond

                seen.add(tail)
            }
        }

        println(seen.joinToString("\n"))

        return seen.size
    }

    override fun partTwo(input: List<String>): Int {
        return 0
    }
}

fun main() {
    execute(Day09::class)
}
