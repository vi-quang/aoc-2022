
/**
 * Main -------------------------------------------------------------------
 */
fun main() {

    /**
     * Compartment -------------------------------------------------------------------
     */
    data class Compartment(val itemList: MutableList<Char> = mutableListOf()) {
        fun contains(c: Char) : Boolean {
            return itemList.contains(c)
        }
    }

    fun Compartment.intersect(other: Compartment) : Set<Char> {
        return other.itemList.intersect(this.itemList.toSet())
    }

    /**
     * Rucksack -------------------------------------------------------------------
     */
    data class Rucksack(val compartments: MutableList<Compartment> = mutableListOf()) {
        init {
            compartments.add(Compartment())
            compartments.add(Compartment())
        }

        fun contains(c : Char) : Boolean {
            for (compartment in compartments) {
                if (compartment.contains(c)) {
                    return true
                }
            }

            return false
        }
    }


    fun Rucksack.intersect(other: Rucksack) : Set<Char> {
        val returnList = mutableSetOf<Char>()

        for (compartment in other.compartments) {
            for (item in compartment.itemList) {
                if (this.contains(item)) {
                    returnList.add(item)
                }
            }
        }

        return returnList.toSet()
    }


    fun Rucksack.intersect(other: Set<Char>) : Set<Char> {
        val returnList = mutableSetOf<Char>()

        for (c in other) {
            if (this.contains(c)) {
                returnList.add(c)
            }
        }

        return returnList.toSet()
    }



    /**
     * Misc -------------------------------------------------------------------
     */
    fun String.toDeque() : ArrayDeque<Char> {
        val deque = ArrayDeque<Char>()
        val array = this.toCharArray()
        for (c in array) {
            deque.addLast(c)
        }
        return deque
    }

    fun Char.priority() : Int {
        val priority = ('a' .. 'z') + ('A' .. 'Z')
        return priority.indexOf(this) + 1
    }


    fun createRucksackList(input: List<String>) : MutableList<Rucksack>  {
        val rucksacks = mutableListOf<Rucksack>()

        for (line in input) {
            val rucksack = Rucksack()
            val deque = line.toDeque()
            while (deque.size > 0) {
                rucksack.compartments[0].itemList.add(deque.removeFirst())
                rucksack.compartments[1].itemList.add(deque.removeLast())
            }

            rucksacks.add(rucksack)
        }

        return rucksacks
    }

    fun part1(input: List<String>): Int {
        var total = 0

        val rucksacks = createRucksackList(input)
        for (rucksack in rucksacks) {

            val intersections = rucksack.compartments[0].intersect(rucksack.compartments[1])
            total += intersections.sumOf { it.priority() }
        }

        return total
    }

    fun part2(input: List<String>): Int {

        var total = 0

        val rucksacks = createRucksackList(input)
        for (index in 1 .. rucksacks.size step 3) {

            val rucksack1 = rucksacks[index - 1]
            val rucksack2 = rucksacks[index]
            val rucksack3 = rucksacks[index + 1]

            var intersections = rucksack1.intersect(rucksack2)
            intersections = rucksack3.intersect(intersections)

            total += intersections.sumOf { it.priority() }
        }

        return total
    }


    val input = readInput(3)
    println(part1(input))
    println(part2(input))
}
