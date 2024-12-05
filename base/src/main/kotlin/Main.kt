package advent.of.code

import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.findAnnotation
import kotlin.time.measureTime

interface Puzzle<T, S> {

    fun parse(input: List<String>): T

    fun partOne(input: T): S

    fun partTwo(input: T): S
}

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Input(vararg val files: String)

fun <A, B, T : Puzzle<A, B>> execute(clazz: KClass<T>) {
    // Check if the class has the FileContent annotation
    val instance = clazz.createInstance()
    val annotation = clazz.findAnnotation<Input>()

    if (annotation != null) {
        val files = annotation.files
        try {
            println("Executing: ${clazz.simpleName}")

            files.forEach { file ->
                println("File: $file")

                val input = clazz.java.getResourceAsStream(file)!!.bufferedReader().readLines().let { instance.parse(it) }

                measureTime {
                    println("Part one solution is ${instance.partOne(input)}")
                }.also { println("Part one solution took $it ms") }

                measureTime {
                    println("Part two solution is ${instance.partTwo(input)}")
                }.also { println("Part two solution took $it ms") }
            }
        } catch (e: Exception) {
            println("Error reading file: ${e.message}")
        }
    } else {
        println("No Input annotation found on class ${clazz.simpleName}.")
    }
}
