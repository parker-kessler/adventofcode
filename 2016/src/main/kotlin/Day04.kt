package advent.of.code

@Files("/Day04.txt")
class Day04 : Puzzle<List<Day04.Room>, Int> {

    data class Room(val encryptedName: String, val sectorId: Int, val checkSum: String)

    override fun parse(input: List<String>) = input.map { line ->
        val parts = line.split("[")
        val encryptedName = parts[0].substringBeforeLast("-")
        val sectorId = parts[0].substringAfterLast("-").toInt()

        Room(encryptedName, sectorId, parts[1].removeSuffix("]"))
    }

    override fun partOne(input: List<Room>): Int {
        return input.filter { createCheckSum(it.encryptedName) == it.checkSum }.sumOf { it.sectorId }
    }

    override fun partTwo(input: List<Room>): Int {
        return input.filter { createCheckSum(it.encryptedName) == it.checkSum }
            .first { decryptName(it.encryptedName, it.sectorId) == "northpole-object-storage" }.sectorId
    }

    private fun createCheckSum(encryptedName: String): String {
        return encryptedName.filter { it != '-' }
            .groupingBy { it }
            .eachCount()
            .toList()
            .sortedWith(compareByDescending<Pair<Char, Int>> { it.second }.thenBy { it.first })
            .take(5)
            .joinToString("") { it.first.toString() }
    }

    private fun decryptName(encryptedName: String, sectorId: Int): String {
        val shiftAmount = sectorId % 26
        return encryptedName.map {
            if (it == '-') it else ((((it - 'a') + shiftAmount) % 26) + 'a'.code).toChar()
        }.joinToString("")
    }
}

fun main() {
    execute(Day04::class)
}
