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

fun parse2(input: String): List<Triple<Int, Int, Int>> {
    val lines = input.split("\n")
            .map {
                val (unused, a, b, c) = parseRegex.find(it)!!.groupValues
                listOf(a.toInt(), b.toInt(), c.toInt())
            }
    val triples = mutableListOf<Triple<Int, Int, Int>>()
    for (i in 0..lines.size-1 step 3) {
        (0..2).mapTo(triples) { Triple(lines[i][it], lines[i+1][it], lines[i+2][it]) }
    }
    return triples
}

fun main(args: Array<String>) {
    val triangles = parse(File("data/day03/input.txt").readText())
    val triangles2 = parse2(File("data/day03/input.txt").readText())

    println("Valid triangles: ${triangles.filter { it.isTriangle() }.count()}")
    println("Valid triangles2: ${triangles2.filter { it.isTriangle() }.count()}")
}