package advent.of.code

import kotlin.math.abs

@Files("/Day20.txt")
class Day20 : Puzzle<List<List<Char>>, Long> {

    enum class Direction(val dx: Int, val dy: Int) {
        UP(-1, 0), DOWN(1, 0), LEFT(0, -1), RIGHT(0, 1);

        fun opposite(): Direction = when (this) {
            UP -> DOWN
            DOWN -> UP
            LEFT -> RIGHT
            RIGHT -> LEFT
        }
    }

    companion object {
        const val EMPTY = '.'
        const val START = 'S'
        const val END = 'E'
        const val MINIMUM_TIME_SAVED = 100
    }

    override fun parse(input: List<String>): List<List<Char>> = input.map { it.toList() }

    override fun partOne(input: List<List<Char>>): Long = findCheatCount(input, 2)

    override fun partTwo(input: List<List<Char>>) = findCheatCount(input, 20)

    private fun findCheatCount(input: List<List<Char>>, seconds: Int): Long {
        val path = findPath(input)
        var sum = 0L
        path.forEachIndexed { index, point ->
            for (i in index + MINIMUM_TIME_SAVED   until path.size) {
                val diff = point.distance(path[i])
                if (diff <= seconds && i - index - diff >= MINIMUM_TIME_SAVED) {
                    sum++
                }
            }

        }
        return sum
    }

    private fun findPath(grid: List<List<Char>>): List<Pair<Int, Int>> {
        var current = findStart(grid)
        var currentDirection: Direction? = null

        return mutableListOf(current).apply {
            while (grid[current.first][current.second] != END) {
                currentDirection = Direction.entries
                    .filter { it != currentDirection?.opposite() }
                    .first { grid[current.first + it.dx][current.second + it.dy] in listOf(EMPTY, END) }
                    .also { current = current.first + it.dx to current.second + it.dy }
                add(current)
            }
        }
    }

    private fun findStart(grid: List<List<Char>>): Pair<Int, Int> {
        grid.forEachIndexed { i, row -> row.forEachIndexed { j, k -> if (k == START) return i to j } }
        return 0 to 0
    }

    private fun Pair<Int, Int>.distance(pair: Pair<Int, Int>): Int =
        abs(first - pair.first) + abs(second - pair.second)
}


fun main() {
    execute(Day20::class)
}
