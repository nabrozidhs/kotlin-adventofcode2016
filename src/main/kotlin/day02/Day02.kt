package day02

import java.io.File
import java.lang.Integer.max
import java.lang.Integer.min

enum class Direction {UP, RIGHT, DOWN, LEFT }
data class Position(val x: Int, val y: Int)

val keypad = listOf(
        listOf(1, 2, 3),
        listOf(4, 5, 6),
        listOf(7, 8, 9)
)

fun trackAllDirections(directions: List<List<Direction>>, keypad: List<List<Int>>): List<List<Position>> {
    val positions = mutableListOf<List<Position>>()
    for (direction in directions) {
        val linePositions: List<Position>
        if (positions.size == 0) {
            linePositions = trackDirections(direction, Position(1, 1), keypad)
        } else {
            linePositions = trackDirections(direction, positions.last().last(), keypad)
        }
        positions.add(linePositions)
    }
    return positions
}

fun trackDirections(directions: List<Direction>, startingPosition: Position, keypad: List<List<Int>>): List<Position> {
    var position = startingPosition
    val positions = mutableListOf<Position>()
    for (direction in directions) {
        when (direction) {
            Direction.UP -> position = Position(position.x, max(position.y - 1, 0))
            Direction.DOWN -> position = Position(position.x, min(position.y + 1, keypad.size - 1))
            Direction.LEFT -> position = Position(max(position.x - 1, 0), position.y)
            Direction.RIGHT -> position = Position(min(position.x + 1, keypad[0].size - 1), position.y)
        }
        positions.add(position)
    }
    return positions
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

fun convertToPin(positions: List<List<Position>>, keypad: List<List<Int>>): String =
        positions.map { keypad[it.last().y][it.last().x] }.fold("", { s, e -> s + e })

fun main(args: Array<String>) {
    val directions = trackAllDirections(parse(File("data/day02/input.txt").readText()), keypad)

    println(directions)
    println(convertToPin(directions, keypad))
}
