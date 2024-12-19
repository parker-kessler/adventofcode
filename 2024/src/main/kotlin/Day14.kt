package advent.of.code

@Files("/Day14.txt")
class Day14 : Puzzle<List<Day14.Robot>, Long> {

    data class Robot(val p: Pair<Int, Int>, val v: Pair<Int, Int>)

    companion object {
        const val MAX_X = 101
        const val MAX_Y = 103
        const val SECONDS = 100
    }

    override fun parse(input: List<String>): List<Robot> = input.map { line ->
        "-?[0-9]+".toRegex().findAll(line).map { it.value.toInt() }.toList().let { (pX, pY, vX, vY) ->
            Robot(pX to pY, vX to vY)
        }
    }

    override fun partOne(input: List<Robot>): Long {
        val robots = (0 until SECONDS).fold(input) { robots, _ -> robots.map { it.move() } }

        val quadrants = MutableList(4) { 0L }
        robots.forEach { robot ->
            when {
                robot.p.first < (MAX_X / 2) && robot.p.second < (MAX_Y / 2) -> quadrants[0]++
                robot.p.first > (MAX_X / 2) && robot.p.second < (MAX_Y / 2) -> quadrants[1]++
                robot.p.first < (MAX_X / 2) && robot.p.second > (MAX_Y / 2) -> quadrants[2]++
                robot.p.first > (MAX_X / 2) && robot.p.second > (MAX_Y / 2) -> quadrants[3]++
            }
        }
        return quadrants.reduce { acc, i -> i * acc }
    }

    override fun partTwo(input: List<Robot>): Long {
        var robots = input
        return (1..MAX_X * MAX_Y * 1L).first {
            robots = robots.map { it.move() }
            robots.size == robots.map { it.p }.toSet().size
        }
    }

    private fun Robot.move(): Robot =
        copy(p = (p.first + v.first + MAX_X) % MAX_X to (p.second + v.second + MAX_Y) % MAX_Y)

}

fun main() {
    execute(Day14::class)
}
