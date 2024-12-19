package advent.of.code

@Files("/Day07.txt")
class Day07 : Puzzle<List<Pair<String, Int>>, Int> {

    override fun parse(input: List<String>): List<Pair<String, Int>> =
        input.map { it.split(" ") }.map { (hand, bet) -> hand to bet.toInt() }

    override fun partOne(input: List<Pair<String, Int>>) = input.sortedWith(cardSorter).calculateWinnings()

    override fun partTwo(input: List<Pair<String, Int>>) = input.sortedWith(jokerSorter).calculateWinnings()

    private val cardSorter = Comparator<Pair<String, Int>> { pair1, pair2 ->
        val rank1 = getNormalRank(pair1.first)
        val rank2 = getNormalRank(pair2.first)
        if (rank1 != rank2) {
            return@Comparator rank2 - rank1
        }

        val (c1, c2) = pair1.first.zip(pair2.first).first { (c1, c2) -> c1 != c2 }
        return@Comparator "AKQJT98765432".indexOf(c2) - "AKQJT98765432".indexOf(c1)
    }

    private val jokerSorter = Comparator<Pair<String, Int>> { pair1, pair2 ->
        val rank1 = getJokerRank(pair1.first)
        val rank2 = getJokerRank(pair2.first)
        if (rank1 != rank2) {
            return@Comparator rank2 - rank1
        }

        val (c1, c2) = pair1.first.zip(pair2.first).first { (c1, c2) -> c1 != c2 }
        return@Comparator "AKQT98765432J".indexOf(c2) - "AKQT98765432J".indexOf(c1)
    }

    private fun getNormalRank(hand: String): Int = getRank(hand.groupingBy { it }.eachCount(), 0)

    private fun getJokerRank(hand: String): Int =
        hand.groupingBy { it }.eachCount().toMutableMap().run {
            getRank(this, this.remove('J') ?: 0)
        }

    private fun getRank(counted: Map<Char, Int>, jokers: Int) =
        counted.values.sortedDescending().run {
            when {
                isEmpty() || this[0] + jokers == 5 -> 1
                this[0] + jokers == 4 -> 2
                this[0] + jokers == 3 && this[1] == 2 -> 3
                this[0] + jokers == 3 -> 4
                this[0] + jokers == 2 && this[1] == 2 -> 5
                this[0] + jokers == 2 -> 6
                else -> 7
            }
        }

    private fun List<Pair<String, Int>>.calculateWinnings() = mapIndexed { i, pair -> (i + 1) * pair.second }.sum()
}

fun main() {
    execute(Day07::class)
}
