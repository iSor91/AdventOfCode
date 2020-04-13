package com.isor.aoc.common

import java.nio.file.Files
import java.nio.file.Paths


abstract class AOC_Runner() {
    val allLines: List<String>  = readAllLines()

    private fun readAllLines() : List<String> {

        val annotations = javaClass.annotations
        val year = (annotations.filter { a -> a is Year }[0] as Year).year
        val moduleName: String = "AdventOfCode${year}"

        var test = ""
        if(annotations.any { a -> a is TestResources }) {
            test = "\\test"
        }
        val resourcesFolder = "resources$test"

        val day = javaClass.simpleName

        val inputPath = Paths.get(moduleName, resourcesFolder, day)
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
