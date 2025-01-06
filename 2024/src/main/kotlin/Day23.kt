package advent.of.code

@Files("/Day23.txt")
class Day23 : Puzzle<List<Pair<String, String>>, String> {

    override fun parse(input: List<String>) = input.map { line ->
        line.split("-").let { if (it[0] < it[1]) it[0] to it[1] else it[1] to it[0] }
    }

    override fun partOne(input: List<Pair<String, String>>): String {
        val relationships = mutableMapOf<String, MutableList<String>>()
        val unique = mutableSetOf<String>()

        input.forEach {
            relationships.computeIfAbsent(it.first) { mutableListOf() }.add(it.second)
            relationships.computeIfAbsent(it.second) { mutableListOf() }.add(it.first)
            unique.add(it.first + it.second)
            unique.add(it.second + it.first)
        }

        val uniqueTriple = mutableSetOf<String>()
        relationships.entries.filter { it.key[0] == 't' }.forEach { (key, value) ->

            for (i in value.indices) {
                for (j in i+1 until value.size) {
                    if (unique.contains(value[i] + value[j])) {
                        uniqueTriple.add(listOf(key, value[i], value[j]).sorted().joinToString())
                    }
                }
            }
        }

        return uniqueTriple.size.toString()
    }

    override fun partTwo(input: List<Pair<String, String>>): String {
        val relationships = mutableMapOf<String, MutableList<String>>()
        val unique = mutableSetOf<String>()

        input.forEach {
            relationships.computeIfAbsent(it.first) { mutableListOf() }.add(it.second)
            relationships.computeIfAbsent(it.second) { mutableListOf() }.add(it.first)
            unique.add(it.first + it.second)
            unique.add(it.second + it.first)
        }

        return relationships.map { (key, value) ->
            val list = mutableListOf(key)

            for (i in value.indices) {
                var allRelate = true
                for (j in i+1 until value.size) {
                    if (!unique.contains(value[i] + value[j])) {
                        allRelate = false
                        break
                    }
                }
                if (allRelate) {
                    list.add(value[i])
                }
            }

            list
        }.maxBy { it.size }.sorted().joinToString(separator = ",")
    }
}


fun main() {
    execute(Day23::class)
}
