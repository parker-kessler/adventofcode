package advent.of.code

import kotlin.math.min

@Files("/Day11.txt")
class Day11 : Puzzle<List<Day11.Floor>, Int> {

    data class Floor(val microchips: List<String>, val generators: List<String>) {
        fun isEmpty() = microchips.isEmpty() && generators.isEmpty()
    }

    data class State(val floors: List<Floor>, val moves: Int, val elevator: Int)

    override fun parse(input: List<String>) = input.map { line ->
        val microchips = mutableListOf<String>()
        val generators = mutableListOf<String>()

        "[a-z]+-compatible microchip|[a-z]+ generator".toRegex().findAll(line).forEach {
            when {
                "microchip" in it.value -> microchips.add(it.value.substringBefore('-'))
                else -> generators.add(it.value.substringBefore(' '))
            }
        }

        Floor(microchips = microchips, generators = generators)
    }

    override fun partOne(input: List<Floor>): Int {
        val total = input.sumOf { it.microchips.size + it.generators.size }
        val seen = mutableMapOf<String, Int>()
        val answer = IntArray(1) { Int.MAX_VALUE }

        findMinimum(elevator = 0, floors = input.toMutableList(), seen = seen, total, moves = 0, answer)

        return answer[0]
    }

    private fun findMinimum(elevator: Int, floors: MutableList<Floor>, seen: MutableMap<String, Int>, total: Int, moves: Int, answer: IntArray) {
        if (floors.last().let { it.generators.size + it.microchips.size } == total) {
            answer[0] = minOf(answer[0], moves)
            return
        }

        if (seen.compute(floors.hash()) { _, v-> min(v ?: Int.MAX_VALUE, moves) } != moves) return

        if (!isValid(floors)) {
            return
        }

        if (answer[0] < moves) {
            return
        }

        val floor = floors[elevator]

        if (elevator != 0 && floors.take(elevator).any { it.microchips.isNotEmpty() || it.generators.isNotEmpty() }) {
            val below = floors[elevator - 1]
            // Take down one chip
            floor.microchips.forEach { microchip ->
                floors[elevator] = floor.copy(microchips = floor.microchips - microchip)
                floors[elevator - 1] = below.copy(microchips = below.microchips + microchip)

                findMinimum(elevator - 1, floors, seen, total, moves + 1, answer)
            }

            floor.generators.forEach { generator ->
                floors[elevator] = floor.copy(generators = floor.generators - generator)
                floors[elevator - 1] = below.copy(generators = below.generators + generator)

                findMinimum(elevator - 1, floors, seen, total, moves + 1, answer)
            }

            floors[elevator - 1] = below
        }

        if (elevator != floors.size - 1) {
            val above = floors[elevator + 1]

            floor.microchips.forEachIndexed { index, microchip ->
                //Take up one chip
                floors[elevator] = floor.copy(microchips = floor.microchips - microchip)
                floors[elevator + 1] = above.copy(microchips = above.microchips + microchip)

                findMinimum(elevator + 1, floors, seen, total, moves + 1, answer)

                //Take up two chips
                floor.microchips.drop(index + 1).forEach { secondChip ->
                    floors[elevator] = floor.copy(microchips = floor.microchips - setOf(microchip, secondChip))
                    floors[elevator + 1] = above.copy(microchips = above.microchips + setOf(microchip, secondChip))

                    findMinimum(elevator + 1, floors, seen, total, moves + 1, answer)
                }

                //Take up chip + generator
                if (floor.generators.contains(microchip)) {
                    floors[elevator] = floor.copy(microchips = floor.microchips - microchip, generators = floor.generators - microchip)
                    floors[elevator + 1] = above.copy(microchips = above.microchips + microchip, generators = above.generators + microchip)

                    findMinimum(elevator + 1, floors, seen, total, moves + 1, answer)
                }
            }

            floor.generators.forEachIndexed { index, generator ->
                //Take up one generator
                floors[elevator] = floor.copy(generators = floor.generators - generator)
                floors[elevator + 1] = above.copy(generators = above.generators + generator)

                findMinimum(elevator + 1, floors, seen, total, moves + 1, answer)

                //Take up two generators
                floor.generators.drop(index + 1).forEach { secondGenerator ->
                    floors[elevator] = floor.copy(generators = floor.generators - setOf(generator, secondGenerator))
                    floors[elevator + 1] = above.copy(generators = above.generators + setOf(generator, secondGenerator))

                    findMinimum(elevator + 1, floors, seen, total, moves + 1, answer)
                }
            }

            floors[elevator + 1] = above
        }

        floors[elevator] = floor
    }

    private fun isValid(floors: List<Floor>) = floors.all { floor -> floor.generators.isEmpty() || floor.microchips.all { floor.generators.contains(it) } }

    private fun List<Floor>.hash() = joinToString { it.hash() }

    private fun Floor.hash() = microchips.sorted().joinToString() + ":" + generators.sorted().joinToString()

    override fun partTwo(input: List<Floor>): Int {
        val first = input.first()
        val new = listOf("elerium", "dilithium")
        val floors = listOf(first.copy(microchips = first.microchips + new, generators = first.generators + new)) + input.drop(1)

        return solve(floors.map { it.microchips.size + it.generators.size })
    }

    private fun solve(items: List<Int>): Int =
        (1..<items.size).sumOf { 2 * items.subList(0, it).sum() - 3 }
}

fun main() {
    execute(Day11::class)
}
