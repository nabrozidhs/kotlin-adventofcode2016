package day14

import java.security.MessageDigest
import javax.xml.bind.DatatypeConverter

class Hashes(private val salt: String, private val times: Int = 1) {

    private val hashes = mutableMapOf<String, String>()

    fun getHash(n: Int): String {
        val key = "$salt$n"
        return hashes.getOrPut(key) {
            var message = key
            repeat(times) {
                val md = MessageDigest.getInstance("MD5")
                md.update(message.toByteArray())
                message = DatatypeConverter.printHexBinary(md.digest())
                    .toLowerCase()
            }
            message
        }
    }
}

fun findRepeated(input: String): Char? {
    for (i in 0..input.length - 3) {
        val c = input[i]
        if ((i + 1..i + 2).all { c == input[it] }) {
            return c
        }
    }
    return null
}

fun day14(salt: String, times: Int): Int {
    val hashes = Hashes(salt, times)
    var found = 0
    for (n in 0..Int.MAX_VALUE) {
        val value = hashes.getHash(n)
        val c = findRepeated(value)
        if (c != null) {
            val toFind = "$c$c$c$c$c"
            if ((n + 1..n + 1000).asSequence()
                    .firstOrNull { hashes.getHash(it).contains(toFind) } != null
            ) {
                found += 1
                if (found >= 64) {
                    return n
                }
            }
        }
    }
    return -1
}

fun main() {
    println("part1 ${day14("ngcjuoqr", 1)}")
    println("part2 ${day14("ngcjuoqr", 2017)}")
}