import java.io.File

private fun getInput(): List<String> = File("/Users/parkerkessler/Library/Application Support/JetBrains/IntelliJIdea2024.3/scratches/advent/2022-04/Day01.txt").readLines()

private fun partOne(): Int {
    return getInput().count { line ->
        val (left, right) = line.split(",")
        val (leftLower, leftUpper) = left.split("-").map { it.toInt() }
        val (rightLower, rightUpper) = right.split("-").map { it.toInt() }

        (leftLower <= rightLower && rightUpper <= leftUpper) || (rightLower <= leftLower && leftUpper <= rightUpper)
    }
}

private fun partTwo(): Int {
    return getInput().count { line ->
        val (left, right) = line.split(",")
        val (leftLower, leftUpper) = left.split("-").map { it.toInt() }
        val (rightLower, rightUpper) = right.split("-").map { it.toInt() }

        leftLower <= rightUpper && rightLower <= leftUpper
    }
}

println(partOne())
println(partTwo())
