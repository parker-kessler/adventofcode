package advent.of.code

import java.math.BigInteger
import java.security.MessageDigest

@Files("/Day14.txt")
class Day14 : Puzzle<String, Int> {

    companion object {
        val matchThree = "([a-z0-9])\\1{2}".toRegex()
        val matchFive = "([a-z0-9])\\1{4}".toRegex()
        val md: MessageDigest = MessageDigest.getInstance("MD5")
    }

    data class Hash(val three: String?, val five: String?)

    override fun parse(input: List<String>) = input.first()

    override fun partOne(input: String): Int = retrieveKeys(input, times = 1).last()

    override fun partTwo(input: String): Int = retrieveKeys(input, times = 2017).last()

    private fun retrieveKeys(input: String, times: Int = 0): List<Int> {
        val found = mutableListOf<Int>()
        val hashes = mutableMapOf<Int, Hash>()

        var index = 0
        while (found.size < 64) {
            hashes.retrieveHash(input, index, times).three?.let { three ->
                if ((1..1000).any { temp ->
                        hashes.retrieveHash(input, index + temp, times).five?.first() == three.first()}) {
                    found.add(index)
                }
            }

            index++
        }
        return found
    }

    private fun MutableMap<Int, Hash>.retrieveHash(input: String, index: Int, times: Int): Hash = getOrPut(index) {
        val md5 = md5("$input$index", times)
        Hash(three = matchThree.find(md5)?.value, matchFive.find(md5)?.value)
    }

    private fun md5(input: String, times: Int): String {
        var hash = input
        repeat(times) {
            hash = BigInteger(1, md.digest(hash.toByteArray())).toString(16).padStart(32, '0')
        }
        return hash
    }

}

fun main() {
    execute(Day14::class)
}
