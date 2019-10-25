package day18

typealias Position = Pair<Int, Int>

private val trapConditions = setOf(
    listOf(true, true, false),
    listOf(false, true, true),
    listOf(true, false, false),
    listOf(false, false, true)
)

class Map(input: List<Boolean>) {

    val width = input.size

    private val cache = mutableMapOf<Position, Boolean>()

    init {
        input.withIndex().forEach { cache[Position(it.index, 0)] = it.value }
    }

    fun isTrap(position: Position): Boolean = cache.getOrPut(position) {
        val leftIsTrap = position.takeIf { it.first > 0 }
            ?.let { isTrap(Position(it.first - 1, it.second - 1)) }
            ?: false

        val centerIsTrap = isTrap(position.copy(second = position.second - 1))

        val rightIsTrap = position.takeIf { it.first < width - 1 }
            ?.let { isTrap(Position(it.first + 1, it.second - 1)) }
            ?: false

        cache.keys.filter { it.second <= position.second - 2 }
            .forEach { cache.remove(it) }
        trapConditions.contains(listOf(leftIsTrap, centerIsTrap, rightIsTrap))
    }
}

fun day18(input: String, rows: Int): Int {
    val map = Map(input.map { it == '^' })

    return (0 until rows).map { y ->
        (0 until map.width).map { x -> map.isTrap(Position(x, y)) }
            .count { !it }
    }.sum()
}

fun main() {
    println(
        "part1 ${day18(
            ".^..^....^....^^.^^.^.^^.^.....^.^..^...^^^^^^.^^^^.^.^^^^^^^.^^^^^..^.^^^.^^..^.^^.^....^.^...^^.^.",
            40
        )}"
    )
    println(
        "part2 ${day18(
            ".^..^....^....^^.^^.^.^^.^.....^.^..^...^^^^^^.^^^^.^.^^^^^^^.^^^^^..^.^^^.^^..^.^^.^....^.^...^^.^.",
            400000
        )}"
    )
}