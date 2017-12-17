package day08

import java.io.File

sealed class Command {
    abstract fun run(screen: MutableList<MutableList<Boolean>>)
}

class Rect(private val x: Int, private val y: Int) : Command() {
    override fun run(screen: MutableList<MutableList<Boolean>>) {
        for (i in 0 until x) {
            for (k in 0 until y) {
                screen[k][i] = true
            }
        }
    }
}

class RotateColumn(private val x: Int, private val quantity: Int) : Command() {
    override fun run(screen: MutableList<MutableList<Boolean>>) {
        val columnCopy = mutableListOf<Boolean>()
        screen.forEach { columnCopy.add(it[x]) }

        (0 until screen.size).forEach {
            screen[(it + quantity) % columnCopy.size][x] = columnCopy[it]
        }
    }
}

class RotateRow(private val y: Int, private val quantity: Int) : Command() {
    override fun run(screen: MutableList<MutableList<Boolean>>) {
        val rowCopy = ArrayList(screen[y])

        (0 until rowCopy.size).forEach {
            screen[y][(it + quantity) % rowCopy.size] = rowCopy[it]
        }
    }
}

val parseRect = Regex("^rect (\\d+)x(\\d+)$")
val parseRotateColumn = Regex("^rotate column x=(\\d+) by (\\d+)$")
val parseRotateRow = Regex("^rotate row y=(\\d+) by (\\d+)$")

fun processCommand(input: String): Command {
    val rectResult = parseRect.find(input)
    if (rectResult != null) {
        return Rect(rectResult.groupValues[1].toInt(), rectResult.groupValues[2].toInt())
    }

    val rotateColumnResult = parseRotateColumn.find(input)
    if (rotateColumnResult != null) {
        return RotateColumn(rotateColumnResult.groupValues[1].toInt(), rotateColumnResult.groupValues[2].toInt())
    }

    val rotateRowResult = parseRotateRow.find(input)
    if (rotateRowResult != null) {
        return RotateRow(rotateRowResult.groupValues[1].toInt(), rotateRowResult.groupValues[2].toInt())
    }

    throw IllegalArgumentException()
}

fun day08(input: List<String>): Pair<Int, String> {
    val screen: MutableList<MutableList<Boolean>> = mutableListOf()

    (0 until 6).forEach {
        val row = mutableListOf<Boolean>()
        (0 until 50).forEach { row.add(false) }
        screen.add(row)
    }

    input.map { processCommand(it) }.forEach { it.run(screen) }

    return Pair(
            screen.map { it.filter { it }.count() }.sum(),
            screen.joinToString(separator = "\n") { it.joinToString(separator = "") { if (it) "#" else "." } })
}

fun main(args: Array<String>) {
    val (part1, part2) = day08(File("data/day08/input.txt").readText().split("\n"))
    println("part1 $part1")
    println("part2 \n$part2")
}