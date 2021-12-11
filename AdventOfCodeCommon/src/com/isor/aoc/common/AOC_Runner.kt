package com.isor.aoc.common

import java.lang.IllegalStateException
import java.nio.file.Files
import java.nio.file.Paths

@RequiredAnnotations([Year::class])
abstract class AOC_Runner : AOC_Utility() {

    val allLines: List<String>

    init {
        validateAnnotations()
        allLines =  readAllLines()
    }

    private fun readAllLines() : List<String> {
        val year = javaClass.getAnnotation(Year::class.java).year
        val moduleName = "AdventOfCode${year}"

        val test = if(javaClass.isAnnotationPresent(TestResources::class.java)) "-test" else ""
        val resourcesFolder = "resources$test"

        val day = javaClass.simpleName

        val inputPath = Paths.get(moduleName, resourcesFolder, day)
        println(inputPath.toAbsolutePath())
        return Files.readAllLines(inputPath)
    }

    private fun validateAnnotations() {
        val required = AOC_Runner::class.java.getAnnotation(RequiredAnnotations::class.java)
        val requiredAnnotations = required.annotations.map { it.java }
        val missingAnnotations = requiredAnnotations.filter { !javaClass.annotations.map { a -> a.annotationClass.java }.contains(it) }
            .map { it.canonicalName }
        if(missingAnnotations.isNotEmpty()) {
            throw IllegalStateException("Missing Annotation(s): ${missingAnnotations.fold("") { acc, c -> "$acc\n - [$c]" }}")
        }
    }

    fun executeGoals() {
        executeGoal_1()
        executeGoal_2()
    }

    abstract fun executeGoal_1()

    abstract fun executeGoal_2()

}
