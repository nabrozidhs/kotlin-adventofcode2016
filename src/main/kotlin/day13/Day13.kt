package day13

typealias Position = Pair<Int, Int>

fun Position.adjacentPosition(): List<Position> = listOf(
    copy(first = first - 1),
    copy(first = first + 1),
    copy(second = second - 1),
    copy(second = second + 1)
)

class Map(val input: Long) {

    private val cache = mutableMapOf<Position, Boolean>()

    fun isWall(x: Int, y: Int): Boolean =
        if (x < 0 || y < 0) {
            true
        } else {
            cache.getOrPut(Pair(x, y)) {
                val v =
                    (x * x + 3 * x + 2 * x * y + y + y * y + input).toString(2)
                v.count { it == '1' } % 2 == 1
            }
        }
}

fun day13part1(map: Map, startLocation: Position, endLocation: Position): Int {
    val visited = mutableSetOf<Position>()
    val bfs = mutableListOf(setOf(startLocation))

    var n = 0
    while (true) {
        val nextBatch = mutableSetOf<Position>()
        for (location in bfs.removeAt(0)) {
            if (endLocation == location) {
                return n
            }
            if (visited.contains(location)) {
                continue
            }
            if (map.isWall(location.first, location.second)) {
                continue
            }
            visited.add(location)
            nextBatch.addAll(location.adjacentPosition())
        }
        n += 1
        bfs.add(nextBatch)
    }
}

fun day13part2(map: Map, startLocation: Position, maxSteps: Int): Int {
    val visited = mutableSetOf<Position>()
    val bfs = mutableListOf(setOf(startLocation))

    for (n in 0..maxSteps) {
        val nextBatch = mutableSetOf<Position>()
        for (location in bfs.removeAt(0)) {
            if (visited.contains(location)) {
                continue
            }
            if (map.isWall(location.first, location.second)) {
                continue
            }
            visited.add(location)
            nextBatch.addAll(location.adjacentPosition())
        }
        bfs.add(nextBatch)
    }
    return visited.size
}

fun main() {
    val map = Map(1350)
    println("part1: ${day13part1(map, Pair(1, 1), Pair(31, 39))}")
    println("part2: ${day13part2(map, Pair(1, 1), 50)}")
}