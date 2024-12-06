package advent.of.code

data class Day06Input(
    val grid: MutableList<MutableList<Char>>,
    var guard: Pair<Int, Int>
)

enum class Direction(val dx: Int, val dy: Int) {
    UP(-1, 0),
    DOWN(1, 0),
    LEFT(0, -1),
    RIGHT(0, 1);

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
        private const val VISITED = 'X'
        private const val OBSTRUCTION = '#'
        private const val EMPTY = '.'
    }

    override fun parse(input: List<String>): Day06Input {
        val width = input.first().length + 2
        val grid = input.map {
            it.toMutableList().apply {
                add(0, BORDER)
                add(BORDER)
            }
        }.toMutableList().apply {
            add(0, MutableList(width) { BORDER })
            add(MutableList(width) { BORDER })
        }

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

    override fun partOne(input: Day06Input): Int = analyze(input.grid, input.guard).let { countSeen(input.grid) }

    override fun partTwo(input: Day06Input): Int {
        return input.grid.indices.sumOf { i ->
            input.grid[i].indices.count { j ->
                if (input.grid[i][j] != EMPTY) {
                    false
                } else {
                    val grid = input.grid.map { it.toMutableList() }.toMutableList().apply {
                        this[i][j] = OBSTRUCTION
                    }
                    analyze(grid, input.guard)
                }
            }
        }
    }

    private fun analyze(grid: MutableList<MutableList<Char>>, startingGuard: Pair<Int, Int>): Boolean {
        var guard = startingGuard
        var direction = Direction.UP
        var steps = 0
        val max = grid.size * grid[0].size

        while(grid[guard.first][guard.second] != BORDER && ++steps < max) {
            val newGuardPosition = guard.first + direction.dx to guard.second + direction.dy
            grid[guard.first][guard.second] = VISITED
            when(grid[newGuardPosition.first][newGuardPosition.second]) {
                OBSTRUCTION -> direction = direction.turnRight()
                else -> guard = newGuardPosition
            }
        }

        return steps == max
    }

    private fun countSeen(grid: List<List<Char>>): Int = grid.sumOf { row -> row.count { it == VISITED } }
}

fun main() {
    execute(Day06::class)
}
