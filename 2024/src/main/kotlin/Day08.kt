package advent.of.code

@Files("/Day08.txt")
class Day08 : Puzzle<Day08.Day08Input, Int> {

    data class Day08Input(val map: Map<Char, List<Pair<Int, Int>>>, val m: Int, val n: Int)

    override fun parse(input: List<String>): Day08Input {
        val map = mutableMapOf<Char, List<Pair<Int, Int>>>()
        for (i in input.indices) {
            for (j in input[i].indices) {
                if (input[i][j] != '.') {
                    map[input[i][j]] = map.getOrDefault(input[i][j], mutableListOf()) + (i to j)
                }
            }
        }
        return Day08Input(map, input.size, input[0].length)
    }

    override fun partOne(input: Day08Input): Int {
        val set = mutableSetOf<Pair<Int, Int>>()
        input.map.values.forEach { list ->
            for (i in list.indices) {
                for (j in i + 1 until list.size) {
                    val left = list[i]
                    val right = list[j]

                    val dx = left.first - right.first
                    val dy = left.second - right.second

                    set.add(left.first + dx to left.second + dy)
                    set.add(right.first - dx to right.second - dy)
                }
            }
        }
        return set.filter { it.isInGrid(input.m, input.n) }.size
    }

    override fun partTwo(input: Day08Input): Int {
        val set = mutableSetOf<Pair<Int, Int>>()
        input.map.values.forEach { list ->
            for (i in list.indices) {
                for (j in i + 1 until list.size) {
                    val left = list[i]
                    val right = list[j]

                    val dx = left.first - right.first
                    val dy = left.second - right.second

                    var forward = left
                    while (forward.isInGrid(input.m, input.n)) {
                        set.add(forward)
                        forward = forward.first + dx to forward.second + dy
                    }

                    var behind = left
                    while (behind.isInGrid(input.m, input.n)) {
                        set.add(behind)
                        behind = behind.first - dx to behind.second - dy
                    }
                }
            }
        }
        return set.size
    }

    private fun Pair<Int, Int>.isInGrid(m: Int, n: Int): Boolean = first >= 0 && second >= 0 && first < m && second < n
}

fun main() {
    execute(Day08::class)
}
