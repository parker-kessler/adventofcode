package advent.of.code

@Files("/Day18.txt")
class Day18 : Puzzle<List<List<Char>>, Int> {

    companion object {
        private const val OFF = '.'
        private const val ON = '#'
    }

    override fun parse(input: List<String>): List<List<Char>> {
        val horizontalBorder = List(input.first().length + 2) { OFF }
        return listOf(horizontalBorder) + input.map { listOf(OFF) + it.toList() + OFF } + listOf(horizontalBorder)
    }

    override fun partOne(input: List<List<Char>>): Int = evaluate(input)

    override fun partTwo(input: List<List<Char>>): Int = evaluate(input, cornersOn = true)

    private fun evaluate(input: List<List<Char>>, cornersOn: Boolean = false): Int {
        var grid = input.map { it.toMutableList() }.apply {
            if (cornersOn) {
                this[1][1] = ON
                this[1][size - 2] = ON
                this[size - 2][1] = ON
                this[size - 2][size - 2] = ON
            }
        }
        repeat(100) {
            grid = grid.next(cornersOn)
        }
        return grid.sumOf { row -> row.count { it == ON } }
    }

    private fun List<List<Char>>.next(cornersOn: Boolean): List<MutableList<Char>> {
        val grid = List(size) { MutableList(size) { OFF } }
        (1 until size - 1).forEach { x ->
            (1 until size - 1).forEach { y ->
                val neighbors = (-1..1).sumOf { a ->
                    (-1..1).count { b ->
                        this[x + a][y + b] == ON
                    }
                }

                grid[x][y] = if (this[x][y] == ON) {
                    if (neighbors == 3 || neighbors == 4) ON else OFF
                } else {
                    if (neighbors == 3) ON else OFF
                }
            }
        }
        if (cornersOn) {
            grid[1][1] = ON
            grid[1][size - 2] = ON
            grid[size - 2][1] = ON
            grid[size - 2][size - 2] = ON
        }
        return grid
    }

}

fun main() {
    execute(Day18::class)
}
