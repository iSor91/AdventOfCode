package com.isor.aoc2021

import com.isor.aoc.common.AOC_Runner
import com.isor.aoc.common.TestResources
import com.isor.aoc.common.Year
import kotlin.math.floor


fun main() {
    Day24().executeGoals()
}

@TestResources
@Year(2021)
class Day24 : AOC_Runner() {

    val registerMap = mutableMapOf<String, Long>(
        "w" to 0L,
        "x" to 0L,
        "y" to 0L,
        "z" to 0L
    )

    val instructionMap = mapOf<String, (Long, Long) -> Long>(
        "inp" to { _, b -> b },
        "add" to { a, b -> a + b },
        "mul" to { a, b -> a * b },
        "div" to { a, b -> floor(a / b.toDouble()).toLong() },
        "mod" to { a, b -> a % b },
        "eql" to { a, b -> if (a == b) 1 else 0 }
    )

    val regex = "(${instructionMap.keys.joinToString("|")}) (${registerMap.keys.joinToString("|")})(?: ?)(-?\\d+|${
        registerMap.keys.joinToString("|")
    }?)".toRegex()

    override fun executeGoal_1() {
        var w1310Num = 10000
        val matchingNumbers = mutableListOf<String>()

        loop@while (w1310Num > 1111) {
            w1310Num--
            val wList = w1310Num.toString().chunked(1).map { it.toLong() }
            val z1310 = ((((wList[0] + 14)) * 26 + wList[1] + 8) * 26 + wList[2] + 4) * 26 + wList[3] + 10
            val w9 = z1310 % 26 - 3
            if(w9 !in 1..9) {
                continue
            }
            val z9 = floor(z1310 / 26.0).toLong()
            val w8 = (z9) % 26 - 4
            if(w8 !in 1..9) {
                continue
            }

            val z8 = floor(z9/26.0).toLong()

            val w7 = IntArray(9){9-it}
            val validW74 = mutableListOf<Long>()
            w7.forEach w7Loop@{
                val z7 = z8 *26 + it +4
                val w6 = z7 % 26 - 8
                if(w6 !in 1..9) {
                    return@w7Loop
                }
                val z6 = floor(z7 / 26.0).toLong()
                val w5 = z6 % 26 - 3
                if(w5 !in 1..9) {
                    return@w7Loop
                }
                val z5 = floor(z6 / 26.0).toLong()
                val w4 = z5 % 26 - 12
                if(w4 !in 1..9) {
                    return@w7Loop
                }
                val z4 = floor(z6 / 26.0).toLong()

                val w74 = it * 1000 + w6 * 100 + w5 * 10 + w4
                validW74.add(w74)

                val w3List = IntArray(9){9-it}
                val validW32 = mutableListOf<Long>()
                w3List.forEach w3Loop@{ w3 ->
                    val z3 = z4 * 26 + w3
                    val w2 = z3 % 26 - 6
                    if(w2 !in 1..9) {
                        return@w3Loop
                    }
                    val w32 = w3 * 10 + w2
                    validW32.add(w32)
                    val z2 = floor(z3 / 26.0).toLong()

                    val w1List = IntArray(9){9-it}
                    val validW10 = mutableListOf<Long>()
                    w1List.forEach w1Loop@{ w1 ->
                        val z1 = z2 * 26 + w1 +13
                        val w0 = z1 % 26 - 12
                        if(w0 !in 1..9) {
                            return@w1Loop
                        }
                        val w10 = w1 * 10 + w0
                        validW10.add(w10)
                    }
                    if(validW10.isEmpty()) {
                        return@w3Loop
                    }

                    validW10.forEach{w10 ->
                        matchingNumbers.add("${w1310Num}$w9$w8$w74$w32$w10")
                    }
                }
                if(validW32.isEmpty()) {
                    return@w7Loop
                }
            }
            if(validW74.isEmpty()) {
                continue
            }
        }

        val max = matchingNumbers.minByOrNull { it.toLong() }
        println(max)


//        val i = mutableListOf<Int>()
//        val j = mutableListOf<Int>()
//        val k = mutableListOf<Int>()
//
//        allLines.forEach {
//            val split = it.split("=")
//            if (split[0] == "i") {
//                i.add(split[1].toInt())
//            } else if (split[0] == "j") {
//                j.add(split[1].toInt())
//            } else if (split[0] == "k") {
//                k.add(split[1].toInt())
//            }
//        }
//
//        var modelNumber = 100000000000000
//        var z: Long
//        var x: Long
//        var w: Long
//        do {
//            modelNumber-=1111
//            z = 0L
//            x = 0L
//            val integers = modelNumber.toString().chunked(1).map { it.toLong() }
//            integers.indices.forEach {
//                w = integers[it]
//                x = if(z%26+i[it] != w) 1L else 0L
//                z = floor((z/j[it]).toDouble()).toLong()*(25 * x + 1) + (w + k[it])*x
//            }
//            println("$modelNumber -> $z")
//        }while (z != 0L)




//        while(!found) {
//            var start = true
//
//            run restart@{
//                println(modelNumber)
//                allLines.forEach {
//                    val operationParts = regex.find(it)!!.groupValues
//                    val instruction = operationParts[1]
//                    if (instruction == "inp") {
//                        if(start) {
//                            start = false
//                        } else if(registerMap["z"] != 0L) {
//                            modelNumber[bit]--
//                            registerMap["w"] = 0L
//                            registerMap["x"] = 0L
//                            registerMap["y"] = 0L
//                            registerMap["z"] = 0L
//                            return@restart
//                        } else {
//                            bit++
//                        }
//                    }
//                    val targetRegister = operationParts[2]
//                    var resource = operationParts[3]
//                    val resourceLong: Long
//                    if (resource.isEmpty()) {
//                        resourceLong = modelNumber[bit]
//                    } else if (resource.length == 1 && resource.matches(registerMap.keys.joinToString("|").toRegex())) {
//                        resourceLong = registerMap[resource]!!
//                    } else {
//                        resourceLong = resource.toLong()
//                    }
//                    registerMap[targetRegister] =
//                        instructionMap[instruction]!!.invoke(registerMap[targetRegister]!!, resourceLong)
//                }
//                if (registerMap["z"] == 0L) {
//                    println(modelNumber.joinToString(""))
//                    found = true
//                }
//            }
//        }
    }


    override fun executeGoal_2() {
    }
}