package com.isor.aoc2021

import com.isor.aoc.common.AOC_Runner
import com.isor.aoc.common.TestResources
import com.isor.aoc.common.Year
import kotlin.math.abs

fun main() {
    Day19().executeGoals()
}

@TestResources(4)
@Year(2021)
class Day19 : AOC_Runner() {

    private val MINIMAL_MATCH: Int = 10

    override fun executeGoal_1() {
        val scanners = mutableListOf<Scanner>()

        val beaconRegex = Regex("--- scanner (\\d+) ---")
        allLines.forEach {
            if (beaconRegex.matches(it)) {
                val id = beaconRegex.find(it)!!.groupValues[1].toInt()
                scanners.add(Scanner(id))
            } else if (it.isEmpty())
                return@forEach
            else {
                val map = it.split(",").map { s -> s.toInt() }
                val initialPos = Triple(map[0], map[1], map[2])
                scanners.last().beacons.add(Beacon(initialPos, absolutePosition = initialPos))
            }
        }

        scanners.forEach { it.updateRelativePositions() }
        println()
        scanners[0].position = Triple(0, 0, 0)
        scanners[0].orientationTo0Done = true

        while (scanners.any { !it.orientationTo0Done }) {
            scanners.filter { it.orientationTo0Done }.forEach { base ->
                scanners.filter { !it.orientationTo0Done }.forEach { check ->
                    mapScanners(check, base)
                }
            }
        }

//        scanners.forEach{println(it.toStringFinal())}
        scanners.forEach { println("Scanner ${it.id} at ${it.position}") }

        val beacons = scanners.map { it.beacons.map { b -> b.absolutePosition } }.flatten().toSet()
//        println(beacons.sortedWith { a, b ->
//            if(a.first == b.first) if(a.second == b.second) a.third - b.third else a.second - b.second else a.first - b.first
//        }.joinToString("\n"))

        println("Scanners that are not identified: " + scanners.filter { it.position == null })

        println(beacons.size)

        var distance = 0
        scanners.forEach { first ->
            scanners.forEach { second ->
                val triple = first.position!! - second.position!!
                val i = abs(triple.first) + abs(triple.second) + abs(triple.third)
                if (i > distance) {
                    distance = i
                }
            }
        }
        println(distance)
    }

    private fun mapScanners(checkedScanner: Scanner, baseScanner: Scanner) {
        var rotation: Int? = null
        var matchingBeacon: Beacon? = null

        var base = baseScanner
        var check = checkedScanner
        if (check.position != null && base.position != null) {
            return
        }
        if (check.position != null && base.position == null) {
//            println("Scanner $checkedScanner already found ad ${check.position}")
            val tmp = base
            base = check
            check = tmp
        }

        if (!base.orientationTo0Done) {
            return
        }

        val referencePositions = base.beacons.map { it to it.relativePositions }
        var matchingBeaconToRotate: Beacon?

        try {
            matchingBeaconToRotate = check.beacons.first() {
                val rotationMap = it.relativePositions.createRotationMap2()
                try {
                    rotation = rotationMap.entries.first { rotatedScanner ->
                        try {
                            matchingBeacon = referencePositions.first { referenceScanner ->
                                rotatedScanner.value.count { rotatedPos -> referenceScanner.second.contains(rotatedPos) } > MINIMAL_MATCH
                            }.first
                            true
                        } catch (e: NoSuchElementException) {
                            false
                        }
                    }.key
                    true
                } catch (e: NoSuchElementException) {
                    false
                }
            }
        } catch (e: NoSuchElementException) {
//            println("Beacon $checkedScanner does not overlap with beacon $baseScanner")
            return
        }

//        println("!!!Beacon $checkedScanner overlap with beacon $baseScanner!!!")

        val rotatedMatching = matchingBeaconToRotate.initialPos.rotate2(rotation!!)

        println(rotation)
//        println(matchingBeacon)
//        println(rotatedMatching)
//        println()

        check.position = matchingBeacon!!.absolutePosition + (rotatedMatching * -1)
        check.beacons.forEach {
            val rotatedInitial = it.initialPos.rotate2(rotation!!)
            it.absolutePosition = rotatedInitial - rotatedMatching + matchingBeacon!!.absolutePosition
        }
        check.updateRelativePositions()
        check.orientationTo0Done = true
    }

    private operator fun Triple<Int, Int, Int>.plus(other: Triple<Int, Int, Int>): Triple<Int, Int, Int> {
        return Triple(first + other.first, second + other.second, third + other.third)
    }

    private operator fun Triple<Int, Int, Int>.minus(other: Triple<Int, Int, Int>): Triple<Int, Int, Int> {
        return Triple(
            first - other.first,
            second - other.second,
            third - other.third
        )
    }

