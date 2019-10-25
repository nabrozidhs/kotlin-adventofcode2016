package day19

data class Elf(val index: Int)

fun day19(input: List<Elf>, stealFrom: (Int, List<Elf>) -> Int): Int {
    val elves = input.toMutableList()
    var i = 0
    while (elves.size > 1) {
        val before = elves[i]
        elves.removeAt(stealFrom(i, elves))
        if (i < elves.size && before == elves[i]) {
            i = if (i + 1 >= elves.size) 0 else i + 1
        } else {
            i %= elves.size
        }
    }
    return elves.first().index
}

// This needs a circular buffer but whatevs
fun main() {
    val input = (1..3005290).map { Elf(it) }
    println("part1 ${day19(input) { i, elves -> (i + 1) % elves.size }}")
    println("part2 ${day19(input) { i, elves -> (i + elves.size / 2) % elves.size }}")
}