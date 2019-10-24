package day12

import java.io.File

interface Command {
    fun run(state: State)
}

class State(startValueC: Long) {

    var idx = 0
    val registers = mutableMapOf<Char, Long>(
        'a' to 0,
        'b' to 0,
        'c' to startValueC,
        'd' to 0
    )
}

fun parse(input: List<String>): List<Command> {
    val commands = mutableListOf<Command>()

    for (line in input) {
        val splitted = line.split(" ")
        commands.add(
            when (splitted[0]) {
                "inc" -> {
                    val register = splitted[1].toCharArray()[0]
                    object : Command {
                        override fun run(state: State) {
                            state.registers[register] = state.registers.getOrDefault(register, 0) + 1
                        }
                    }
                }
                "dec" -> {
                    val register = splitted[1].toCharArray()[0]
                    object : Command {
                        override fun run(state: State) {
                            state.registers[register] = state.registers.getOrDefault(register, 0) - 1
                        }
                    }
                }
                "cpy" -> {
                    val v = splitted[1]
                    val dest = splitted[2].toCharArray()[0]
                    object : Command {
                        override fun run(state: State) {
                            val number = v.toLongOrNull()
                            if (number != null) {
                                state.registers[dest] = number
                            } else {
                                state.registers[dest] = state.registers.getOrDefault(v.toCharArray()[0], 0)
                            }
                        }
                    }
                }
                "jnz" -> {
                    val v = splitted[1].toCharArray()[0]
                    val number = splitted[1].toLongOrNull()
                    val diff = splitted[2].toInt()
                    object : Command {
                        override fun run(state: State) {
                            if ((number == null && state.registers.getOrDefault(v, 0) != 0L) ||
                                number?.let { it != 0L } == true
                            ) {
                                state.idx = state.idx + diff - 1
                            }
                        }
                    }
                }
                else -> throw IllegalArgumentException()
            }
        )
    }

    return commands
}

fun day12(commands: List<Command>, startValueC: Long): Long {
    val state = State(startValueC)

    while (state.idx < commands.size) {
        commands[state.idx].run(state)
        state.idx += 1
    }

    return state.registers.getOrDefault('a', 0)
}

fun main(args: Array<String>) {
    val commands = parse(File("data/day12/input.txt").readLines())
    println("part1 ${day12(commands, 0)}")
    println("part2 ${day12(commands, 1)}")
}
