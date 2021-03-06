package day02

import java.io.File
import java.lang.Integer.max
import java.lang.Integer.min

enum class Direction { UP, RIGHT, DOWN, LEFT }
data class Position(val x: Int, val y: Int)

val keypad1 = listOf(
        listOf('1', '2', '3'),
        listOf('4', '5', '6'),
        listOf('7', '8', '9')
)
val keypad2 = listOf(
        listOf(' ', ' ', '1', ' ', ' '),
        listOf(' ', '2', '3', '4', ' '),
        listOf('5', '6', '7', '8', '9'),
        listOf(' ', 'A', 'B', 'C', ' '),
        listOf(' ', ' ', 'D', ' ', ' ')
)

fun trackAllDirections(directions: List<List<Direction>>, keypad: List<List<Char>>): List<Position> {
    val positions = mutableListOf<Position>()
    for (direction in directions) {
        positions.add(
                if (positions.size == 0) trackDirection(direction, Position(1, 1), keypad)
                else trackDirection(direction, positions.last(), keypad))
    }
    return positions
}

fun trackDirection(directions: List<Direction>, startingPosition: Position, keypad: List<List<Char>>): Position {
    var position = startingPosition
    directions
            .asSequence()
            .map {
                when (it) {
                    Direction.UP -> Position(position.x, max(position.y - 1, 0))
                    Direction.DOWN -> Position(position.x, min(position.y + 1, keypad.size - 1))
                    Direction.LEFT -> Position(max(position.x - 1, 0), position.y)
                    Direction.RIGHT -> Position(min(position.x + 1, keypad[0].size - 1), position.y)
                }
            }
            .filter { keypad[it.y][it.x] != ' ' }
            .forEach { position = it }
    return position
}

fun parse(input: String): List<List<Direction>> =
        input.split("\n")
                .map {
                    it.map {
                        when (it) {
                            'U' -> Direction.UP
                            'L' -> Direction.LEFT
                            'R' -> Direction.RIGHT
                            'D' -> Direction.DOWN
                            else -> throw IllegalArgumentException()
                        }
                    }
                }

fun convertToPin(positions: List<Position>, keypad: List<List<Char>>): String =
        positions.map { keypad[it.y][it.x] }.fold("", { s, e -> s + e })

fun main(args: Array<String>) {
    val directions1 = trackAllDirections(parse(File("data/day02/input.txt").readText()), keypad1)

    println(convertToPin(directions1, keypad1))

    val directions2 = trackAllDirections(parse(File("data/day02/input.txt").readText()), keypad2)

    println(convertToPin(directions2, keypad2))
}
