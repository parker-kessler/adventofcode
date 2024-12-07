package advent.of.code

import kotlin.math.max

@Files("/Day08.txt")
class Day08 : Puzzle<List<List<Char>>, Int> {

    override fun parse(input: List<String>): List<List<Char>> {
        return input.map { it.toList() }
    }

    override fun partOne(input: List<List<Char>>): Int {
        var sum = 0
        for (i in input.indices) {
            for (j in input[i].indices) {
                if (!input.isHidden(i, j)) sum++
            }
        }
        return sum
    }

    override fun partTwo(input: List<List<Char>>): Int {
        var max = 0
        for (i in input.indices) {
            for (j in input[i].indices) {
                max = max(max, input.scenicScore(i, j))
            }
        }
        return max
    }

    private fun List<List<Char>>.isHidden(i: Int, j: Int): Boolean {
        if (i == 0 || j == 0 || i == size - 1 || j == this[i].size - 1) return false

        val current = this[i][j]

        val hiddenLeft = (0 until i).any { this[it][j] >= current }
        val hiddenRight = (i + 1 until size).any { this[it][j] >= current }

        val hiddenDown = (0 until j).any { this[i][it] >= current }
        val hiddenUp = (j + 1 until this[i].size).any { this[i][it] >= current }

        return hiddenLeft && hiddenRight && hiddenDown && hiddenUp
    }

    private fun List<List<Char>>.scenicScore(i: Int, j: Int): Int {
        val current = this[i][j]

        var left = 0
        for (x in i - 1 downTo   0) {
            left++
            if (this[x][j] >= current) break
        }

        var right = 0
        for (x in i + 1 until size) {
            right++
            if (this[x][j] >= current) break
        }

        var down = 0
        for (y in j - 1 downTo  0) {
            down++
            if (this[i][y] >= current) break
        }

        var up = 0
        for (y in j + 1 until   this[i].size) {
            up++
            if (this[i][y] >= current) break
        }

        return left * right * up * down
    }
}

fun main() {
    execute(Day08::class)
}
