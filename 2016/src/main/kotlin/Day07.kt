package advent.of.code

@Files("/Day07.txt")
class Day07 : Puzzle<List<Day07.IPAddress>, Int> {

    data class IPAddress(val outsideBrackets: List<String>, val insideBrackets: List<String>)

    override fun parse(input: List<String>) = input.map { line ->
        val outsideBrackets = mutableListOf<String>()
        val insideBrackets = mutableListOf<String>()

        var process = line
        while (process.contains('[')) {
            outsideBrackets.add(process.substringBefore('['))
            process = process.substringAfter('[')
            insideBrackets.add(process.substringBefore(']'))
            process = process.substringAfter(']')
        }
        outsideBrackets.add(process)

        IPAddress(
            outsideBrackets = outsideBrackets,
            insideBrackets = insideBrackets
        )
    }

    override fun partOne(input: List<IPAddress>): Int {
        return input.count { ipAddress ->
            ipAddress.outsideBrackets.any { it.hasAbbaSequence() } && ipAddress.insideBrackets.none { it.hasAbbaSequence() }
        }
    }

    override fun partTwo(input: List<IPAddress>): Int {
        return input.count { ipAddress ->
            val sequences = ipAddress.outsideBrackets.flatMap { it.getFlippedAbaSequence() }
            ipAddress.insideBrackets.any { insideBracket -> sequences.any { insideBracket.contains(it) } }
        }
    }

    private fun String.hasAbbaSequence(): Boolean {
        return (0 until length - 3).any {
            this[it] == this[it + 3] && this[it + 1] == this[it + 2] && this[it] != this[it + 1]
        }
    }

    private fun String.getFlippedAbaSequence(): List<String> {
        return (0 until length - 2)
            .filter { this[it] == this[it + 2] && this[it] != this[it + 1] }
            .map { "${this[it+1]}${this[it]}${this[it+1]}" }
    }
}

fun main() {
    execute(Day07::class)
}
