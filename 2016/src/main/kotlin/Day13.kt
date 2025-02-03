package advent.of.code

@Files("/Day13.txt")
class Day13 : Puzzle<Int, Int> {

    enum class Direction(val dx: Int, val dy: Int) {
        UP(0, 1),
        DOWN(0, -1),
        LEFT(-1, 0),
        RIGHT(1, 0)
    }

    override fun parse(input: List<String>) = input.first().toInt()

    override fun partOne(input: Int): Int {
        val cache = mutableMapOf<Pair<Int, Int>, Boolean>()
        val queue = mutableListOf(listOf(1 to 1))
        while (queue.isNotEmpty()) {
            val top = queue.removeFirst()
            val (x, y) = top.last()
            if (x == 31 && y == 39) {
                return top.size - 1
            }

            Direction.entries.forEach { direction ->
                val newX = x + direction.dx
                val newY = y + direction.dy

                val node = newX to newY

                if (newX >= 0 && newY >= 0) {
                    val openSpace = cache.getOrPut(node) { isOpenSpace(node.first, node.second, input) }
                    if (openSpace && !top.contains(node)) {
                        queue.add(top + node)
                    }
                }
            }
        }
        return 0
    }

    private fun isOpenSpace(x: Int, y: Int, favorite: Int): Boolean {
        return Integer.toBinaryString(x*x + 3*x + 2*x*y + y + y*y + favorite).count { it == '1' } % 2 == 0
    }

    override fun partTwo(input: Int): Int {
        val cache = mutableMapOf<Pair<Int, Int>, Boolean>()
        val queue = mutableListOf(listOf(1 to 1))
        val unique = mutableSetOf(1 to 1)
        while (queue.isNotEmpty()) {
            val top = queue.removeFirst()
            unique.add(top.last())
            if (top.size == 51) {
                continue
            }

            Direction.entries.forEach { direction ->
                val newX = top.last().first + direction.dx
                val newY = top.last().second + direction.dy

                val node = newX to newY

                if (newX >= 0 && newY >= 0) {
                    val openSpace = cache.getOrPut(node) { isOpenSpace(node.first, node.second, input) }
                    if (openSpace && !top.contains(node)) {
                        queue.add(top + node)
                    }
                }
            }
        }
        return unique.size
    }

}

fun main() {
    execute(Day13::class)
}
