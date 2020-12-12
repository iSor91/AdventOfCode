package com.isor.aoc.common

import java.lang.StringBuilder

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

    operator fun String.minus(toRemove : String) : String {
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
}