    private operator fun Triple<Int, Int, Int>.times(c: Int): Triple<Int, Int, Int> {
        return Triple(first * c, second * c, third * c)
    }

    override fun executeGoal_2() {
    }

    fun Scanner.updateRelativePositions() {
        beacons.forEach {
            it.relativePositions = beacons.map { b ->
                b.absolutePosition - it.absolutePosition
            }
        }
    }

    data class Scanner(
        val id: Int,
        val beacons: MutableList<Beacon> = mutableListOf(),
        var position: Triple<Int, Int, Int>? = null,
        var orientationTo0Done: Boolean = false
    ) {
        override fun equals(other: Any?): Boolean {
            return other is Scanner && other.id == id
        }

        override fun hashCode(): Int {
            return id.hashCode()
        }

        override fun toString(): String {
            return """Scanner $id - $position
                |   ${beacons.joinToString("\n\t")}""".trimMargin()
        }

        fun toStringFinal(): String {
            return """Scanner $id - $position
                |   ${
                beacons.map { it.absolutePosition }.sortedBy { it.toString() }.joinToString("\n\t")
            }""".trimMargin()
        }
    }

    data class Beacon(
        val initialPos: Triple<Int, Int, Int>,
        var relativePositions: List<Triple<Int, Int, Int>> = listOf(),
        var absolutePosition: Triple<Int, Int, Int> = Triple(Int.MAX_VALUE, Int.MAX_VALUE, Int.MAX_VALUE)
    ) {
        override fun toString(): String {
            return "$initialPos"
        }
    }


    fun List<Triple<Int, Int, Int>>.createRotationMap(): MutableMap<Pair<Int, Triple<Int, Int, Int>>, List<Triple<Int, Int, Int>>> {
        val rotationMap = mutableMapOf<Pair<Int, Triple<Int, Int, Int>>, List<Triple<Int, Int, Int>>>()
        val fullRotation = arrayOf(-1, 1)
        fullRotation.forEach { x ->
            fullRotation.forEach { y ->
                fullRotation.forEach { z ->
                    (0..5).forEach { shift ->
                        val key = Pair(shift, Triple(x, y, z))
                        rotationMap[key] = this.map { it.rotate(key) }
                    }
                }
            }
        }
        return rotationMap
    }

    fun Triple<Int, Int, Int>.rotate(rotation: Pair<Int, Triple<Int, Int, Int>>): Triple<Int, Int, Int> {
        val shift = rotation.first
        val x = first * rotation.second.first
        val y = second * rotation.second.second
        val z = third * rotation.second.third
        return when (shift) {
            0 -> Triple(x, y, z)
            3 -> Triple(x, z, y)
            1 -> Triple(y, z, x)
            4 -> Triple(y, x, z)
            2 -> Triple(z, x, y)
            else -> Triple(z, y, x)
        }
    }


    fun List<Triple<Int, Int, Int>>.createRotationMap2(): MutableMap<Int, List<Triple<Int, Int, Int>>> {
        val rotationMap = mutableMapOf<Int, List<Triple<Int, Int, Int>>>()
        (0..23).forEach { key ->
            rotationMap[key] = this.map { it.rotate2(key) }
        }
        return rotationMap
    }

    fun Triple<Int, Int, Int>.rotate2(rotation: Int): Triple<Int, Int, Int> {
        val x = first
        val y = second
        val z = third
        return when (rotation) {
            0 -> Triple(x, y, z)
            1 -> Triple(x, z, -y)
            2 -> Triple(x, -y, -z)
            3 -> Triple(x, -z, y)

            4 -> Triple(-x, -y, z)
            5 -> Triple(-x, z, y)
            6 -> Triple(-x, y, -z)
            7 -> Triple(-x, -z, -y)

            8 -> Triple(y, x, -z)
            9 -> Triple(y, -x, z)
            10 -> Triple(y, z, x)
            11 -> Triple(y, -z, -x)

            12 -> Triple(-y, x, z)
            13 -> Triple(-y, -x, -z)
            14 -> Triple(-y, z, -x)
            15 -> Triple(-y, -z, x)

            16 -> Triple(z, y, -x)
            17 -> Triple(z, -y, x)
            18 -> Triple(z, x, y)
            19 -> Triple(z, -x, -y)

            20 -> Triple(-z, y, x)
            21 -> Triple(-z, -y, -x)
            22 -> Triple(-z, x, -y)
            23 -> Triple(-z, -x, y)

            else -> throw IllegalStateException("invalid rotation requested")
        }
    }

}

