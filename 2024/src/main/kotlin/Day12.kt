package advent.of.code

@Files("/Day12.txt")
class Day12 : Puzzle<List<List<Char>>, Long> {

    companion object {
        private const val BORDER = '!'
        private val directions: List<Pair<Int, Int>> = arrayListOf(0 to 1, 0 to -1, 1 to 0, -1 to 0)
    }

    override fun parse(input: List<String>): List<List<Char>> {
        val horizontalBorder = List(input.first().length + 2) { BORDER }
        return listOf(horizontalBorder) + input.map { listOf(BORDER) + it.toList() + BORDER } + listOf(horizontalBorder)
    }

    override fun partOne(input: List<List<Char>>): Long {
        val seen = mutableSetOf<Pair<Int, Int>>()

        var sum = 0L
        for (i in 1 until input.size - 1) {
            for (j in 1 until input[i].size - 1) {
                if (!seen.contains(i to j)) {
                    sum += normalPrice(input, seen, i to j)
                }
            }
        }
        return sum
    }

    override fun partTwo(input: List<List<Char>>): Long {
        val seen = mutableSetOf<Pair<Int, Int>>()

        var sum = 0L
        for (i in 1 until input.size - 1) {
            for (j in 1 until input[i].size - 1) {
                if (!seen.contains(i to j)) {
                    sum += bulkDiscount(input, seen, i to j)
                }
            }
        }
        return sum
    }

    private fun normalPrice(grid: List<List<Char>>, seen: MutableSet<Pair<Int, Int>>, start: Pair<Int, Int>): Int {
        var perimeter = 0
        val seenSizeBefore = seen.size

        val queue = mutableListOf(start)
        while (queue.isNotEmpty()) {
            val square = queue.removeFirst()
            if (!seen.add(square)) continue

            val (same, different) = directions
                .map { (dx, dy) -> square.first + dx to square.second + dy }
                .partition { (dx, dy) ->  grid[dx][dy] == grid[start.first][start.second] }

            perimeter += different.size
            same.forEach { queue.add(it) }
        }
        return perimeter * (seen.size - seenSizeBefore)
    }

    private fun bulkDiscount(grid: List<List<Char>>, seen: MutableSet<Pair<Int, Int>>, start: Pair<Int, Int>): Int {
        val edgePoints = mutableMapOf<Int, MutableList<Pair<Int, Int>>>()
        val seenSizeBefore = seen.size

        val queue = mutableListOf(start)
        while (queue.isNotEmpty()) {
            val square = queue.removeFirst()
            if (!seen.add(square)) continue

            val (same, different) = directions
                .mapIndexed { index, (dx, dy) -> index to (square.first + dx to square.second + dy) }
                .partition { (_, point) ->  grid[point.first][point.second] == grid[start.first][start.second] }

            same.forEach { (_, point) -> queue.add(point) }
            different.forEach { (index, _) -> edgePoints.computeIfAbsent(index) { mutableListOf() }.add(square) }
        }

        var edges = 4
        directions.forEachIndexed { index, (dx, _) ->
            edgePoints[index]?.let { points ->
                edges += if (dx == 0) {
                    points.sortedWith(compareBy({ it.second }, { it.first })).zipWithNext()
                        .count { (previous, next) -> previous.second != next.second || previous.first + 1 != next.first }
                } else {
                    points.sortedWith(compareBy({ it.first }, { it.second })).zipWithNext()
                        .count { (previous, next) -> previous.first != next.first || previous.second + 1 != next.second }
                }
            }
        }

        return edges * (seen.size - seenSizeBefore)
    }


}

fun main() {
    execute(Day12::class)
}
