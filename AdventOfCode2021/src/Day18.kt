package com.isor.aoc2021

import com.isor.aoc.common.AOC_Runner
import com.isor.aoc.common.TestResources
import com.isor.aoc.common.Year
import kotlin.math.ceil
import kotlin.math.floor

fun main() {
    Day18().executeGoals()
}

@TestResources(10)
@Year(2021)
class Day18: AOC_Runner() {

    companion object{
        var index = 0
    }

    data class SnailFishNum(
        val id : Int,
        val originalString: String,
        var parent: SnailFishNum? = null,
        var left: SnailFishNum? = null,
        var right: SnailFishNum? = null,
        var leftInt: Int? = null,
        var rightInt: Int? = null
    ) {

        override fun equals(other: Any?): Boolean {
            return other is SnailFishNum && other.id == this.id
        }

        override fun hashCode(): Int {
            return this.id.hashCode()
        }

        fun magnitude(): Long {
            return 1L.times(
                3L.times(leftInt?.toLong()?:left!!.magnitude())
                    .plus(2L.times(rightInt?.toLong()?:right!!.magnitude()))
            )
        }

        fun getLeft(): Any? {
            return left ?: leftInt
        }
        fun getRight(): Any? {
            return right ?: rightInt
        }

        override fun toString(): String {
            return "[${getLeft()},${getRight()}]"
        }

        fun getLevel(): Int {
            return if(parent == null) 0 else parent!!.getLevel() + 1
        }

        fun getExplodee(): SnailFishNum? {
            if(getLevel() >= 4) {
                return this
            }
            return this.left?.getExplodee() ?: this.right?.getExplodee()
        }

        fun getSplitee(): SnailFishNum? {
            if((leftInt != null && leftInt!! >= 10)) {
                return this
            } else if(left?.getSplitee() != null) {
                return left?.getSplitee()
            } else if(rightInt != null && rightInt!! >= 10) {
                return this
            }
            return this.right?.getSplitee()
        }

        fun split() {
            if(leftInt != null && leftInt!! >= 10) {
                this.left = SnailFishNum(index++, "", parent = this, leftInt = floor(leftInt!!/2.0).toInt(), rightInt = ceil(leftInt!!/2.0).toInt())
                this.leftInt = null
            } else if(rightInt!=null&& rightInt!! >=10) {
                this.right = SnailFishNum(index++, "", parent = this, leftInt = floor(rightInt!!/2.0).toInt(), rightInt = ceil(rightInt!!/2.0).toInt())
                this.rightInt = null
            }
        }

        fun explode(){
            if(getLevel() < 4) {
                return
            }
            when(this) {
                parent!!.left -> {
                    if(parent!!.rightInt != null) {
                        parent!!.rightInt = parent!!.rightInt?.plus(this.rightInt!!)
                    } else {
                        val leftMostWithInt = parent!!.right!!.getLeftMostWithInt()
                        leftMostWithInt.leftInt = leftMostWithInt.leftInt?.plus(this.rightInt!!)
                    }
                    val neighborStart = getRightParent()
                    if(neighborStart?.leftInt != null) {
                        neighborStart.leftInt = neighborStart.leftInt!!.plus(this.leftInt!!)
                    } else {
                        val leftMostWithInt = neighborStart?.left?.getRightMostWithInt()
                        leftMostWithInt?.rightInt = leftMostWithInt?.rightInt?.plus(this.leftInt!!)
                    }
                    parent!!.left = null
                    parent!!.leftInt = 0
                }
                parent!!.right -> {
                    if(parent!!.leftInt != null) {
                        parent!!.leftInt = parent!!.leftInt?.plus(this.leftInt!!)
                    } else {
                        val rightMostWithInt = parent!!.left!!.getRightMostWithInt()
                        rightMostWithInt.rightInt = rightMostWithInt.rightInt?.plus(this.leftInt!!)
                    }
                    val neighborStart = getLeftParent()
                    if(neighborStart?.rightInt != null) {
                        neighborStart.rightInt = neighborStart.rightInt!!.plus(this.rightInt!!)
                    } else {
                        val rightMostWithInt = neighborStart?.right?.getLeftMostWithInt()
                        rightMostWithInt?.leftInt = rightMostWithInt?.leftInt?.plus(this.rightInt!!)
                    }
                    parent!!.right = null
                    parent!!.rightInt = 0
                }
            }
        }

        fun getRightParent(): SnailFishNum? {
            if(parent == null) {
                return null
            }
            if(parent!!.right == this) {
                return parent
            }
            return parent!!.getRightParent()
        }

        fun getLeftParent(): SnailFishNum? {
            if(parent == null) {
                return null
            }
            if(parent!!.left == this) {
                return parent
            }
            return parent!!.getLeftParent()
        }

        fun getRightMostWithInt():SnailFishNum {
            return if(this.rightInt != null) this else this.right!!.getRightMostWithInt()
        }

        fun getLeftMostWithInt():SnailFishNum {
            return if(this.leftInt != null) this else this.left!!.getLeftMostWithInt()
        }

    }

