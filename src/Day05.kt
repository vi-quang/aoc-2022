/**
 * Main -------------------------------------------------------------------
 */
fun main() {

    class Move(val numberOfCratesToMove : Int, val indexOfCrateToMoveFrom : Int, val indexOfCrateToMoveTo: Int) {

    }

    fun createStackList(input: List<String>) : MutableList<ArrayDeque<Char>> {
        val returnList = mutableListOf<ArrayDeque<Char>>()
        for (line in input) {
            if (line.isEmpty()) {
                break
            }
            for ((index, c) in line.withIndex()) {
                val modulo = (index-1).mod(4)
                if (modulo == 0) {
                    val crateIndex = index/4

                    if (returnList.size < (crateIndex + 1)) {
                        returnList.add(ArrayDeque())
                    }

                    if (c.isLetter()) {
                        returnList[crateIndex].addLast(c)
                    }
                }
            }
        }

        return returnList
    }


    fun createMoveList(input: List<String>) : MutableList<Move> {
        val returnList = mutableListOf<Move>()
        var mode = 0
        for (line in input) {
            if (line.isEmpty()) {
                mode = 1
                continue
            }

            if (mode == 0) {
                continue
            }

            var moveLine =  line.replace("move ", "")
            moveLine = moveLine.replace("from ", "")
            moveLine = moveLine.replace("to ", "")

            val (crateNumber, fromIndex, toIndex) = moveLine.split(" ").map { it.toInt() }
            returnList.add(Move(crateNumber, fromIndex, toIndex))
        }

        return returnList
    }

    fun part1(input: List<String>): String {
        val stacks = createStackList(input)
        val moves = createMoveList(input)

        for (move in moves) {
            for (i in 1..move.numberOfCratesToMove) {
                val crate = stacks[move.indexOfCrateToMoveFrom - 1].removeFirst()
                stacks[move.indexOfCrateToMoveTo - 1].addFirst(crate)
            }
        }

        var result = ""
        for (stack in stacks) {
            result += stack.first()
        }

        return result
    }

    fun part2(input: List<String>): String {
        val stacks = createStackList(input)
        val moves = createMoveList(input)

        for (move in moves) {
            val moveQueue = ArrayDeque<Char>()
            for (i in 1..move.numberOfCratesToMove) {
                val crate = stacks[move.indexOfCrateToMoveFrom - 1].removeFirst()
                moveQueue.addFirst(crate)
            }

            for (c in moveQueue) {
                stacks[move.indexOfCrateToMoveTo - 1].addFirst(c)
            }
        }

        var result = ""
        for (stack in stacks) {
            result += stack.first()
        }

        return result

    }


    val input = readInput(5)
    println(part1(input))
    println(part2(input))
}
