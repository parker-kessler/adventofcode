package advent.of.code

@Files("/Day10.txt")
class Day10 : Puzzle<List<String>, Int> {

    private val directions: List<Pair<Int, Int>> = arrayListOf(0 to 1, 0 to -1, 1 to 0, -1 to 0)

    override fun parse(input: List<String>): List<String> = input

    override fun partOne(input: List<String>): Int {
        var sum = 0
        for (i in input.indices) {
            for (j in input[0].indices) {
                if (input[i][j] != '0') continue
                sum += mutableSetOf<Pair<Int, Int>>().apply { findPeaks(input, i, j, this) }.size
            }
        }
        return sum
    }

    override fun partTwo(input: List<String>): Int {
        var sum = 0
        for (i in input.indices) {
            for (j in input[0].indices) {
                if (input[i][j] != '0') continue
                sum += mutableListOf<Pair<Int, Int>>().apply { findPeaks(input, i, j, this) }.size
            }
        }
        return sum
    }

    private fun findPeaks(
        input: List<String>,
        i: Int,
        j: Int,
        peaks: MutableCollection<Pair<Int, Int>>,
        previous: Char? = null
    ) {
        if (i < 0 || j < 0 || i >= input.size || j >= input[0].length || previous != null && previous + 1 != input[i][j])
            return

        if (input[i][j] == '9') {
            peaks.add(i to j)
            return
        }

        directions.forEach { (dx, dy) -> findPeaks(input, i + dx, j + dy, peaks, input[i][j]) }
    }
}

fun main() {
    execute(Day10::class)
}
