package com.isor.aoc2018

import java.lang.StringBuilder

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