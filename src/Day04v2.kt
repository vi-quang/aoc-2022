
fun main() {
    class Elf(range: String) {
        init {
            val token = range.split("-")
            val (rangeMin, rangeMax) = token[0].toInt() to token[1].toInt()
            this.range = rangeMin .. rangeMax
        }

        val range : ClosedRange<Int>
    }

    fun ClosedRange<Int>.length() : Int {
        return (endInclusive - start)
    }
    fun Elf.fullyContainsOrIsFullyContained(other : Elf) : Boolean {
        val otherIsLarger = other.range.length() > range.length()
        val (smallRange, largeRange) = if (otherIsLarger) range to other.range else other.range to range

        return largeRange.contains(smallRange.start) && largeRange.contains(smallRange.endInclusive)
    }

    fun Elf.hasIntersection(other : Elf) : Boolean {
        val otherIsLarger = other.range.length() > range.length()
        val (smallRange, largeRange) = if (otherIsLarger) range to other.range else other.range to range

        return largeRange.contains(smallRange.start) || largeRange.contains(smallRange.endInclusive)
    }

    fun createElfPairList(input: List<String>) : MutableList<Pair<Elf, Elf>> {
        val elfPairList = mutableListOf<Pair<Elf, Elf>>()

        for (line in input) {
            val token = line.split(",")
            val pair = (Elf(token[0]) to Elf(token[1]))
            elfPairList.add(pair)
        }

        return elfPairList
    }


    fun part1(input: List<String>): Int {
        val elfPairList = createElfPairList(input)

        val resultList = mutableListOf<Pair<Elf, Elf>>()
        for (pair in elfPairList) {
            if (pair.first.fullyContainsOrIsFullyContained(pair.second)) {
                resultList.add(pair)
            }
        }

        return resultList.size
    }

    fun part2(input: List<String>): Int {
        val elfPairList = createElfPairList(input)

        val resultList = mutableListOf<Pair<Elf, Elf>>()
        for (pair in elfPairList) {
            if (pair.first.hasIntersection(pair.second)) {
                resultList.add(pair)
            }
        }

        return resultList.size
    }

    val input = readInput(4)
    println(part1(input))
    println(part2(input))
}
