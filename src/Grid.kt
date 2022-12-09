import java.util.function.Consumer
import java.util.function.Function

interface IGrid<T> {
    fun get(r: Int, c: Int): T

    fun rowCount() : Int
    fun columnCount() : Int
}

class Grid<T>(input: List<String>, val conversionFunction: Function<Char, T>) : IGrid<T> {

    private val rowList = mutableListOf<MutableList<T>>()
    private var columnCount = 0

    init {
        for(line in input) {
            val row = mutableListOf<T>()
            line.trim().forEach {
                row.add(conversionFunction.apply(it))
            }

            rowList.add(row)
        }

        columnCount = rowList[0].size
    }

    override fun get(r: Int, c: Int): T {
        return rowList[r][c]
    }

    override fun rowCount(): Int {
        return rowList.size
    }

    override fun columnCount(): Int {
        return columnCount
    }

    fun getRow(r: Int) : List<T> {
        return rowList[r]
    }

    fun interface LinkAction<T> {
        fun link(a: T, b: T)
    }

    fun linkLeft(linkAction: LinkAction<T>) {
        for (r in 0 until rowList.size) {
            val row = getRow(r)

            val rowShifted = mutableListOf<T>()
            rowShifted.addAll(row)
            rowShifted.removeAt(0)

            for (c in 0 until rowShifted.size) {
                val me = rowShifted[c]
                val other = row[c]

                linkAction.link(me, other)
            }
        }
    }

    fun linkRight(linkAction: LinkAction<T>) {
        for (r in 0 until rowList.size) {
            val row = getRow(r)

            val rowShifted = mutableListOf<T>()
            rowShifted.addAll(row)
            rowShifted.removeAt(0)

            for (c in 0 until rowShifted.size) {
                val other = rowShifted[c]
                val me = row[c]

                linkAction.link(me, other)
            }
        }
    }

    fun linkTop(linkAction: LinkAction<T>) {
        for (r in 1 until rowList.size) {
            val previousRow = getRow(r -1)
            val row = getRow(r)

            for (c in 0 until columnCount) {
                val other = previousRow[c]
                val me = row[c]

                linkAction.link(me, other)
            }
        }
    }

    fun linkBottom(linkAction: LinkAction<T>) {
        for (r in 0 until rowList.size - 1) {
            val nextRow = getRow(r +1)
            val row = getRow(r)

            for (c in 0 until columnCount) {
                val other = nextRow[c]
                val me = row[c]

                linkAction.link(me, other)
            }
        }
    }

    fun forAllCell(consumer: Consumer<T>) {
        for (r in 0 until rowCount()) {
            for (c in 0 until columnCount) {
                consumer.accept(get(r, c))
            }
        }
    }
}
