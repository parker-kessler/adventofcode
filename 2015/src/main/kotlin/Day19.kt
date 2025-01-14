package advent.of.code

import kotlin.math.min

@Files("/Day19.txt")
class Day19 : Puzzle<Day19.Input, Int> {

    data class Input(val equations: List<Pair<String, String>>, val molecule: String)

    override fun parse(input: List<String>): Input {
        val (equations, molecule) = input.filter { it.isNotBlank() }.partition { "=>" in it }
        return Input(
            equations = equations.map { it.split(" => ") }.map { it[0] to it[1] },
            molecule = molecule.first()
        )
    }

    override fun partOne(input: Input): Int {
        return input.equations.fold(mutableSetOf<String>()) { unique, equation ->
            equation.first.toRegex().findAll(input.molecule).forEach { match ->
                val before = input.molecule.substring(0..<match.range.first)
                val after = input.molecule.substring(match.range.last + 1)

                unique.add(before + equation.second + after)
            }
            unique
        }.size
    }

    override fun partTwo(input: Input): Int {
        var min = Int.MAX_VALUE
        val queue = mutableListOf(input.molecule to 0)
        val seen = mutableMapOf<String, Int>()

        val equations = input.equations.sortedByDescending { it.second.length }

        while (queue.isNotEmpty()) {
            val (word, replacements) = queue.removeLast()
            if (word == "e") {
                min = min(min, replacements)
                println(min)
                continue
            }

            equations.forEach { (to, fro) ->
                fro.toRegex().findAll(word).forEach { match ->
                    val before = word.substring(0..<match.range.first)
                    val after = word.substring(match.range.last + 1)

                    val newMolecule = before + to + after
                    if (seen.getOrDefault(newMolecule, Int.MAX_VALUE) > replacements) {
                        println(word)
                        println(newMolecule)
                        println("Replaced $fro with $to")
                        println("------------")
                        seen[newMolecule] = replacements
                        queue.add(newMolecule to replacements + 1)
                    }
                }
            }
        }
        return min
    }

}

fun main() {
    execute(Day19::class)
}
