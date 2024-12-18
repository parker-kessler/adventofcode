package advent.of.code

@Files("/Day18.txt")
class Day18 : Puzzle<List<Pair<Int, Int>>, String> {

    companion object {
        const val SIZE = 71
        val directions: List<Pair<Int, Int>> = arrayListOf(0 to 1, 0 to -1, 1 to 0, -1 to 0)
    }

    override fun parse(input: List<String>) = input.map {
        line -> line.split(",").map(String::toInt).let { Pair(it[0], it[1]) }
    }

    override fun partOne(input: List<Pair<Int, Int>>): String = getCost(input, 1024).toString()

    override fun partTwo(input: List<Pair<Int, Int>>): String {
        var left = 0
        var right = input.size - 1

        while (left < right) {
            val mid = (left + right) / 2

            if (getCost(input, mid) == 0) {
                right = mid
            } else {
                left = mid + 1
            }
        }

        return input[left - 1].let { "${it.first},${it.second}" }
    }

    private fun getCost(input: List<Pair<Int, Int>>, size: Int): Int {
        val cost = createGrid(input, size)
        val queue = mutableListOf(1 to 1)
        while (queue.isNotEmpty()) {
            val (x, y) = queue.removeFirst()

            directions.forEach { (dx, dy) ->
                val new = x + dx to y + dy
                if (cost[new.first][new.second] == 0) {
                    cost[new.first][new.second] = cost[x][y] + 1
                    queue.add(new)
                }
            }
        }
        return cost[SIZE][SIZE]
    }

    private fun createGrid(input: List<Pair<Int, Int>>, size: Int): Array<Array<Int>> {
        return Array(SIZE + 2) { Array(SIZE + 2) { 0 } }.apply {
            for (i in indices) {
                this[i][0] = Int.MAX_VALUE
                this[i][SIZE + 1] = Int.MAX_VALUE
                this[0][i] = Int.MAX_VALUE
                this[SIZE + 1][i] = Int.MAX_VALUE
            }
            input.take(size).forEach { this[it.second+1][it.first+1] = Int.MAX_VALUE }
        }
    }
}

fun main() {
    execute(Day18::class)
}
