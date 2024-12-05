package advent.of.code

private enum class Classic(val opponent: Char, val me: Char, val shape: Int) {
    Rock('A', 'X', 1),
    Paper('B', 'Y', 2),
    Scissors('C', 'Z', 3);

    companion object {
        val opponentMap = entries.toTypedArray().associateBy { it.opponent }
        val meMap = entries.toTypedArray().associateBy { it.me }
    }
}

@Files("/Day02.txt")
class Day02 : Puzzle<List<String>, Int> {

    override fun parse(input: List<String>): List<String> = input

    override fun partOne(input: List<String>): Int {
        val win = 6
        val tie = 3
        val loss = 0

        return input.sumOf { line ->
            val opp = Classic.opponentMap[line[0]]!!
            val me = Classic.meMap[line[2]]!!

            me.shape + when (opp) {
                Classic.Rock -> when (me) {
                    Classic.Rock -> tie
                    Classic.Paper -> win
                    Classic.Scissors -> loss
                }
                Classic.Paper -> when (me) {
                    Classic.Rock -> loss
                    Classic.Paper -> tie
                    Classic.Scissors -> win
                }
                Classic.Scissors -> when (me) {
                    Classic.Rock -> win
                    Classic.Paper -> loss
                    Classic.Scissors -> tie
                }
            }
        }
    }

    override fun partTwo(input: List<String>): Int {
        val win = 6
        val tie = 3
        val loss = 0

        return input.sumOf { line ->
            val opp = Classic.opponentMap[line[0]]!!
            val me = Classic.meMap[line[2]]!!

            when (me) {
                Classic.Rock -> loss + when (opp) {
                    Classic.Rock -> Classic.Scissors.shape
                    Classic.Paper -> Classic.Rock.shape
                    Classic.Scissors -> Classic.Paper.shape
                }
                Classic.Paper -> tie + opp.shape
                Classic.Scissors -> win + when (opp) {
                    Classic.Rock -> Classic.Paper.shape
                    Classic.Paper -> Classic.Scissors.shape
                    Classic.Scissors -> Classic.Rock.shape
                }
            }
        }
    }
}

fun main() {
    execute(Day02::class)
}
