package day23

import java.io.File

sealed class Command {
    abstract fun run(state: State)

    class Inc(val register: Char) : Command() {
        override fun run(state: State) {
            state.registers[register] =
                state.registers.getOrDefault(register, 0) + 1
        }
    }

    class Dec(val register: Char) : Command() {
        override fun run(state: State) {
            state.registers[register] =
                state.registers.getOrDefault(register, 0) - 1
        }
    }

    class Tgl(private val register: Char) : Command() {

        override fun run(state: State) {
            val idx =
                state.idx + state.registers.getOrDefault(register, 0).toInt()
            val command = state.program.getOrNull(idx) ?: return
            state.program[idx] = when (command) {
                is Inc -> Dec(command.register)
                is Dec -> Inc(command.register)
                is Tgl -> Inc(command.register)
                is Cpy -> Jnz(command.a, command.b)
                is Jnz -> Cpy(command.a, command.b)
            }
        }
    }

    class Jnz(val a: (State) -> Long, val b: String) : Command() {

        override fun run(state: State) {
            if (a(state) != 0L) {
                val v =
                    b.toLongOrNull() ?: state.registers[b.toCharArray()[0]]!!
                state.idx = state.idx + v.toInt() - 1
            }
        }
    }

    class Cpy(val a: (State) -> Long, val b: String) : Command() {

        override fun run(state: State) {
            if (b.toLongOrNull() != null) {
                return
            }
            state.registers[b.toCharArray()[0]] = a(state)
        }
    }
}

class State(val program: MutableList<Command>, startValueA: Long) {

    var idx = 0
    val registers = mutableMapOf<Char, Long>('a' to startValueA)
}

fun parse(input: List<String>): List<Command> {
    val commands = mutableListOf<Command>()

    for (line in input) {
        val splitted = line.split(" ")
        commands.add(
            when (splitted[0]) {
                "tgl" -> Command.Tgl(splitted[1].toCharArray()[0])
                "inc" -> Command.Inc(splitted[1].toCharArray()[0])
                "dec" -> Command.Dec(splitted[1].toCharArray()[0])
                "cpy" -> {
                    val a = splitted[1]
                    val b = splitted[2]
                    Command.Cpy(
                        {
                            a.toLongOrNull() ?: it.registers.getOrDefault(a.toCharArray()[0], 0L)
                        },
                        b
                    )
                }
                "jnz" -> {
                    val a = splitted[1]
                    val b = splitted[2]
                    Command.Jnz(
                        {
                            a.toLongOrNull() ?: it.registers.getOrDefault(a.toCharArray()[0], 0L)
                        },
                        b
                    )
                }
                else -> throw IllegalArgumentException()
            }
        )
    }

    return commands
}

fun day23(input: List<Command>, startValueA: Long): Long {
    val state = State(input.toMutableList(), startValueA)

    while (state.idx < state.program.size) {
        state.program[state.idx].run(state)
        state.idx += 1
    }

    return state.registers.getOrDefault('a', 0)
}

fun main() {
    val commands = parse(File("data/day23/input.txt").readLines())
//    val commands = parse(
//        "cpy 2 a\ntgl a\ntgl a\ntgl a\ncpy 1 a\ndec a\ndec a".lines()
//    )

    println("part1 ${day23(commands, 7)}")
    println("part2 ${day23(commands, 12)}")
}
