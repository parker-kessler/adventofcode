package advent.of.code

data class Equation(val target: Long, val amounts: List<Long>)

data class Step(val amount: Long, val index:Int, val debug: String)

@Files("/Day07.txt")
class Day07 : Puzzle<List<Equation>, Long> {

    override fun parse(input: List<String>): List<Equation> {
        return input.map { line ->
            "[0-9]+".toRegex().findAll(line).map { it.value.toLong() }.toList().let { i -> Equation(i.first(), i.drop(1)) }
        }
    }

    override fun partOne(input: List<Equation>): Long = input.filter { evaluate(it) }.sumOf { it.target }

    override fun partTwo(input: List<Equation>): Long = input.filter { evaluate(it, true) }.sumOf { it.target }

    private fun evaluate(equation: Equation, includeOr: Boolean = false): Boolean {
        val list = mutableListOf(Step(amount = equation.amounts.first(), index = 1, debug = equation.amounts.first().toString()))
        while (list.isNotEmpty()) {
            val step = list.removeLast() //DFS (removeLast) for part 2: 1.034159458s, BFS (removeFirst) for part 2: 2m 45.341065s
            if (step.index == equation.amounts.size) {
                if (step.amount == equation.target) {
                    return true
                }
            } else {
                if (step.amount >= equation.target) continue

                val current = equation.amounts[step.index]

                list.add(Step(amount = step.amount * current, index = step.index + 1, debug = step.debug + "*" + current))
                list.add(Step(amount = step.amount + current, index = step.index + 1, debug = step.debug + "+" + current))

                if (includeOr) {
                    list.add(Step(amount = (step.amount.toString() + current.toString()).toLong(), index = step.index + 1, debug = step.debug + "||" + current))
                }

            }
        }
        return false
    }
}

fun main() {
    execute(Day07::class)
}
