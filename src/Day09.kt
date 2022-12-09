import kotlin.math.abs

/**
 * Main -------------------------------------------------------------------
 */
fun main() {

    fun Int.toIdentity(): Int {
        if (this == 0) {
            return 0
        }

        if (this < 1) {
            return -1
        } else {
            return 1
        }
    }

    fun getHeadPositions(input: List<String>): List<Pair<Int, Int>> {
        val positionList = mutableListOf<Pair<Int, Int>>()
        var headR = 0
        var headC = 0

        input.forEach {
            val token = it.split(" ")
            var step = token[1].toInt()

            System.err.println(">>$it")
            while (step > 0) {
                //System.err.println("STEPS: $step")
                step-- //we take a step

                when (token[0]) {
                    "R" -> {
                        headC++
                    }

                    "L" -> {
                        headC--
                    }

                    "U" -> {
                        headR++
                    }

                    "D" -> {
                        headR--
                    }
                }
                positionList.add(Pair(headR, headC))
            }
        }

        return positionList
    }

    fun getTailPositions(headPositions: List<Pair<Int, Int>>): List<Pair<Int, Int>> {
        val tailPositions = mutableListOf<Pair<Int, Int>>()

        var tailR = 0
        var tailC = 0

        headPositions.forEach {

            val headR = it.first
            val headC = it.second

            val diffC = headC - tailC
            val diffR = headR - tailR
            val deltaC = abs(diffC)
            val deltaR = abs(diffR)

            if (deltaC == 2 || deltaR == 2) {
                tailC += diffC.toIdentity()
                tailR += diffR.toIdentity()
            }

            //System.err.println("H:$headR, $headC --- T:$tailR, $tailC")

            tailPositions.add(Pair(tailR, tailC))

        }

        return tailPositions
    }

    fun part1(input: List<String>): Int {
        val headPositions = getHeadPositions(input)
        val tailPositions = getTailPositions(headPositions)

        return tailPositions.toSet().size
    }

    fun part2(input: List<String>): Int {
        val headPositions = getHeadPositions(input)
        var tailPositions = getTailPositions(headPositions) //tail 1

        for (i in 2 .. 9) { //tails 2 .. 9
            System.err.println("$i")
            tailPositions = getTailPositions(tailPositions)
        }

        return tailPositions.toSet().size
    }


    val input = readInput(9)
    println(part1(input))
    println(part2(input))
}
