package day04

import java.io.File

data class Room(private val encryptedName: String,
                val sectorId: Int,
                val checksum: String) {
    fun calculateChecksum(): String =
            encryptedName.replace("-", "")
                    .toCharArray()
                    .groupBy({ it })
                    .entries
                    .sortedBy { it.key }
                    .sortedByDescending { it.value.size }
                    .map { it.key }
                    .take(5)
                    .joinToString(separator = "")

    fun name(): String =
            encryptedName.replace("-", " ")
                    .map {
                        if (it == ' ') {
                            it
                        } else {
                            var c = it
                            for (i in 0..sectorId - 1) {
                                c = shiftRight(c)
                            }
                            c
                        }
                    }
                    .joinToString(separator = "")
}

fun shiftRight(c: Char): Char =
        ((c.toInt() - 'a'.toInt() + 1) % 26 + 'a'.toInt()).toChar()

val roomRegex = Regex("([\\w-]+)-(\\d+)\\[(\\w+)]")
fun parse(input: String): List<Room> =
        input.split("\n")
                .map {
                    val (_, name, id, checksum) = roomRegex.find(it)!!.groupValues
                    Room(name, id.toInt(), checksum)
                }

fun main(args: Array<String>) {
    val rooms = parse(File("data/day04/input.txt").readText())

    println("Valid rooms sum of sectorId: ${rooms.filter({ it.calculateChecksum() == it.checksum }).sumBy { it.sectorId }}")
    println("North pole sectorId: ${rooms.filter({ it.calculateChecksum() == it.checksum }).find { it.name() == "northpole object storage" }!!.sectorId}")
}
