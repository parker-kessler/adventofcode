package advent.of.code

data class ClawInput(val prize: Pair<Long, Long>, val aButton: Pair<Long, Long>, val bButton: Pair<Long, Long>)

@Files("/Day13.txt")
class Day13 : Puzzle<List<ClawInput>, Long> {

    companion object {
        const val A_COST = 3
        const val B_COST = 1
        const val OFFSET = 10_000_000_000_000
    }

    override fun parse(input: List<String>): List<ClawInput> = input.chunked(4).map { (one, two, three) ->
        ClawInput(
            aButton = parsePair(one),
            bButton = parsePair(two),
            prize = parsePair(three)
        )
    }


    private fun parsePair(line: String): Pair<Long, Long> = "[0-9]+".toRegex().findAll(line).map { it.value.toLong() }.let {it.first() to it.last() }

    override fun partOne(input: List<ClawInput>): Long = solve(input)

    override fun partTwo(input: List<ClawInput>): Long =
        solve(input.map { it.copy(prize = it.prize.first + OFFSET to it.prize.second + OFFSET) })

    private fun solve(input: List<ClawInput>): Long = input.sumOf { clawInput ->
        val (aX, aY) = clawInput.aButton
        val (bX, bY) = clawInput.bButton
        val (pX, pY) = clawInput.prize

        val denominator = aX * bY - bX * aY
        val firstNumerator = (bY*pX - bX*pY)
        val secondNumerator = (aX*pY - aY*pX)

        if (firstNumerator % denominator == 0L && secondNumerator % denominator == 0L) {
            firstNumerator / denominator * A_COST + secondNumerator / denominator * B_COST
        } else 0
    }
}

fun main() {
    execute(Day13::class)
}