    fun SnailFishNum.add(other: SnailFishNum): SnailFishNum {
        val newSnailFishNum = SnailFishNum(index++, "", left = this, right = other)
        newSnailFishNum.parent = this.parent
        this.parent = newSnailFishNum
        other.parent = newSnailFishNum
        return newSnailFishNum
    }

    val a = allLines.map {
        readSnailFishNumber(it)
    }

    private fun readSnailFishNumber(it: String): SnailFishNum? {
        var top: SnailFishNum? = null
        var parent: SnailFishNum? = null
        var current: SnailFishNum? = top
        for (i in it) {
            if (i == '[') {
                parent = current
                current = SnailFishNum(index++, it, parent = parent)
                if (top == null) top = current
                if (parent?.left == null && parent?.leftInt == null)
                    parent?.left = current
                else
                    parent.right = current
            } else if (i == ']') {
                current = current!!.parent
                parent = parent?.parent
            } else if (i.isDigit()) {
                if (current?.left == null && current?.leftInt == null) {
                    current?.leftInt = i.toString().toInt()
                } else
                    current?.rightInt = i.toString().toInt()
            }
        }
        return top
    }

    override fun executeGoal_1() {
//        val subList = a.subList(1, a.size)
//        val folded = subList.fold(a[0]) { acc, num ->
//            val add = acc!!.add(num!!)
//            var explodee = add.getExplodee()
//            var splitee = add.getSplitee()
//            while (explodee != null || splitee != null) {
////                println("$add \n\t- $explodee \n\t- $splitee" )
//                if(explodee != null) {
//                    explodee.explode()
//                } else {
//                    splitee?.split()
//                }
//                explodee = add.getExplodee()
//                splitee = add.getSplitee()
//            }
//            add
//        }
//        println(folded)
//
//        println(folded!!.magnitude())


    }

    override fun executeGoal_2() {

        val allPossiblePairs = mutableListOf<Pair<SnailFishNum, SnailFishNum>>()
        a.forEach { first ->
            a.forEach { second ->
                if(first != second)
                    allPossiblePairs.add(Pair(readSnailFishNumber(first!!.originalString)!!, readSnailFishNumber(second!!.originalString)!!))
            }
        }

        val maxOf = allPossiblePairs.map {
            val add = it.first.add(it.second)
            var explodee = add.getExplodee()
            var splitee = add.getSplitee()
            while (explodee != null || splitee != null) {
//                println("$add \n\t- $explodee \n\t- $splitee" )
                if (explodee != null) {
                    explodee.explode()
                } else {
                    splitee?.split()
                }
                explodee = add.getExplodee()
                splitee = add.getSplitee()
            }
            val magnitude = add.magnitude()
            val result = add.toString()
            it.first.parent = null
            it.second.parent = null
            Pair("${it.first.originalString} + ${it.second.originalString} = $result",magnitude)
        }

        maxOf.sortedBy { it.second }.forEach { println(it) }
    }
}