package day06

import java.io.File

val testInput = "eedadn\n" +
        "drvtee\n" +
        "eandsr\n" +
        "raavrd\n" +
        "atevrs\n" +
        "tsrnev\n" +
        "sdttsa\n" +
        "rasrtv\n" +
        "nssdts\n" +
        "ntnada\n" +
        "svetve\n" +
        "tesnvt\n" +
        "vntsnd\n" +
        "vrdear\n" +
        "dvrsen\n" +
        "enarar"

fun day06(input: List<String>): Pair<String, String> {
    val m: MutableList<MutableList<Char>> = mutableListOf()
    (0 until input[0].length).forEach { m.add(mutableListOf()) }

    for (line in input) {
        line.toCharArray()
                .forEachIndexed { i, c -> m[i].add(c) }
    }

    return Pair(
            m.map { it.groupBy { it }.maxBy { it.value.size }!!.value[0] }
                    .joinToString(separator = ""),
            m.map { it.groupBy { it }.minBy { it.value.size }!!.value[0] }
                    .joinToString(separator = ""))
}

fun main(args: Array<String>) {
    val (part1, part2) = day06(File("data/day06/input.txt").readText().split("\n"))
    println("part1: $part1")
    println("part2: $part2")
}