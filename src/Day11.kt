import java.lang.IllegalStateException
import kotlin.properties.Delegates

/**
 * Part1 is broken due to modulo division.
 */
fun main() {
    data class Monkey(
        val id: String, val operation: String,
        val testValue: Int, val throwToMonkeyIdWhenTrue: String, val throwToMonkeyIdWhenFalse: String,
        val worryLevelInitialString: String,
        val worryLevelItemList: MutableList<ModuloNumber> = mutableListOf(),
    ) {

        val observers = mutableListOf<(Monkey, String, ModuloNumber) -> Unit>() //source, toMonkeyName, itemValue

        var thrownItem: Pair<String, ModuloNumber> by Delegates.observable(
            Pair(
                "",
                ModuloNumber(0)
            )
        ) { _, _, newValue ->
            observers.forEach { it(this, newValue.first, newValue.second) }
        }

        fun performInspection() {
            worryLevelItemList.forEach {
                performInspection(it)
            }

            worryLevelItemList.clear()
        }

        val inspectedWorryLevelList = mutableListOf<ModuloNumber>()
        private fun performInspection(worryLevel: ModuloNumber) {
            inspectedWorryLevelList.add(worryLevel)

            val token = operation.split(" ")

            val left = if (token[0] == "old") worryLevel else ModuloNumber(token[0].toInt(), true)
            val operator = token[1]
            val right = if (token[2] == "old") worryLevel else ModuloNumber(token[2].toInt(), true)

            var result = when (operator) {
                "*" -> {
                    left * right
                }

                "+" -> {
                    left + right
                }

                else -> {
                    throw IllegalStateException()
                }
            }

            result /= 1  //DIV 3 DIV 3

            var throwToMonkeyNameId = throwToMonkeyIdWhenFalse
            val mod = result.getValue(testValue)

            if (mod == 0) {
                throwToMonkeyNameId = throwToMonkeyIdWhenTrue
            }

            thrownItem = Pair(throwToMonkeyNameId, result)

//            System.out.println("Monkey $id ----------------------------------")
//            System.out.println("     Inspecting $worryLevel")
//            System.out.println("     Worry $result")
//            System.out.println("     Transfer $throwToMonkeyNameId")
        }

    }

    class MonkeyManager {

        var monkeyIdToMonkeyMap = mutableMapOf<String, Monkey>()

        fun add(monkey: Monkey) {
            monkeyIdToMonkeyMap.putIfAbsent(monkey.id, monkey)
            monkey.observers.add { _, monkeyId, item ->
                monkeyIdToMonkeyMap[monkeyId]!!.worryLevelItemList.add(item)
            }
        }


        fun start(numberOfRounds: Int) {
            (1..numberOfRounds).forEach { round ->
                monkeyIdToMonkeyMap.keys.forEach {
                    monkeyId ->
                    startInspectionForMonkey(monkeyId)
                }

                //printCurrent(round)
            }

//            val level = getMonkeyBusinessLevel()
//            if (level != 17926061332) {
//                throw IllegalStateException()
//            }
        }

        fun getMonkeyBusinessLevel(): Long {
            return monkeyIdToMonkeyMap.keys.map { monkeyId ->
                val monkey = monkeyIdToMonkeyMap[monkeyId]
                monkey!!.inspectedWorryLevelList.size.toLong()
            }.sortedDescending().take(2).scan(1L) { acc, numberOfInspections ->
                acc * numberOfInspections
            }.toList().last()
        }


        fun startInspectionForMonkey(monkeyName: String) {
            monkeyIdToMonkeyMap[monkeyName]!!.performInspection()
        }

        fun printCurrent(round: Int) {
            System.out.println("ROUND: $round==============================")
            monkeyIdToMonkeyMap.keys.forEach {
                val key = it
                val monkey = monkeyIdToMonkeyMap[key]
                System.err.println("$round: $monkey")
            }
        }


    }


    fun parseForMonkeyList(inputx: List<String>): List<Monkey> {
        val input = inputx.toMutableList()
        input.add("")
        val monkeyList = mutableListOf<Monkey>()
        input.asSequence().windowed(7, 7).map {
            System.err.println(">> $it")

            val (name) = "Monkey (\\d+):".toRegex().find(it[0])!!.destructured
            val (items) = "Starting items: (.+)$".toRegex().find(it[1])!!.destructured
            val (operation) = "Operation: new = (.+)$".toRegex().find(it[2])!!.destructured
            val (test) = "Test: divisible by (\\d+)$".toRegex().find(it[3])!!.destructured
            val (resultTrue) = "If true: throw to monkey (\\d+)$".toRegex().find(it[4])!!.destructured
            val (resultFalse) = "If false: throw to monkey (\\d+)$".toRegex().find(it[5])!!.destructured

            val monkey = Monkey(name, operation, test.toInt(), resultTrue, resultFalse, items)
            ModuloNumber.initialize(test.toInt())
            monkeyList.add(monkey)
        }.toList()

        monkeyList.forEach { monkey ->
            monkey.worryLevelItemList.addAll(monkey.worryLevelInitialString.split(", ").map { it.toModuloNumber() }
                .toList())
        }
        return monkeyList
    }

    fun part1(input: List<String>): Long {
        val monkeyList = parseForMonkeyList(input)
        val monkeyManager = MonkeyManager()
        monkeyList.forEach { monkey ->
            monkeyManager.add(monkey)
        }

        monkeyManager.start(20)

        return monkeyManager.getMonkeyBusinessLevel()
    }

    fun part2(input: List<String>): Long {
        val monkeyList = parseForMonkeyList(input)
        val monkeyManager = MonkeyManager()
        monkeyList.forEach { monkey ->
            monkeyManager.add(monkey)
        }

        monkeyManager.start(10000)

        return monkeyManager.getMonkeyBusinessLevel()
    }


    val input = readInput(11)
    println(part1(input))
    println(part2(input))
}
