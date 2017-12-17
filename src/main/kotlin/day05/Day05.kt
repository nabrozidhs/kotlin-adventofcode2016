package day05

import java.security.MessageDigest
import javax.xml.bind.DatatypeConverter

val md5Digest: MessageDigest = MessageDigest.getInstance("MD5")
fun md5(data: String): String =
        DatatypeConverter.printHexBinary(md5Digest.digest(data.toByteArray()))

fun findPassword(doorId: String): String =
        generateSequence(0, { it + 1 })
                .map { md5(doorId + it) }
                .filter { it.startsWith("00000") }
                .map { it[5] }
                .take(8)
                .joinToString(separator = "")

val password2Regex = Regex("^00000[0-7]")
fun findPassword2(doorId: String): String {
    val password = CharArray(8, { ' ' })
    for (i in 0..Int.MAX_VALUE) {
        val md5hash = md5(doorId + i)
        if (password2Regex.find(md5hash) != null && password[md5hash[5].toString().toInt()] == ' ') {
            password[md5hash[5].toString().toInt()] = md5hash[6]
        }

        if (!password.contains(' ')) {
            return password.joinToString(separator = "")
        }
    }
    throw IllegalArgumentException()
}

fun main(args: Array<String>) {
    println("password 1: ${findPassword("cxdnnyjw")}")
    println("password 2: ${findPassword2("cxdnnyjw")}")
}
