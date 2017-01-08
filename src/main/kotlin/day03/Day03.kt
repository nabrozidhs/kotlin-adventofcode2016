package day03

import java.io.File

fun Triple<Int, Int, Int>.isTriangle(): Boolean {
    val (a, b, c) = toList().sorted()
    return a + b > c
}

val parseRegex = Regex("(\\d+) +(\\d+) +(\\d+)")
fun parse(input: String): List<Triple<Int, Int, Int>> =
        input.split("\n")
                .map {
                    val (unused, a, b, c) = parseRegex.find(it)!!.groupValues
                    Triple(a.toInt(), b.toInt(), c.toInt())
                }

fun main(args: Array<String>) {
    val triangles = parse(File("data/day03/input.txt").readText())

    println("Valid triangles: ${triangles.filter { it.isTriangle() }.count()}")
}