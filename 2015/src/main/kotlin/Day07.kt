package advent.of.code

@Files("/Day07.txt")
class Day07 : Puzzle<List<Day07.Operation>, Int> {

    sealed interface Operation

    data class InitialOperation(val input: Int, val output: String) : Operation
    data class MappingOperation(val input: String, val output: String) : Operation
    data class NotOperation(val input: String, val output: String) : Operation
    data class AndOperation(val left: String, val right: String, val output: String) : Operation
    data class OrOperation(val left: String, val right: String, val output: String) : Operation
    data class LeftShiftOperation(val input: String, val amount: Int, val output: String) : Operation
    data class RightShiftOperation(val input: String, val amount: Int, val output: String) : Operation

    override fun parse(input: List<String>) = input.map { line ->
        val split = line.split(" ")
        when {
            "NOT" in line -> NotOperation(split[1], split[3])
            "AND" in line -> AndOperation(split[0], split[2], split[4])
            "OR" in line -> OrOperation(split[0], split[2], split[4])
            "LSHIFT" in line -> LeftShiftOperation(split[0], split[2].toInt(), split[4])
            "RSHIFT" in line -> RightShiftOperation(split[0], split[2].toInt(), split[4])
            split[0].toIntOrNull() != null -> InitialOperation(split[0].toInt(), split[2])
            else -> MappingOperation(split[0], split[2])
        }
    }

    override fun partOne(input: List<Operation>): Int = evaluate(input)

    override fun partTwo(input: List<Operation>): Int = evaluate(input).let { a ->
        evaluate(input.map { if (it is InitialOperation && it.output == "b") it.copy(input = a) else it })
    }

    private fun evaluate(input: List<Operation>): Int {
        val map = mutableMapOf("1" to 1)
        val queue = input.toMutableList()
        while (queue.isNotEmpty()) {
            when (val operation = queue.removeFirst()) {
                is InitialOperation -> {
                    map[operation.output] = operation.input
                }

                is AndOperation -> {
                    if (map.containsKey(operation.left) && map.containsKey(operation.right)) {
                        map[operation.output] = map[operation.left]!! and map[operation.right]!!
                    } else queue.add(operation)
                }

                is OrOperation -> {
                    if (map.containsKey(operation.left) && map.containsKey(operation.right)) {
                        map[operation.output] = map[operation.left]!! or map[operation.right]!!
                    } else queue.add(operation)
                }

                is MappingOperation -> {
                    if (map.containsKey(operation.input)) {
                        map[operation.output] = map[operation.input]!!
                    } else queue.add(operation)
                }

                is NotOperation -> {
                    if (map.containsKey(operation.input)) {
                        map[operation.output] = map[operation.input]!!.inv() % 65535
                    } else queue.add(operation)
                }

                is LeftShiftOperation -> {
                    if (map.containsKey(operation.input)) {
                        map[operation.output] = map[operation.input]!! shl operation.amount
                    } else queue.add(operation)
                }

                is RightShiftOperation -> {
                    if (map.containsKey(operation.input)) {
                        map[operation.output] = map[operation.input]!! shr operation.amount
                    } else queue.add(operation)
                }
            }
        }

        return map["a"] ?: 0
    }
}

fun main() {
    execute(Day07::class)
}
