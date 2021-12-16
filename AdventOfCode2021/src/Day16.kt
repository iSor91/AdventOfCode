package com.isor.aoc2021

import com.isor.aoc.common.AOC_Runner
import com.isor.aoc.common.TestResources
import com.isor.aoc.common.Year
import kotlin.math.max
import kotlin.math.min

fun main() {
    Day16().executeGoals()
}

@Year(2021)
//@TestResources(14)
class Day16: AOC_Runner() {


    companion object {
        const val LITERAL: Int = 4

        val operationMap = mapOf(
            0 to {a: List<Packet> -> a.sumOf { it.value() }},
            1 to {a: List<Packet> -> a.fold(1L) { acc,i-> acc * i.value() }},
            2 to {a: List<Packet> -> a.minByOrNull { it.value() }!!.value()},
            3 to {a: List<Packet> -> a.maxByOrNull { it.value() }!!.value()},
            5 to {a: List<Packet> -> if(a[0].value()>a[1].value()) 1L else 0L},
            6 to {a: List<Packet> -> if(a[0].value()<a[1].value()) 1L else 0L},
            7 to {a: List<Packet> -> if(a[0].value()==a[1].value()) 1L else 0L}
        )
    }

    abstract class Packet(val string: String, var version: Int, var type: Int) {
        abstract fun flatten(): List<Packet>
        abstract fun value(): Long
    }

    class Literal(string: String, val numeric: Long, version: Int, type: Int): Packet(string, version, type ) {
        override fun flatten(): List<Packet> {
            return mutableListOf(this)
        }

        override fun toString(): String {
            return "Literal - $version [$string=$numeric]"
        }

        override fun value(): Long {
            return this.numeric
        }
    }

    class Operation(string: String, val subPackets: List<Packet>, version: Int, type: Int): Packet(string, version, type){
        override fun toString(): String {
            return """Operation - $version [$string], SubPackets: {
                |    ${subPackets.joinToString("\n\t")}
                |}""".trimMargin()
        }

        override fun flatten(): List<Packet> {
            val allPackets = mutableListOf<Packet>(this)
            allPackets.addAll(this.subPackets.map { it.flatten() }.flatten())
            return allPackets
        }

        override fun value(): Long {
            return operationMap[this.type]!!.invoke(subPackets)
        }
    }

    val bitMap = mapOf(
        '0' to "0000", 
        '1' to "0001", 
        '2' to "0010", 
        '3' to "0011", 
        '4' to "0100", 
        '5' to "0101", 
        '6' to "0110", 
        '7' to "0111", 
        '8' to "1000", 
        '9' to "1001", 
        'A' to "1010", 
        'B' to "1011", 
        'C' to "1100", 
        'D' to "1101", 
        'E' to "1110", 
        'F' to "1111"
    )


    override fun executeGoal_1() {
        val s = allLines.map {
            it.map { c-> bitMap[c] }.joinToString("")
        }
        s.forEach {
            val packet = getPacket(it)
//            println(packet.flatten().sumOf { p -> p.version })
            println(packet.value())
        }
    }

    private fun getPacket(packet: String): Packet {
        val version = packet.substring(0..2).toInt(2)
        val type = packet.substring(3..5).toInt(2)
//        println("$version - $type")

        if (type == LITERAL) {
            val literalValue = getLiteralValue(packet)
            literalValue.type = type
            literalValue.version = version
            return literalValue
        }
        val operation = getOperation(packet)
        operation.type = type
        operation.version = version
        return operation
    }

    private fun getOperation(operation: String): Operation {
        val lengthTypeId = operation[6]
        val subPackets = mutableListOf<Packet>()
        var fullLength = 0
        var substring = ""
        if(lengthTypeId == '0') {
            val totalLength = operation.substring(7..21).toInt(2)
            while(fullLength<totalLength) {
                val packetString = operation.substring(22 + fullLength)
                val packet = getPacket(packetString)
                fullLength += packet.string.length
                subPackets.add(packet)
            }
            substring = operation.substring(0..21 + fullLength)
        } else {
            val packetCount = operation.substring(7..17).toInt(2)
            repeat(packetCount){
                val packetString = operation.substring(18 + fullLength)
                val packet = getPacket(packetString)
                fullLength += packet.string.length
                subPackets.add(packet)
            }
            substring = operation.substring(0..17 + fullLength)
        }
        return Operation(substring,subPackets,0,0)
    }

    private fun getLiteralValue(literal: String): Literal {
        var notLast = true
        var sum = ""
        var i = 6
        while (notLast) {
            val group = literal.substring(i..i + 4)
            i += 5
            if (group.startsWith('0')) {
                notLast = false
            }
            sum += group.substring(1)
        }
        return Literal(literal.substring(0 until i), sum.toLong(2),0,0)
    }

    override fun executeGoal_2() {
    }
}