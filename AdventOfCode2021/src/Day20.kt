package com.isor.aoc2021

import com.isor.aoc.common.AOC_Runner
import com.isor.aoc.common.TestResources
import com.isor.aoc.common.Year

fun main() {
    Day20().executeGoals()
}

//@TestResources
@Year(2021)
class Day20: AOC_Runner() {

    val algoMap = mutableMapOf<Int,Char>()
    var pixels = mutableMapOf<Pair<Int,Int>, Char>()

    init {
        var isAlgorithm = true
        var algorithm = ""
        var lineId = 0
        allLines.map {
            if(it.isEmpty()) {
                isAlgorithm = false
                return@map
            }
            else if(isAlgorithm) {
                algorithm += it
                return@map
            }
            it.mapIndexed { i,c->
                if(c == '#') {
                    pixels[Pair(lineId, i)] = '1'
                } else {
                    pixels[Pair(lineId, i)] = '0'
                }
            }
            lineId++
        }

        algorithm.mapIndexed{i,c ->
            algoMap[i] = if(c == '#') '1' else '0'
        }
    }

    override fun executeGoal_1() {

        printPixels(0)
        (1..2).forEach {
            enhanceImage(1,if(it%2==1) '0' else '1')
        }

        println(pixels.filter { it.value == '1' }.size)
    }

    override fun executeGoal_2() {
        (3..50).forEach {
            enhanceImage(1,if(it%2==1) '0' else '1')
        }
        println(pixels.filter { it.value == '1' }.size)
        printPixels(0)
    }

    private fun printPixels(extension: Int) {
        val minY = pixels.keys.map { it.first }.minOrNull()!! - extension
        val minX = pixels.keys.map { it.second }.minOrNull()!! - extension
        val maxY = pixels.keys.map { it.first }.maxOrNull()!! + extension
        val maxX = pixels.keys.map { it.second }.maxOrNull()!! + extension

        (minY..maxY).forEach { y ->
            var line = ""
            (minX..maxX).forEach { x ->
                line += if(pixels[Pair(y, x)] == '1') '#' else '.' ?: '.'
            }
            println(line)
        }
    }

    private fun enhanceImage(extension: Int, default: Char) {
        val minY = pixels.keys.map { it.first }.minOrNull()!! - extension
        val minX = pixels.keys.map { it.second }.minOrNull()!! - extension
        val maxY = pixels.keys.map { it.first }.maxOrNull()!! + extension
        val maxX = pixels.keys.map { it.second }.maxOrNull()!! + extension

        val pixels2 = mutableMapOf<Pair<Int, Int>, Char>()

        (minY..maxY).forEach { y ->
            (minX..maxX).forEach { x ->
                val sorrounding = getPixels(y, x)
                val algorithmIndex = sorrounding.map {
                    pixels[it] ?: default
                }.joinToString("").toInt(2)
                val c = algoMap[algorithmIndex]
                if (c != null) pixels2[Pair(y, x)] = c
            }
        }

        pixels = pixels2
    }

    fun getPixels(y: Int, x: Int): List<Pair<Int,Int>> {
        return (y - 1..y + 1).map { yy -> (x - 1..x + 1).map { xx -> Pair(yy, xx) } }.flatten()
    }

}