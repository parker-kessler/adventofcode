package advent.of.code

import kotlin.math.abs

@Files("/Day22.txt")
class Day22 : Puzzle<List<Day22.Node>, Int> {

    data class Node(val x: Int, val y: Int, val size: Int, val used: Int, val available: Int, val usePercentage: Int)

    override fun parse(input: List<String>) = input.filter { "/dev/grid" in it }.map { line ->
        val nums = "[0-9]+".toRegex().findAll(line).map { it.value.toInt() }.toList()

        Node(nums[0], nums[1], nums[2], nums[3], nums[4], nums[5])
    }

    override fun partOne(input: List<Node>): Int {
        return input.sumOf { left ->
            input.count { right ->
                isViable(left, right)
            }
        }
    }

    private fun isViable(left: Node, right: Node): Boolean {
        return left != right && left.used != 0 && left.used <= right.available
    }

    override fun partTwo(input: List<Node>): Int {
        val maxX = input.maxOf { it.x }
        val minX = input.filter { it.size > 250 }.minOf { it.x }
        val emptyNode = input.first { it.used == 0 }
        var result = abs(emptyNode.x - minX) + 1 // Empty around wall X.
        result += emptyNode.y // Empty to top
        result += (maxX - minX) // Empty over next to goal
        result += (5 * maxX.dec()) + 1 // Goal back to start
        return result
    }
}

fun main() {
    execute(Day22::class)
}
