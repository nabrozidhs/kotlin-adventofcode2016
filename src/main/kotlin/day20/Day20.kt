package day20

import java.io.File

fun combinesRanges(ranges: List<LongRange>): List<LongRange> {
    val combinedRanges = mutableListOf<LongRange>()
    var current = ranges.first()
    for (r in ranges) {
        current = if (current.contains(r.first)) {
            current.first..r.last
        } else {
            combinedRanges.add(current)
            r
        }
    }
    combinedRanges.add(current)
    return combinedRanges
}

fun parse(lines: List<String>): List<LongRange> =
    combinesRanges(
        lines.asSequence()
            .map { line ->
                val splitted = line.split("-")
                splitted[0].toLong()..splitted[1].toLong()
            }
            .sortedBy { it.first }
            .sortedBy { it.last }
            .toList()
    )

fun day20part1(ranges: List<LongRange>): Long =
    (sequenceOf(0L) + ranges.asSequence().map { it.last + 1 })
        .first { v -> ranges.none { it.contains(v) } }

const val maxValue = 4294967295
fun day20part2(ranges: List<LongRange>): Long {
    val starts = (sequenceOf(0L) + ranges.asSequence().map { it.last + 1 })
        .filter { v -> v <= maxValue && ranges.none { it.contains(v) } }
        .toList()
    val endings =
        (ranges.asSequence().map { it.first - 1 } + sequenceOf(maxValue))
            .filter { v -> v >= 0 && ranges.none { it.contains(v) } }
            .toList()
    return starts.zip(endings).map { it.second - it.first + 1 }.sum()
}

fun main() {
    val ranges = parse(File("data/day20/input.txt").readLines())
    println("part1 ${day20part1(ranges)}")
    println("part2 ${day20part2(ranges)}")
}
