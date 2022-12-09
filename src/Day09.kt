import kotlin.math.abs

/**
 * Main -------------------------------------------------------------------
 */
fun main() {


    fun Int.toIdentity() : Int {
        if (this < 1) {
            return -1
        } else {
            return 1
        }
    }

    fun part1(input: List<String>): Int {
        val tailPositionList = mutableSetOf<Pair<Int, Int>>()
        var headC = 0
        var headR = 0
        var tailC = 0
        var tailR = 0


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

                val diffC = headC - tailC
                val diffR = headR - tailR
                val deltaC = abs(diffC)
                val deltaR = abs(diffR)


                if (deltaC == 2 || deltaR == 2) {
                    if (headR == tailR) { //same row
                        tailC += diffC.toIdentity()
                    } else if (headC == tailC) {
                        tailR += diffR.toIdentity()
                    } else {
                        if (deltaC == 2) {
                            tailC += diffC.toIdentity()
                            tailR = headR
                        }

                        if (deltaR == 2) {
                            tailR += diffR.toIdentity()
                            tailC = headC
                        }
                    }
                }


                System.err.println("H:$headR, $headC --- T:$tailR, $tailC")

                tailPositionList.add(Pair(tailR, tailC))

            }

        }


        return tailPositionList.size
    }

    fun part2(input: List<String>): Int {
        var total = 0

        return total
    }


    val input = readInput(9)
    println(part1(input))
    println(part2(input))
}
