package com.isor.aoc.common

import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin


fun List<List<Char>>.getColumn(col: Int) : String {
    return this.map { it.elementAt(col) }.joinToString ("")
}

open class AOC_Utility {

    protected val <E> List<E>.copy: MutableList<E>
        get() {
            val copy = mutableListOf<E>()
            this.forEach { copy.add(it) }
            return copy
        }

    fun Int.upperBoundPlus(x:Int, bound:Int) : Int {
        return if (this + x in 0..bound) {
            this + x
        } else {
            this + x - bound - 1
        }
    }

    fun Int.boundedPlus(x:Int, lowerBound:Int, upperBound: Int) : Int {
        val i = this + x
        return if (i in lowerBound..upperBound) {
            i
        } else if (i > upperBound){
            i - upperBound - 1 + lowerBound
        } else {
            upperBound - (lowerBound - i) + 1
        }
    }

    fun String.countChar(a: Char): Int {
        return this.count { c -> c == a }
    }

    fun String.sort () :String {
        return this.toCharArray().sorted().joinToString("")
    }

    fun String.characters() : String {
        return this.toSet().joinToString("")
    }

    fun CharSequence.translateToBinaryString(translationMap: Map<Char, String>) : Int =
            this.map { c -> translationMap[c] }.joinToString("").toInt(2)

    operator fun Pair<Int, Int>.times(value: Int) : Pair<Int, Int> {
        return first * value to second * value
    }

    operator fun Pair<Int, Int>.plus(value: Pair<Int, Int>) : Pair<Int, Int> {
        return first + value.first to second + value.second
    }

    operator fun Pair<Int, Int>.minus(value: Pair<Int, Int>) : Pair<Int, Int> {
        return first - value.first to second - value.second
    }

    fun Pair<Int, Int>.rotate(value: Int) : Pair<Int,Int> {
        val radVal = value.toRad()
        val cosx = cos(radVal).roundToInt()
        val sinx = sin(radVal).roundToInt()
        return Pair((first * cosx - second * sinx), (first * sinx + second * cosx))
    }

    fun Int.toRad() = this * Math.PI / 180

    private fun gcd(a: Long, b:Long) : Long {
        var tb = b
        var ta = a
        while (tb > 0) {
            var temp = tb;
            tb = ta % tb
            ta = temp
        }
        return ta
    }

    protected fun gcd(list: List<Long>) : Long {
        return list.reduce { acc, num -> gcd(acc, num) }
    }

    private fun lcm(a: Long, b: Long): Long {
        return a * (b / gcd(a, b));
    }

    protected fun lcm(list: List<Long>) : Long {
        return list.reduce { acc, num -> lcm(acc, num) }
    }

}
