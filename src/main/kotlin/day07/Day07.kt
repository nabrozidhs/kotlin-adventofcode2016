package day07

import java.io.File

fun isValidAbba(line: String): Boolean {
    var inBrackets = false
    var found = false
    (0 until line.length - 3).forEach {
        when (line[it]) {
            '[' -> inBrackets = true
            ']' -> inBrackets = false
            else -> {
                if (line[it] == line[it + 3]
                        && line[it + 1] == line[it + 2]
                        && line[it] != line[it + 1]) {
                    if (inBrackets) return false
                    else found = true
                }
            }
        }
    }
    return found
}

fun isValidSsl(line: String): Boolean {
    var inBrackets = false
    val seqOutsideBrackets = mutableSetOf<String>()
    val seqInBrackets = mutableSetOf<String>()
    (0 until line.length - 2).forEach {
        when (line[it]) {
            '[' -> inBrackets = true
            ']' -> inBrackets = false
            else -> {
                if (line[it] == line[it + 2] && line[it] != line[it + 1]) {
                    if (inBrackets) seqInBrackets.add("${line[it + 1]}${line[it]}${line[it + 1]}")
                    else seqOutsideBrackets.add(line.substring(it, it + 3))
                }
            }
        }
    }
    return seqOutsideBrackets.any { seqInBrackets.contains(it) }
}

fun day07(input: List<String>) = Pair(input.filter { isValidAbba(it) }.count(), input.filter { isValidSsl(it) }.count())

fun main(args: Array<String>) {
    val (part1, part2) = day07(File("data/day07/input.txt").readText().split("\n"))
    println("part1 $part1")
    println("part2 $part2")
}