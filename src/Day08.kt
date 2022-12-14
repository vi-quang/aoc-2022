import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.properties.Delegates


/**
 * How this code solves the puzzle:
 * Any Tree send out Probes in four directions: North, South, East, West. The Tree listens to the probes it sends out
 * for a completion event. These Probes have a height value that matches the value of the Tree that they originate from.
 *
 * Probes send out a completion event when it hits a tree with a height >= its height (Fail), or can't go further in
 * its set direction because it's at the end of the map (Success).
 *
 * TreeTracker tracks all the trees in the map (Grid) and listen for the Tree's completion event. A Tree sends out a
 * completion event, when it's heard from all the Probes it sent out. It sends a Success if any of the Probes has a
 * Success.
 *
 * The TreeTracker stops when it's heard from all the Trees.
 *
 * For Part 2: As each Probe pass through each Tree it tracks the Trees it has made contact with.
 */
enum class Direction {
    North, South, East, West
}


open class Completable() {
    enum class CompletionState {
        Success, Fail, Uncompleted
    }

    fun onCompletion(state: CompletionState) {
        completionState = state
    }

    val observers = mutableListOf<(Any, CompletionState) -> Unit>()

    var completionState: CompletionState by Delegates.observable(CompletionState.Uncompleted) { _, _, newValue ->
        observers.forEach { it(this, newValue) }
    }
}

class Probe(
    val heightValue: Int, val direction: Direction,
    val seenTreeList: MutableList<Tree> = mutableListOf()
) : Completable()


class Tree(val treeHeight: Int) : Completable() {
    var north: Tree? = null
    var south: Tree? = null
    var east: Tree? = null
    var west: Tree? = null

    var queue = ConcurrentLinkedQueue<Probe>()
    var returnCount = 0
    var hasSuccess = false

    val probeList: MutableList<Probe> by lazy {
        val list = mutableListOf<Probe>()
        list.add(Probe(treeHeight, Direction.North))
        list.add(Probe(treeHeight, Direction.South))
        list.add(Probe(treeHeight, Direction.East))
        list.add(Probe(treeHeight, Direction.West))

        list.forEach {
            it.observers.add { _, state -> processOnStateChanged(state) }
        }

        list
    }

    init {
        probeList.forEach { queue.add(it) }
    }


    private fun processOnStateChanged(state: CompletionState) {
        returnCount += 1
        if (state == CompletionState.Success) {
            hasSuccess = true
        }

        if (returnCount >= 4) {
            this.completionState = if (hasSuccess) CompletionState.Success else CompletionState.Fail
        }
    }


    fun getTotalPoint(): Int {
        var product = 1
        probeList.forEach {
            product *= it.seenTreeList.size
        }
        return product
    }

    fun onTransfer(probe: Probe) {
        if (probe.heightValue <= treeHeight) {
            probe.onCompletion(CompletionState.Fail)
        } else {
            queue.add(probe)
        }

        probe.seenTreeList.add(this)
    }


    private val timer: Timer by lazy {
        val timer = Timer()
        timer.scheduleAtFixedRate(
            object : TimerTask() {
                override fun run() {
                    transfer()
                }
            },
            0, 1000
        ) // 1000 Millisecond  = 1 second

        timer
    }

    fun startTimer() {
        timer
    }


    fun transfer() {
        if (queue.size > 0) {
            val tracer = queue.remove()

            when (tracer.direction) {
                Direction.North -> {
                    transferWithCheck(tracer, north)
                }

                Direction.South -> {
                    transferWithCheck(tracer, south)
                }

                Direction.East -> {
                    transferWithCheck(tracer, east)
                }

                Direction.West -> {
                    transferWithCheck(tracer, west)
                }
            }
        }
    }

    private fun transferWithCheck(probe: Probe, position: Tree?) {
        if (position == null) {
            probe.onCompletion(CompletionState.Success)
        } else {
            position.onTransfer(probe)
        }
    }
}


/**
 * Main -------------------------------------------------------------------
 */
fun main() {


    class TreeTracker(val grid: Grid<Tree>) {

        val totalCount: Int
        var reportedList = mutableSetOf<Tree>()
        var visibleTreeList = mutableSetOf<Tree>()


        init {
            grid.forAllCell {
                it.observers.add { source, state -> processCompletionStateChange(source, state) }
            }

            totalCount = grid.rowCount() * grid.columnCount()
        }

        private fun processCompletionStateChange(source: Any, state: Completable.CompletionState) {
            when (state) {
                Completable.CompletionState.Success -> {
                    visibleTreeList.add(source as Tree)
                    reportedList.add(source)
                }

                Completable.CompletionState.Fail -> {
                    reportedList.add(source as Tree)
                }

                Completable.CompletionState.Uncompleted -> {

                }
            }
        }

        fun start() {
//            grid.forAllCell {
//                it.startTimer()
//            }
            while (totalCount > reportedList.size) {
                transfer()
            }
        }

        private fun transfer() {
            grid.forAllCell {
                it.transfer()
            }
        }
    }

    fun createGrid(input: List<String>): Grid<Tree> {
        val grid = Grid(input) { Tree(it.digitToInt()) }

        grid.linkLeft { a, b -> b.east = a }
        grid.linkRight { a, b -> b.west = a }
        grid.linkTop { a, b -> b.south = a }
        grid.linkBottom { a, b -> b.north = a }

        return grid
    }

    fun part1(input: List<String>): Int {
        val grid = createGrid(input)
        val tracker = TreeTracker(grid)

        tracker.start()

        return tracker.visibleTreeList.size
    }

    fun part2(input: List<String>): Int {
        val grid = createGrid(input)
        val tracker = TreeTracker(grid)
        tracker.start()

        var max = 0
        grid.forAllCell {
            if (it.getTotalPoint() > max) {
                max = it.getTotalPoint()
            }
        }

        return max
    }


    val input = readInput(8)
    println(part1(input))
    println(part2(input))
}
