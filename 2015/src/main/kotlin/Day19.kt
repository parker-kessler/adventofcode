package advent.of.code

import java.util.Random
import java.util.regex.Matcher
import java.util.regex.Pattern


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
        val reverse = input.equations.associate { it.second to it.first }
        var result = -1
        while (result == -1) {
            result = findMolecule(0, input.molecule, reverse)
        }
        return result
    }

    private fun findMolecule(
        depth: Int,
        molecule: String,
        reverseReplacements: Map<String, String>
    ): Int {
        if (molecule == "e") {
            return depth
        }

        val keys: MutableList<String> = ArrayList(reverseReplacements.keys)
        var replaced = false
        var molecule = molecule
        while (!replaced) {
            val toReplace: String = keys.removeAt(Random().nextInt(keys.size))
            val matcher: Matcher = Pattern.compile(toReplace).matcher(molecule)
            if (matcher.find()) {
                molecule = replace(molecule, reverseReplacements[toReplace]!!, matcher)
                replaced = true
            }
            if (keys.isEmpty()) {
                return -1
            }
        }
        return findMolecule(depth + 1, molecule, reverseReplacements)
    }

    private fun replace(original: String, replacement: String, matcher: Matcher): String {
        val begin = matcher.start(0)
        val end = matcher.end(0)
        val newMolecule = StringBuilder()
        newMolecule.append(original.substring(0, begin))
        newMolecule.append(replacement)
        newMolecule.append(original.substring(end))
        return newMolecule.toString()
    }

}

fun main() {
    execute(Day19::class)
}
