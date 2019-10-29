package day24

import util.permutations
import java.io.File

typealias Position = Pair<Int, Int>

class State(
    private val walls: Set<Position>,
    val destinations: Map<Char, Position>
) {

    private fun adjacentPosition(pos: Position): List<Position> = listOf(
        pos.copy(first = pos.first - 1),
        pos.copy(first = pos.first + 1),
        pos.copy(second = pos.second - 1),
        pos.copy(second = pos.second + 1)
    ).filter { !walls.contains(it) }

    fun findPath(start: Char, end: Char): Int {
        val a = destinations[start] ?: error("")
        val b = destinations[end] ?: error("")

        val visited = mutableSetOf<Position>()
        val bfs = mutableListOf(listOf(a))
        var n = 0
        while (bfs.isNotEmpty()) {
            val check = bfs.removeAt(0)
            val next = mutableListOf<Position>()
            for (p in check) {
                if (visited.contains(p)) {
                    continue
                }

                if (p == b) {
                    return n
                }
                visited.add(p)
                next.addAll(adjacentPosition(p))
            }
            bfs.add(next)
            n += 1
        }

        throw IllegalArgumentException()
    }
}

fun parse(input: List<String>): State {
    val walls = mutableSetOf<Position>()
    val destinations = mutableMapOf<Char, Position>()

    for ((y, line) in input.withIndex()) {
        for ((x, c) in line.toCharArray().withIndex()) {
            when (c) {
                '#' -> walls.add(Pair(x, y))
                '.' -> {
                }
                else -> destinations[c] = Pair(x, y)
            }
        }
    }

    return State(walls, destinations)
}

fun day24(state: State, returnToBase: Boolean): Int {
    val distances = mutableMapOf<Pair<Char, Char>, Int>()
    for (key1 in state.destinations.keys) {
        for (key2 in state.destinations.keys) {
            distances[Pair(key1, key2)] = state.findPath(key1, key2)
        }
    }

    return state.destinations.keys.toMutableList().apply { remove('0') }.permutations()
        .map { listOf('0') + it + if (returnToBase) listOf('0') else emptyList() }
        .map { it.windowed(2, 1).map { distances[Pair(it[0], it[1])]!! }.sum() }
        .min()!!
}

fun main(args: Array<String>) {
    val state = parse(File("data/day24/input.txt").readLines())
    println("part1 ${day24(state, false)}")
    println("part2 ${day24(state, true)}")
}
