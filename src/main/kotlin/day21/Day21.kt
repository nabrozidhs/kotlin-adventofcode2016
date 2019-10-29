package day21

import util.permutations
import java.io.File
import kotlin.math.max
import kotlin.math.min

class State(var data: MutableList<Char>)

sealed class Command {

    abstract fun run(state: State)

    class SwapPosition(private val a: Int, private val b: Int) : Command() {

        override fun run(state: State) {
            val temp = state.data[a]
            state.data[a] = state.data[b]
            state.data[b] = temp
        }
    }

    class SwapChar(private val a: Char, private val b: Char) : Command() {

        override fun run(state: State) {
            val pos1 = state.data.indexOf(a)
            val pos2 = state.data.indexOf(b)
            val temp = state.data[pos1]
            state.data[pos1] = state.data[pos2]
            state.data[pos2] = temp
        }
    }

    class RotatePosition(private val pos: Int, private val isLeft: Boolean) : Command() {

        override fun run(state: State) {
            val newArray = MutableList(state.data.size) { ' ' }
            val movements = pos % state.data.size
            for (i in state.data.indices) {
                newArray[i] = if (isLeft) {
                    state.data[(i + movements) % state.data.size]
                } else {
                    state.data[(i - movements + state.data.size) % state.data.size]
                }
            }
            state.data = newArray
        }
    }

    class RotateChar(private val a: Char) : Command() {

        override fun run(state: State) {
            var pos = state.data.indexOf(a)
            if (pos >= 4) {
                pos += 1
            }

            RotatePosition(pos + 1, false).run(state)
        }
    }

    class Reverse(private val a: Int, private val b: Int) : Command() {

        override fun run(state: State) {
            val pos1 = min(a, b)
            val pos2 = max(a, b)
            val diff = pos2 - pos1
            for (i in pos1..(pos1 + diff / 2)) {
                SwapPosition(i, pos2 - (i - pos1)).run(state)
            }
        }
    }

    class Move(private val a: Int, private val b: Int) : Command() {

        override fun run(state: State) {
            val c = state.data.removeAt(a)
            state.data.add(b, c)
        }
    }
}

fun parse(lines: List<String>): List<Command> = lines.map {
    val split = it.split(" ")
    when (split[0]) {
        "swap" -> {
            if (split[1] == "position") {
                Command.SwapPosition(split[2].toInt(), split[5].toInt())
            } else {
                Command.SwapChar(split[2][0], split[5][0])
            }
        }
        "rotate" -> {
            if (split[1] == "left" || split[1] == "right") {
                Command.RotatePosition(split[2].toInt(), split[1] == "left")
            } else {
                Command.RotateChar(split[6][0])
            }
        }
        "reverse" -> Command.Reverse(split[2].toInt(), split[4].toInt())
        "move" -> Command.Move(split[2].toInt(), split[5].toInt())
        else -> throw IllegalArgumentException(it)
    }
}

fun day21part1(commands: List<Command>, start: String): String {
    val state = State(start.toCharArray().toMutableList())
    commands.forEach {
        it.run(state)
    }
    return state.data.joinToString(separator = "")
}

fun day21part2(commands: List<Command>, lookingFor: String): String {
    for (k in "abcdefgh".toCharArray().toList().permutations()) {
        val input = k.joinToString(separator = "")
        if (day21part1(commands, input) == lookingFor) {
            return input
        }
    }
    throw IllegalArgumentException()
}

fun main(args: Array<String>) {
    val commands = parse(File("data/day21/input.txt").readLines())
    println("part1 ${day21part1(commands, "abcdefgh")}")
    println("part2 ${day21part2(commands, "fbgdceah")}")
}
