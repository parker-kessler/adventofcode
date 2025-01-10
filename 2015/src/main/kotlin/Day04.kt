package advent.of.code

import java.math.BigInteger
import java.security.MessageDigest

@Files("/Day04.txt")
class Day04 : Puzzle<String, Int> {

    override fun parse(input: List<String>): String = input.first()

    override fun partOne(input: String): Int = findZeroPrefixLength(input, 5)

    override fun partTwo(input: String): Int = findZeroPrefixLength(input, 6)

    private fun findZeroPrefixLength(input: String, size: Int): Int {
        var current = 0
        while (zeroPrefixLengthOfMd5(input + current) < size) {
            current++
        }
        return current
    }

    private fun zeroPrefixLengthOfMd5(input: String) = md5(input).takeWhile { it == '0' }.length

    private fun md5(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }
}

fun main() {
    execute(Day04::class)
}
