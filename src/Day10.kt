import kotlin.math.abs
import kotlin.streams.asSequence

/**
 * It's one line solution day for a change of pace.
 */
fun main() {

    fun getSequence(input: List<String>): Map<Int, List<Pair<Int, Int>>> {
        return input.stream().map { it.replace("addx ", "noop noop,noop") }
            .asSequence()
            .flatMap(Regex(" ")::splitToSequence)
            .mapIndexed { index, line ->
                val cycle = index + 1
                line.replace(",noop", " $cycle|")
                    .replace("noop", "$cycle|0")
            }
            .flatMap(Regex(" ")::splitToSequence)
            .map {
                val token = it.split("|")
                val cycle = token[0].toInt()
                cycle to token[1].toInt()
            }
            .scan(Pair(0, 1)) { acc, pair ->
                pair.first to (acc.second + pair.second)
            }
            .groupBy { it.first }
    }

    fun part1(input: List<String>): Int {
        return getSequence(input)
            .filter { it.key % 20 == 0 && it.key % 40 != 0 && it.key <= 220 }
            .map { it.value.first() }
            .sumOf { it.first * it.second }
    }

    fun part2(input: List<String>): List<String> {

        return getSequence(input)
            .map {
                val entry = it.value.last()
                val pixel = if (abs(entry.first % 40 - entry.second) < 2) '#' else '.'
                pixel
            }.windowed(40, 40)
            .map { it.joinToString("") }
            .toList()

    }

    val input = readInput(10)

    println(part1(input))
    part2(input).forEach {
        println(it)
    }
}
