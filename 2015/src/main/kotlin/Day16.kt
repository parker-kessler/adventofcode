package advent.of.code

@Files("/Day16.txt")
class Day16 : Puzzle<List<Day16.Sue>, Int> {

    data class Sue(val number: Int, val attributes: Map<String, Int>)

    override fun parse(input: List<String>) = input.map { line ->
        val parts = line.split(" ")

        Sue(
            number = parts[1].removeSuffix(":").toInt(),
            attributes = mapOf(
                parts[2].removeSuffix(":") to parts[3].removeSuffix(",").toInt(),
                parts[4].removeSuffix(":") to parts[5].removeSuffix(",").toInt(),
                parts[6].removeSuffix(":") to parts[7].toInt()
            )
        )
    }

    override fun partOne(input: List<Sue>): Int {
        val known = knownAmounts()
        return input.first { sue ->
            sue.attributes.all { known[it.key] == it.value }
        }.number
    }

    override fun partTwo(input: List<Sue>): Int {
        val known = knownAmounts()
        val more = listOf("cats", "trees")
        val less = listOf("pomeranians", "goldfish")

        return input.first { sue ->
            sue.attributes.all { (key, value) ->
                known[key]?.let { knownAmount ->
                    when {
                        more.contains(key) -> value > knownAmount
                        less.contains(key) -> value < knownAmount
                        else -> knownAmount == value
                    }
                } ?: false
            }
        }.number
    }

    private fun knownAmounts(): Map<String, Int> {
        return """
            children: 3
            cats: 7
            samoyeds: 2
            pomeranians: 3
            akitas: 0
            vizslas: 0
            goldfish: 5
            trees: 3
            cars: 2
            perfumes: 1
        """.trimIndent().split("\n").associate { line ->
            line.split(": ").let { it[0] to it[1].toInt() }
        }
    }
}

fun main() {
    execute(Day16::class)
}
