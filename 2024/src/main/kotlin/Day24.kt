package advent.of.code

@Files("/Day24.txt")
class Day24 : Puzzle<Day24.Input, String> {

    data class Input(val wires: Map<String, Boolean>, val rules: List<Rule>)
    data class Rule(val left: String, val operand: String, val right: String, val output: String)

    override fun parse(input: List<String>): Input {
        val wires = mutableMapOf<String, Boolean>()
        val rules = mutableListOf<Rule>()

        input.forEach { line ->
            when {
                ":" in line -> line.split(": ").let { wires.put(it[0], it[1][0] == '1') }
                "->" in line -> line.split(" ").let { rules.add(Rule(it[0], it[1], it[2], it[4])) }
            }
        }

        return Input(wires, rules)
    }

    override fun partOne(input: Input): String {
        val wires = input.wires.toMutableMap()

        val queue = input.rules.toMutableList()
        while (queue.isNotEmpty()) {
            val rule = queue.removeFirst()

            val left = wires[rule.left]
            val right = wires[rule.right]

            if (left != null && right != null) {
                wires[rule.output] = when(rule.operand) {
                    "OR" -> left || right
                    "AND" -> left && right
                    "XOR" -> left != right
                    else -> error("Unrecognized operand: ${rule.operand}")
                }
            } else {
                queue.add(rule)
            }
        }
        return wires.findWire('z')
    }

    private fun Map<String, Boolean>.findWire(prefix: Char): String {
        return entries
            .filter { (k, _) -> k.startsWith(prefix) }
            .sortedByDescending { it.key }
            .joinToString(separator = "") { if (it.value) "1" else "0" }
            .toLong(2)
            .toString()
    }

    override fun partTwo(input: Input): String {
        var file = input.wires.entries.joinToString(separator = "\n") { "${it.key}: ${if (it.value) "1" else "0"}" }
        file += "\n\n"
        file += input.rules.joinToString(separator = "\n") { "${it.left} ${it.operand} ${it.right} -> ${it.output}" }
        return Cheater().solve(file)
    }
}


fun main() {
    execute(Day24::class)
}


class Cheater {
    private enum class Gate { AND, OR, XOR, INPUT }
    private data class Wire(var name: String, var value: Boolean?, val gate: Gate, val input1: String?, val input2: String?)

    private fun MutableList<Wire>.run() = this
        .filter { Regex("""z\d+""").matches(it.name) }
        .sortedByDescending { it.name }
        .map { if (getWireValue(this, it)) 1 else 0 }
        .joinToString("")
        .toLong(2)

    private fun getCircuit(input: String): MutableList<Wire> {
        val circuit = Regex("""(\w+): (\d)""").findAll(input).map {
            val (name, value) = it.destructured
            Wire(name, value == "1", Gate.INPUT, null, null)
        }.toMutableList()

        Regex("""(\w+) (AND|OR|XOR) (\w+) -> (\w+)""").findAll(input).forEach {
            val (input1, gate, input2, output) = it.destructured
            circuit.add(Wire(output, null, Gate.valueOf(gate), input1, input2))
        }

        return circuit
    }

    private fun getWireValue(circuit: MutableList<Wire>, wire: Wire): Boolean {
        if (wire.value != null) return wire.value!!

        val input1 = circuit.first { inputWire -> inputWire.name == wire.input1 }
        val input2 = circuit.first { inputWire -> inputWire.name == wire.input2 }
        val value = when (wire.gate) {
            Gate.INPUT -> wire.value
            Gate.AND -> getWireValue(circuit, input1) and getWireValue(circuit, input2)
            Gate.OR -> getWireValue(circuit, input1) or getWireValue(circuit, input2)
            Gate.XOR -> getWireValue(circuit, input1) xor getWireValue(circuit, input2)
        }

        wire.value = value
        return value!!
    }

    private fun findFirstOutputFrom(circuit: MutableList<Wire>, wire: String): String? {
        val parents = circuit.filter { it.input1 == wire || it.input2 == wire }

        val validOutput = parents.find { it.name.first() == 'z' }
        if (validOutput == null) return parents.firstNotNullOfOrNull { findFirstOutputFrom(circuit, it.name) }

        val previousOutputNumber = validOutput.name.drop(1).toInt() - 1
        return "z" + previousOutputNumber.toString().padStart(2, '0')
    }

    private fun interpretWireAsNumber(start: Char, circuit: List<Wire>) = circuit
        .filter { it.name.first() == start }
        .sortedByDescending(Wire::name)
        .map { if (it.value!!) '1' else '0' }
        .joinToString("")
        .toLong(2)

    fun solve(input: String): String {
        val circuit = getCircuit(input)
        val invalidEndWires = circuit.filter {
            it.name.first() == 'z' && it.name != "z45" && it.gate != Gate.XOR
        }

        val invalidMidWires = circuit.filter {
            it.name.first() != 'z'
                    && it.input1?.first() != 'x' && it.input1?.first() != 'y'
                    && it.input2?.first() != 'x' && it.input2?.first() != 'y'
                    && it.gate == Gate.XOR
        }

        invalidMidWires.forEach { wire ->
            val toSwitch = invalidEndWires.first { it.name == findFirstOutputFrom(circuit, wire.name) }
            val temp = wire.name
            wire.name = toSwitch.name
            toSwitch.name = temp
        }

        val xInput = interpretWireAsNumber('x', circuit)
        val yInput = interpretWireAsNumber('y', circuit)

        val diffFromActual = xInput + yInput xor circuit.run()
        val zeroBits = diffFromActual
            .countTrailingZeroBits()
            .toString()
            .padStart(2, '0')

        val invalidCarryWires = circuit.filter {
            it.input1?.endsWith(zeroBits) == true
                    && it.input2?.endsWith(zeroBits) == true
        }

        return (invalidEndWires + invalidMidWires + invalidCarryWires)
            .map { it.name }
            .sorted()
            .joinToString(",")
    }
}
