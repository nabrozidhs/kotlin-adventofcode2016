package day25

import java.io.File

interface Command {
    fun run(state: State)
}

class State(startValueA: Long) {

    var idx = 0
    var previous: Long? = null
    val registers = mutableMapOf('a' to startValueA)
}

fun parse(input: List<String>): List<Command> {
    val commands = mutableListOf<Command>()

    for (line in input) {
        val splitted = line.split(" ")
        commands.add(
            when (splitted[0]) {
                "out" -> {
                    val register = splitted[1].toCharArray()[0]
                    object : Command {

                        override fun run(state: State) {
                            val current = state.registers[register]
                            require(state.previous != current)
                            state.previous = current
                        }
                    }
                }
                "inc" -> {
                    val register = splitted[1].toCharArray()[0]
                    object : Command {
                        override fun run(state: State) {
                            state.registers[register] =
                                state.registers.getOrDefault(register, 0) + 1
                        }
                    }
                }
                "dec" -> {
                    val register = splitted[1].toCharArray()[0]
                    object : Command {
                        override fun run(state: State) {
                            state.registers[register] =
                                state.registers.getOrDefault(register, 0) - 1
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
                                state.registers[dest] =
                                    state.registers.getOrDefault(
                                        v.toCharArray()[0],
                                        0
                                    )
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
                            if ((number == null && state.registers.getOrDefault(
                                    v,
                                    0
                                ) != 0L) ||
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

fun day25(commands: List<Command>, startValueA: Long): Long {
    val state = State(startValueA)

    while (state.idx < commands.size) {
        commands[state.idx].run(state)
        state.idx += 1
    }

    return state.registers.getOrDefault('a', 0)
}

fun main() {
    val commands = parse(File("data/day25/input.txt").readLines())
    (0..Long.MAX_VALUE).forEach { a ->
        println("trying register a = $a")
        runCatching { day25(commands, a) }
    }
}
