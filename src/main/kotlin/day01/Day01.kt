package day01

import java.io.File

data class Location(val x: Int, val y: Int)

enum class Orientation {
    NORTH, EAST, SOUTH, WEST
}

data class Position(val location: Location, val orientation: Orientation)

fun Orientation.turn(turn: Turn): Orientation {
    val orientationSize = Orientation.values().size
    return when (turn) {
        Turn.LEFT -> Orientation.values()[(ordinal + orientationSize - 1) % orientationSize]
        Turn.RIGHT -> Orientation.values()[(ordinal + orientationSize + 1) % orientationSize]
    }
}

fun Position.move(turn: Turn, moves: Int): Position {
    val newOrientation = orientation.turn(turn)
    val newLocation: Location
    when (newOrientation) {
        Orientation.NORTH -> newLocation = Location(location.x, location.y + moves)
        Orientation.EAST -> newLocation = Location(location.x + moves, location.y)
        Orientation.SOUTH -> newLocation = Location(location.x, location.y - moves)
        Orientation.WEST -> newLocation = Location(location.x - moves, location.y)
    }
    return Position(newLocation, newOrientation)
}

enum class Turn {
    LEFT, RIGHT
}

fun steps(commands: List<Pair<Turn, Int>>): List<Position> {
    val steps = mutableListOf<Position>()

    var currentPosition = Position(Location(0, 0), Orientation.NORTH)
    steps.add(currentPosition)
    for (command in commands) {
        currentPosition = currentPosition.move(command.first, command.second)
        steps.add(currentPosition)
    }

    return steps
}

val parseRegex = Regex("(L|R)(\\d+)")
fun parse(text: String): List<Pair<Turn, Int>> =
        text
                .split(",")
                .map { it ->
                    val (unused, turn, moves) = parseRegex.find(it)!!.groupValues
                    Pair(if (turn == "L") Turn.LEFT else Turn.RIGHT, moves.toInt())
                }

fun Position.distanceFrom(other: Position) =
        Math.abs(location.x + other.location.x) + Math.abs(location.y + other.location.y)

fun findRepeatedPosition(steps: List<Position>): Position? {
    val foundLocations = mutableSetOf<Location>()
    for (step in steps) {
        val location = step.location
        if (foundLocations.contains(location)) {
            return step
        } else {
            foundLocations.add(location)
        }
    }
    return null
}

fun main(args: Array<String>) {
    val steps = steps(parse(File("data/day01/input.txt").readText()))
    println(steps)

    println("Distance origin - end: ${steps[0].distanceFrom(steps[steps.size - 1])}")
    println("Distance origin - repeated path: ${steps[0].distanceFrom(findRepeatedPosition(steps)!!)}")
}
