package day10

import java.io.File

class Bot(private val id: Int) {

    private var input1: Wire? = null
    private var input2: Wire? = null

    fun addInput(input: Wire) {
        when {
            input1 == null -> input1 = input
            input2 == null -> input2 = input
            else -> throw IllegalArgumentException()
        }
        input.action = { action() }
    }

    private var outputLow: Wire? = null
    private var outputHigh: Wire? = null

    fun addOutputLow(output: Wire) {
        outputLow = output
        action()
    }

    fun addOutputHigh(output: Wire) {
        outputHigh = output
        action()
    }

    private fun action() {
        val a1 = input1?.value
        val a2 = input2?.value

        if (a1 != null && a2 != null) {
            println("Bot $id comparing $a1 $a2")
            outputHigh?.value = Math.max(a1, a2)
            outputLow?.value = Math.min(a1, a2)
        }
    }
}

class Wire(initialValue: Int? = null) {

    var action: () -> Unit = {}
        set(v) {
            field = v
            v()
        }

    var value: Int? = initialValue
        set(v) {
            field = v
            action()
        }
}

fun day10(input: List<String>): Int {
    val bots = mutableMapOf<Int, Bot>()
    for (line in input) {
        val split = line.split(" ")
        when {
            split[0] == "value" -> {
                val botId = split[5].toInt()

                bots.getOrPut(botId, { Bot(botId) }).addInput(Wire(initialValue = split[1].toInt()))
            }
            split[0] == "bot" -> {
                val sourceBotId = split[1].toInt()

                val sourceBot = bots.getOrPut(sourceBotId, { Bot(sourceBotId) })
                if (split[5] == "output") {
                    val wire = Wire()
                    sourceBot.addOutputLow(wire)
                    wire.action = { println("output ${split[6]} is ${wire.value}") }
                } else {
                    val destBotId = split[6].toInt()
                    val wire = Wire()
                    sourceBot.addOutputLow(wire)
                    bots.getOrPut(destBotId, { Bot(destBotId) }).addInput(wire)
                }

                if (split[10] == "output") {
                    val wire = Wire()
                    sourceBot.addOutputHigh(wire)
                    wire.action = { println("output ${split[11]} is ${wire.value}") }
                } else {
                    val destBotId = split[11].toInt()
                    val wire = Wire()
                    sourceBot.addOutputHigh(wire)
                    bots.getOrPut(destBotId, { Bot(destBotId) }).addInput(wire)
                }
            }
            else -> throw IllegalArgumentException(line)
        }
    }
    return 0
}

fun main(args: Array<String>) {
    day10(File("data/day10/input.txt").readLines())
}