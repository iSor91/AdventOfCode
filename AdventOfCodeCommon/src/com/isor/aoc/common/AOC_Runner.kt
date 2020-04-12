package com.isor.aoc.common

import java.nio.file.Files
import java.nio.file.Paths

abstract class AOC_Runner(private val year: Int) {

    private val moduleName: String = """AdventOfCode${year}"""
    val allLines: List<String> = readAllLines()

    private fun readAllLines() : List<String> {
        val day = this.javaClass.simpleName
        val inputPath = Paths.get(this.moduleName, "resources", day)
        println(inputPath.toAbsolutePath())
        return Files.readAllLines(inputPath)
    }

    fun executeGoals() {
        executeGoal_1()
        executeGoal_2()
    }

    abstract fun executeGoal_1()

    abstract fun executeGoal_2()

}
