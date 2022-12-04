import java.util.PriorityQueue

fun main() {
    data class Elf(val calorieList: MutableList<Int> = mutableListOf()) {
        fun totalCalories() : Int {
            return calorieList.sumOf { it }
        }
    }

    fun createElfList(input : List<String>) : MutableList<Elf> {
        val elves = mutableListOf<Elf>()
        var currentElf = Elf()

        for (line in input) {
            if (line.isEmpty()) {
                elves.add(currentElf)
                currentElf = Elf()
            } else {
                currentElf.calorieList.add(line.toInt())
            }
        }
        return elves
    }

    fun findElvesWithTheMostCalories(elves: List<Elf>, numberOfElves:Int) : PriorityQueue<Elf> {
        val priorityQueue = PriorityQueue<Elf> {
                e1, e2 -> e1.totalCalories() - e2.totalCalories()
        }

        for (elf in elves) {
            priorityQueue.add(elf)
            if (priorityQueue.size > numberOfElves) {
                priorityQueue.poll()
            }
        }

        return priorityQueue
    }

    fun part1(input: List<String>): Int {
        val elves = createElfList(input)
        val priorityQueue = findElvesWithTheMostCalories(elves, 1)
        return priorityQueue.sumOf { it.totalCalories() }
    }

    fun part2(input: List<String>): Int {
        val elves = createElfList(input)
        val priorityQueue = findElvesWithTheMostCalories(elves, 3)
        return priorityQueue.sumOf { it.totalCalories() }
    }


    val input = readInput(1)
    println(part1(input))
    println(part2(input))
}
