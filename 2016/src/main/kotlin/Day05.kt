package advent.of.code

import java.math.BigInteger
import java.security.MessageDigest

@Files("/Day05.txt")
class Day05 : Puzzle<String, String> {

    override fun parse(input: List<String>): String = input.first()

    override fun partOne(input: String): String {
        val hashes = mutableListOf<String>()
        var number = 0
        while (hashes.size < 8) {
            md5("$input${number++}").takeIf { it.startsWith("00000") }?.let(hashes::add)
        }
        return hashes.joinToString("") { it[5].toString() }
    }

    override fun partTwo(input: String): String {
        val hashes = Array(10) { '.' }
        var number = 0
        while (hashes.any { it == '.' }) {
            md5("$input${number++}").takeIf { it.startsWith("00000") && it[5].isDigit() && hashes[it[5] - '0'] == '.' }?.let {
                hashes[it[5] - '0'] = it[6]
            }
        }
        return hashes.take(8).joinToString("")
    }

    private fun md5(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }
}

fun main() {
    execute(Day05::class)
}
