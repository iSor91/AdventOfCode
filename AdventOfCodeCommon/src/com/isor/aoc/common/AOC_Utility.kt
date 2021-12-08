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
        return this.filter { c -> c==a}.count()
    }

    fun String.sort () :String {
        return this.toCharArray().sorted().joinToString("")
    }

    fun String.characters() : String {
        return this.toSet().joinToString("")
    }

    fun String.and(other: String) : String {
        val sb = StringBuilder()
        if(this.length != other.length) {
            return this
        }
        this.forEachIndexed {
            i, c ->
            if(other.get(i) == c) {
                sb.append(c)
            }
        }
        return sb.toString()
    }

    open operator fun String.minus(toRemove : String) : String {
        val sb = StringBuilder()
        if(this.length != toRemove.length) {
            return this
        }
        this.forEachIndexed {
            i, c ->
            if(toRemove.get(i) != c) {
                sb.append(c)
            }
        }
        return sb.toString()
    }

    fun CharSequence.translateToBinaryString(translationMap: Map<Char, String>) : Int =
            this.map { c -> translationMap[c] }.joinToString("").toInt(2)


    operator fun Pair<Int, Int>.times(value: Int) : Pair<Int, Int> {
        return this.first * value to this.second * value
    }

    operator fun Pair<Int, Int>.plus(value: Pair<Int, Int>) : Pair<Int, Int> {
        return this.first + value.first to this.second + value.second
    }

    operator fun Pair<Int, Int>.minus(value: Pair<Int, Int>) : Pair<Int, Int> {
        return this.first - value.first to this.second - value.second
    }

    fun Pair<Int, Int>.rotate(value: Int) : Pair<Int,Int> {
        val radVal = toRad(value)
        val cosx = cos(radVal).roundToInt()
        val sinx = sin(radVal).roundToInt()
        return Pair((this.first * cosx - this.second * sinx), (this.first * sinx + this.second * cosx))
    }

    private fun toRad(value: Int) = value.toDouble() * Math.PI / 180

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
