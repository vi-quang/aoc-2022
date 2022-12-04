
fun main() {
    class Elf(range: String) {
        val sectionIdList = mutableSetOf<Int>()
        init {
            add(range)
        }

        fun add(range: String) {
            val token = range.split("-")
            val (rangeMin, rangeMax) = token[0].toInt() to token[1].toInt()
            for (i in rangeMin .. rangeMax) {
                sectionIdList.add(i)
            }
        }
    }

    fun Elf.fullyContainsOrIsFullyContained(other : Elf) : Boolean {
        val intersection = this.sectionIdList.intersect(other.sectionIdList)
        return (intersection.size == other.sectionIdList.size || intersection.size == this.sectionIdList.size)
    }

    fun Elf.hasIntersection(other : Elf) : Boolean {
        val intersection = this.sectionIdList.intersect(other.sectionIdList)
        return (intersection.isNotEmpty())
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
