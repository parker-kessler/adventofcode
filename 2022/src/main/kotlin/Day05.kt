package advent.of.code

data class Move(val amount: Int, val from: Int, val to: Int)

@Files("/Day05.txt")
class Day05 : Puzzle<Pair<Map<Int, MutableList<Char>>, List<Move>>, String> {

    override fun parse(input: List<String>): Pair<Map<Int, MutableList<Char>>, List<Move>> {
        val crates = mutableMapOf<Int, MutableList<Char>>()
        val moves = mutableListOf<Move>()

        input.forEach { line ->
            when {
                "[" in line -> {
                    "[A-Z]+".toRegex().findAll(line).forEach { match ->
                        crates.computeIfAbsent((match.range.first - 1) / 4 + 1) { mutableListOf() }.add(0, match.value[0])
                    }
                }
                "move" in line -> {
                    "[0-9]+".toRegex().findAll(line).map { it.value.toInt() }.toList().let { (amount, from, to) -> moves.add(Move(amount, from, to)) }
                }
            }
        }

        return crates to moves
    }

    override fun partOne(input: Pair<Map<Int, MutableList<Char>>, List<Move>>): String {
        val (crates, moves) = input

        moves.forEach { move ->
            val toCrate = crates[move.to]
            val fromCrate = crates[move.from]

            if (toCrate != null && fromCrate != null) {
                repeat(move.amount) {
                    toCrate.add(fromCrate.removeLast())
                }
            }
        }

        return crates.entries.sortedBy { it.key }.joinToString(separator = "") { it.value.last().toString() }
    }

    override fun partTwo(input: Pair<Map<Int, MutableList<Char>>, List<Move>>): String {
        val (crates, moves) = input

        moves.forEach { move ->
            val toCrate = crates[move.to]
            val fromCrate = crates[move.from]
            if (toCrate != null && fromCrate != null) {
                val index = fromCrate.size - move.amount
                repeat(move.amount) {
                    toCrate.add(fromCrate.removeAt(index))
                }
            }
        }

        return crates.entries.sortedBy { it.key }.joinToString(separator = "") { it.value.last().toString() }
    }
}

fun main() {
    execute(Day05::class)
}
