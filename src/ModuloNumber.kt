import java.lang.IllegalStateException

/**
 * https://en.wikipedia.org/wiki/Modular_arithmetic
 */
class ModuloNumber() {
    private val modulusToIndexMap = mutableMapOf<Int, Int>()

    constructor(seed: List<Pair<Int, Int>>) : this() { //modulus, index
        seed.forEach {
            modulusToIndexMap[it.first] = it.second
        }
    }

    var scalar = 0
    var asScalar = false

    constructor(seedValue: Int, asScalar: Boolean) : this() {
        scalar = seedValue
        this.asScalar = asScalar
    }

    constructor(seedValue: Int) : this() {
        modulusToResidueMap.keys.forEach {
            modulus ->

            val residue = modulusToResidueMap[modulus]
            val remainder = seedValue % modulus

            modulusToIndexMap.putIfAbsent(modulus, residue!!.indexOf(remainder))
        }

        printInfo()
    }

    fun getValue(modulus: Int): Int {
        val index = modulusToIndexMap[modulus]
        val residue = modulusToResidueMap[modulus]
        return residue!![index!!]
    }

    operator fun plus(additive: Int): ModuloNumber {
        return plus(ModuloNumber(additive, true))
    }

    operator fun plus(additive: ModuloNumber): ModuloNumber {
        return applyOperator(additive, ::plus)
    }


    private fun plus(me: Int, other: Int): Int {
        return me + other
    }


    operator fun times(multiplier: Int): ModuloNumber {
        return times(ModuloNumber(multiplier, true))
    }

    operator fun times(multiplier: ModuloNumber): ModuloNumber {
        return applyOperator(multiplier, ::times)
    }

    private fun times(me: Int, other: Int): Int {
        return me * other
    }


    operator fun div(divisor: Int): ModuloNumber {
        return div(ModuloNumber(divisor, true))
    }

    operator fun div(divisor: ModuloNumber): ModuloNumber {
        return applyOperator(divisor, ::div)
    }

    private fun div(me: Int, other: Int): Int {
        return me / other
    }


    fun printInfo() {
//        System.err.println("INFO----------------------")
//        indexMap.forEach {
//            System.err.println("Info>> " + it.key)
//        }
//        System.err.println("--------------------------")
    }

    private fun applyOperator(other: ModuloNumber, operation: (me: Int, other: Int) -> Int): ModuloNumber {
        val list = mutableListOf<Pair<Int, Int>>()
        other.printInfo()

        modulusToResidueMap.keys.forEach {
            modulus ->

            val residue = modulusToResidueMap[modulus]!!

            if (asScalar && other.asScalar) {
                throw IllegalStateException()
            }

            if (asScalar) { //i am scalar
                val otherValue = operation(other.getValue(modulus), scalar)
                val remainder = otherValue % modulus
                val index = residue.indexOf(remainder)
                list.add(Pair(modulus, index))
            } else if (other.asScalar) { // other scalar
                val myValue = operation(getValue(modulus), other.scalar) //only thing that's different from +
                val remainder = myValue % modulus
                val index = residue.indexOf(remainder)
                list.add(Pair(modulus, index))
            } else {
                val myValue = getValue(modulus)
                val otherValue = other.getValue(modulus)

                val operationResult = operation(myValue, otherValue)

                val remainder = operationResult % modulus
                val index = residue.indexOf(remainder)
                list.add(Pair(modulus, index))
            }
        }

        return ModuloNumber(list)
    }

    companion object {
        val modulusToResidueMap = mutableMapOf<Int, List<Int>>()
        fun initialize(modulus: Int) {
            println("ADD CLASS: $modulus ---------------------------------------")
            modulusToResidueMap.putIfAbsent(modulus, (0 until modulus).toList())
            println("ADD CLASS END -------------------------- ${modulusToResidueMap.size}")
        }
    }
}

fun String.toModuloNumber(): ModuloNumber {
    return ModuloNumber(this.toInt())
}