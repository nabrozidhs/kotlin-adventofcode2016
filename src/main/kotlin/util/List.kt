package util

fun <T> List<T>.permutations(): List<List<T>> {
    fun inner(input: List<T>): List<List<T>> {
        if (input.isEmpty()) {
            return listOf(emptyList())
        }
        return input.withIndex().map { (i, v) ->
            inner(input.subList(0, i) + input.subList(i + 1, input.size))
                .map { listOf(v) + it}
        }.flatten()
    }
    if (isEmpty()) {
        return emptyList()
    }
    return inner(this)
}
