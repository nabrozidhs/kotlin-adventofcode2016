package day01

import java.io.File

data class Location(val x: Int, val y: Int)

enum class Orientation {
    NORTH, EAST, SOUTH, WEST
}

data class Path(val startLocation: Location,
                val endLocation: Location,
                val orientation: Orientation)

fun Path.routeTaken(): List<Location> {
    val route = mutableListOf<Location>()

    var nextLocation = startLocation
    while (nextLocation != endLocation) {
        nextLocation = when (orientation) {
            Orientation.NORTH -> Location(nextLocation.x, nextLocation.y + 1)
            Orientation.EAST -> Location(nextLocation.x + 1, nextLocation.y)
            Orientation.SOUTH -> Location(nextLocation.x, nextLocation.y - 1)
            Orientation.WEST -> Location(nextLocation.x - 1, nextLocation.y)
        }
        route.add(nextLocation)
    }
    return route
}

fun Orientation.turn(turn: Turn): Orientation {
    val orientationSize = Orientation.values().size
    return when (turn) {
        Turn.LEFT -> Orientation.values()[(ordinal + orientationSize - 1) % orientationSize]
        Turn.RIGHT -> Orientation.values()[(ordinal + orientationSize + 1) % orientationSize]
    }
}

fun Path.newMove(turn: Turn, moves: Int): Path {
    val newOrientation = orientation.turn(turn)
    val newLocation: Location
    newLocation = when (newOrientation) {
        Orientation.NORTH -> Location(endLocation.x, endLocation.y + moves)
        Orientation.EAST -> Location(endLocation.x + moves, endLocation.y)
        Orientation.SOUTH -> Location(endLocation.x, endLocation.y - moves)
        Orientation.WEST -> Location(endLocation.x - moves, endLocation.y)
    }
    return Path(endLocation, newLocation, newOrientation)
}

enum class Turn {
    LEFT, RIGHT
}

fun steps(commands: List<Pair<Turn, Int>>): List<Path> {
    val steps = mutableListOf<Path>()

    var currentPosition = Path(Location(0, 0), Location(0, 0), Orientation.NORTH)
    for (command in commands) {
        currentPosition = currentPosition.newMove(command.first, command.second)
        steps.add(currentPosition)
    }

    return steps
}

val parseRegex = Regex("([LR])(\\d+)")
fun parse(text: String): List<Pair<Turn, Int>> =
        text
                .split(",")
                .map { it ->
                    val (_, turn, moves) = parseRegex.find(it)!!.groupValues
                    Pair(if (turn == "L") Turn.LEFT else Turn.RIGHT, moves.toInt())
                }

fun Location.distanceFrom(other: Location) =
        Math.abs(x + other.x) + Math.abs(y + other.y)

fun findRepeatedLocation(steps: List<Path>): Location? {
    val visitedLocations = mutableSetOf(Location(0, 0))
    for (step in steps) {
        for (location in step.routeTaken()) {
            if (visitedLocations.contains(location)) {
                return location
            }
            visitedLocations.add(location)
        }
    }
    return null
}

fun main(args: Array<String>) {
    val steps = steps(parse(File("data/day01/input.txt").readText()))
    println(steps)

    println("Distance origin - end: ${steps[0].startLocation.distanceFrom(steps[steps.size - 1].endLocation)}")
    println("Distance origin - repeated path: ${steps[0].startLocation.distanceFrom(findRepeatedLocation(steps)!!)}")
}
