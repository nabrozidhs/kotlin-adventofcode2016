package day17

import java.security.MessageDigest
import javax.xml.bind.DatatypeConverter
import kotlin.math.max

typealias Position = Pair<Int, Int>

fun Position.adjacentPositions(): List<Position> = listOf(
    Pair(first - 1, second),
    Pair(first + 1, second),
    Pair(first, second - 1),
    Pair(first, second + 1)
)

data class Path(val input: String, val path: String, val position: Position) {

    fun adjacentPaths(): List<Path> {
        val md = MessageDigest.getInstance("MD5")
        md.update((input + path).toByteArray())
        return DatatypeConverter.printHexBinary(md.digest()).toLowerCase()
            .take(4)
            .toCharArray()
            .zip(position.adjacentPositions())
            .withIndex()
            .mapNotNull { (i, v) ->
                val (c, position) = v
                position.takeIf {
                    c in 'b'..'f' &&
                            position.first in 0..3 &&
                            position.second in 0..3
                }?.let {
                    Path(
                        input,
                        path + when (i) {
                            0 -> 'U'
                            1 -> 'D'
                            2 -> 'L'
                            3 -> 'R'
                            else -> throw IllegalArgumentException()
                        },
                        it
                    )
                }
            }
    }
}

fun day17Part1(input: String): String {
    val endPosition = Pair(3, 3)

    val bfs = mutableListOf(Path(input, "", Pair(0, 0)))
    while (true) {
        val path = bfs.removeAt(0)

        if (path.position == endPosition) {
            return path.path
        }

        bfs.addAll(path.adjacentPaths())
    }
}

fun day17Part2(input: String): Int {
    val endPosition = Pair(3, 3)

    val bfs = mutableListOf(Path(input, "", Pair(0, 0)))
    var longest = 0
    while (bfs.isNotEmpty()) {
        val path = bfs.removeAt(0)

        if (path.position == endPosition) {
            longest = max(path.path.length, longest)
            continue
        }

        bfs.addAll(path.adjacentPaths())
    }
    return longest
}

fun main() {
    println("part1 ${day17Part1("pslxynzg")}")
    println("part2 ${day17Part2("pslxynzg")}")
}