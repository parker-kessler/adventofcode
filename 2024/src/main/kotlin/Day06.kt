package advent.of.code

data class Day06Input(
    val grid: List<List<Char>>,
    var guard: Pair<Int, Int>
)

enum class Direction(val dx: Int, val dy: Int) {
    UP(-1, 0), DOWN(1, 0), LEFT(0, -1), RIGHT(0, 1);

    fun turnRight(): Direction = when (this) {
        UP -> RIGHT
        RIGHT -> DOWN
        DOWN -> LEFT
        LEFT -> UP
    }
}

@Files("/Day06.txt")
class Day06 : Puzzle<Day06Input, Int> {

    companion object {
        private const val GUARD = '^'
        private const val BORDER = '!'
        private const val OBSTRUCTION = '#'
        private const val EMPTY = '.'
    }

    override fun parse(input: List<String>): Day06Input {
        val horizontalBorder = List(input.first().length + 2) { BORDER }
        val grid = listOf(horizontalBorder) + input.map { listOf(BORDER) + it.toList() + BORDER } + listOf(horizontalBorder)

        var guard = 0 to 0
        for (i in grid.indices) {
            for (j in grid[i].indices) {
                if (grid[i][j] == GUARD) {
                    guard = i to j
                }
            }
        }
        return Day06Input(grid = grid, guard = guard)
    }

    override fun partOne(input: Day06Input): Int = analyze(input.grid, input.guard)

    override fun partTwo(input: Day06Input): Int {
        return input.grid.indices.sumOf { i ->
            input.grid[i].indices
                .filter { j -> input.grid[i][j] == EMPTY }
                .count { j -> analyze(input.grid, input.guard, i to j) == 0 }
        }
    }

    private fun analyze(grid: List<List<Char>>, startingGuard: Pair<Int, Int>, obstruction: Pair<Int, Int>? = null): Int {
        val seen = mutableMapOf<Pair<Int, Int>, MutableSet<Direction>>()
        var guard = startingGuard
        var direction = Direction.UP

        while(grid[guard.first][guard.second] != BORDER) {
            if (!seen.computeIfAbsent(guard) { mutableSetOf() }.add(direction)) return 0
            (guard.first + direction.dx to guard.second + direction.dy).let {
                if (it == obstruction || grid[it.first][it.second] == OBSTRUCTION) {
                    direction = direction.turnRight()
                } else {
                    guard = it
                }
            }
        }

        return seen.size
    }
}

fun main() {
    execute(Day06::class)
}
