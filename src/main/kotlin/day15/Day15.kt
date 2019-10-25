package day15

import java.io.File

data class Disc(
    val dt: Int,
    val positions: Int,
    val startPosition: Int
) {

    fun bounces(t: Int): Boolean {
        val currentPosition = startPosition + t + dt
        return currentPosition % positions != 0
    }
}

fun parse(input: List<String>): List<Disc> {
    return input.withIndex().map {
        val split = it.value.split(" ")
        Disc(
            it.index + 1,
            split[3].toInt(),
            split[11].substringBefore('.').toInt()
        )
    }
}

fun day15(discs: List<Disc>): Int =
    (0..Int.MAX_VALUE).first { t -> discs.none { it.bounces(t) } }

fun main() {
    val discs = parse(File("data/day15/input.txt").readLines()).toMutableList()
    println("part1: ${day15(discs)}")
    discs.add(Disc(discs.last().dt + 1, 11, 0))
    println("part2: ${day15(discs)}")
}
