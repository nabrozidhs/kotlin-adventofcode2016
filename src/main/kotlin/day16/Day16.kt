package day16

fun lastState(input: String, discLength: Int): String {
    var value = input
    while (value.length < discLength) {
        val reversed: String = value.reversed()
            .map { if (it == '0') '1' else '0' }
            .joinToString(separator = "")
        value = "${value}0$reversed"
    }
    return value.take(discLength)
}

fun checksum(input: String): String {
    var output = input
    while (output.length % 2 == 0) {
        output = output.chunked(2)
            .map { if (it[0] == it[1]) '1' else '0' }
            .joinToString(separator = "")
    }
    return output
}

fun day16(input: String, discLength: Int): String {
    val v = lastState(input, discLength)
    return checksum(v)
}

fun main() {
    println("part1 ${day16("11101000110010100", 272)}")
    println("part2 ${day16("11101000110010100", 35651584)}")
}