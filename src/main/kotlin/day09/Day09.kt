package day09

import java.io.File

fun String.findNext(c: Char): Int? =
        (0 until this.length).firstOrNull { this[it] == c }

fun day09part1(input: String): Int {
    var copy = input

    var size = 0
    var next = copy.findNext('(')
    while (next != null) {
        val end = copy.findNext(')')!!
        val values = copy.substring(next + 1, end).split('x')

        copy = copy.substring(end + 1 + values[0].toInt())
        size += (values[0].toInt() * values[1].toInt()) + next

        next = copy.findNext('(')
    }

    return size + copy.length
}

fun day09part2(input: String): Long {
    var copy = input
    var next = input.findNext('(')
    if (next == null) {
        return input.length.toLong()
    } else {
        var size = 0L

        while (next != null) {
            val end = copy.findNext(')')!!
            val values = copy.substring(next + 1, end).split('x')

            size += values[1].toLong() * day09part2(copy.substring(end + 1, end + 1 + values[0].toInt())) + next

            copy = copy.substring(end + 1 + values[0].toInt())
            next = copy.findNext('(')
        }

        return size
    }
}

fun main(args: Array<String>) {
    val input = File("data/day09/input.txt").readText().split("\n")[0]
    println("part1 ${day09part1(input)}")
    println("part2 ${day09part2(input)}")
}