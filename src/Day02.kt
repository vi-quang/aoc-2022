import java.lang.IllegalStateException

/**
 * HandShapeNode -------------------------------------------------------------------
 */
private class HandShapeNode(val shape: HandShape) {
    var next: HandShapeNode = this
        set(value) {
            field = value
            field._prev = this
        }

    private var _prev = this

    val prev: HandShapeNode
        get() {
            return _prev
        }
}

private fun HandShapeNode.rotateForwardTo(shape: HandShape) : HandShapeNode {
    var currentNode = this

    while (currentNode.shape != shape) {
        currentNode = currentNode.next
    }

    return currentNode
}

private fun HandShapeNode.rotateBackwardTo(shape: HandShape) : HandShapeNode {
    var currentNode = this

    while (currentNode.shape != shape) {
        currentNode = currentNode.prev
    }

    return currentNode
}

private fun HandShapeNode.score() : Int {
    return this.shape.score
}

private fun HandShapeNode.getNodeForOutcome(outcome : Outcome) : HandShapeNode {
    return when (outcome) {
        Outcome.Lose -> {
            this.prev
        }

        Outcome.Draw -> {
            this
        }

        Outcome.Win -> {
            this.next
        }
    }
}


private fun HandShapeNode.outcome(opponentHand: HandShape) : Outcome {
    if (this.shape == opponentHand) {
        return Outcome.Draw
    } else if (this.next.shape == opponentHand) {
        return Outcome.Win
    } else {
        return Outcome.Lose
    }
}


/**
 * HandShape -------------------------------------------------------------------
 */
private enum class HandShape (val score: Int) {
    Rock(1),
    Paper(2),
    Scissors(3)
}



/**
 * Outcome -------------------------------------------------------------------
 */
private enum class Outcome(val score: Int) {
    Lose(0),
    Draw(3),
    Win(6)
}


private fun Char.toOutcome() : Outcome {
    return when (this) {
        'X' -> Outcome.Lose
        'Y' -> Outcome.Draw
        'Z' -> Outcome.Win
        else -> throw IllegalStateException()
    }
}


/**
 * Main -------------------------------------------------------------------
 */
fun main() {

    fun createRing(): HandShapeNode {
        val nodeRock = HandShapeNode(HandShape.Rock)
        val nodePaper = HandShapeNode(HandShape.Paper)
        val nodeScissor = HandShapeNode(HandShape.Scissors)

        nodeRock.next = nodePaper
        nodePaper.next = nodeScissor
        nodeScissor.next = nodeRock
        return nodeRock
    }

    fun createMap(rock: Char, paper: Char, scissors: Char) : Map<Char, HandShape> {
        return mapOf(rock to HandShape.Rock, paper to HandShape.Paper, scissors to HandShape.Scissors)
    }


    fun part1(input: List<String>): Int {
        val opponent = createMap('A', 'B', 'C')
        val me = createMap('X', 'Y', 'Z')

        var node = createRing()
        var total = 0

        for (token in input) {
            val (opponentHandCode, myHandCode) = token.replace(" ", "").toCharArray()
            val (opponentHand, myHand) = opponent[opponentHandCode]!! to me[myHandCode]!!

            node = node.rotateForwardTo(opponentHand)
            total += node.outcome(myHand).score

            node = node.rotateBackwardTo(myHand)
            total += node.score()
        }

        return total
    }

    fun part2(input: List<String>): Int {
        val opponent = createMap('A', 'B', 'C')

        var node = createRing()
        var total = 0

        for (token in input) {
            val (opponentHandCode, outcomeCode) = token.replace(" ", "").toCharArray()
            val opponentHand = opponent[opponentHandCode]!!

            node = node.rotateForwardTo(opponentHand)

            val outcome = outcomeCode.toOutcome()
            val myHand = node.getNodeForOutcome(outcome)

            total += myHand.score()
            total += outcome.score
        }

        return total
    }


    val input = readInput(2)
    println(part1(input))
    println(part2(input))
}
