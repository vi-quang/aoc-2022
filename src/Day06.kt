/**
 * Main -------------------------------------------------------------------
 */
fun main() {

    class Bin(val name: String) {
        var content: Char = '-'
    }

    class CharacterWindow(var capacity: Int) {

        val binList = mutableListOf<Bin>()
        init {
            for (i in 1..capacity) {
                binList.add(Bin("" + i))
            }
        }

        val map = mutableMapOf<Char, MutableList<Bin>>()
        private fun getListForChar(c : Char) : MutableList<Bin> {
            if (map[c] == null) {
                map[c] = mutableListOf()
            }

            return map[c]!!
        }

        var index = -1
        var hasInitialized = false

        var count = 0

        fun push(c : Char) {
            index++

            if (index >= capacity) {
                hasInitialized = true
                index = 0
            }

            if (!hasInitialized) {
                addChar(c)
            } else {
                emptyCurrentBin()
                addChar(c)
            }
        }

        private fun addChar(c: Char) {
            val bin = binList[index]
            bin.content = c

            val list = getListForChar(c)
            list.add(bin)
            val size = list.size - 1
            count += (Math.pow(2.0, size.toDouble())).toInt()
        }

        private fun emptyCurrentBin() {
            val bin = binList[index]
            val list = getListForChar(bin.content)
            val size = list.size - 1
            count -= (Math.pow(2.0, size.toDouble())).toInt()
            list.remove(bin)
        }


        fun isAllDistinct() : Boolean {
            return count == capacity
        }
    }

    fun part1(input: List<String>): Int {
        val line = input[0]

        val capacity = 4

        val window = CharacterWindow(capacity)
        for ((index, c) in line.withIndex()) {

            window.push(c)

            if (window.isAllDistinct()) {
                return index + 1
            }
        }

        return 0
    }

    fun part2(input: List<String>): Int {
        val line = input[0]

        val capacity = 14

        val window = CharacterWindow(capacity)
        for ((index, c) in line.withIndex()) {

            window.push(c)

            if (window.isAllDistinct()) {
                return index + 1
            }
        }

        return 0    }


    val input = readInput(6)
    println(part1(input))
    println(part2(input))
}
