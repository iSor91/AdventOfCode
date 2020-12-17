import com.isor.aoc.common.AOC_Runner
import com.isor.aoc.common.TestResources
import com.isor.aoc.common.Year

fun main() {
    Day17().executeGoals()
}

//@TestResources
@Year(2020)
class Day17 : AOC_Runner() {

    val active = '#'
    val inactive = '.'
    val baseState : MutableList<CharArray>

    var cubeArray3d = mutableListOf<MutableList<CharArray>>()
    var cubeArray4d = mutableListOf<MutableList<MutableList<CharArray>>>()

    init {
        baseState = allLines.map { it.toCharArray() }.toMutableList()
        cubeArray3d.add(baseState)
        cubeArray4d.add(cubeArray3d)
    }

    fun MutableList<MutableList<MutableList<CharArray>>>.extend4dCharArray(): MutableList<MutableList<MutableList<CharArray>>> {
        val map = this.map { it.extend3dCharArray() }.toMutableList()
        map.add(0, map.createDefault3dArray4d())
        map.add(map.createDefault3dArray4d())
        return map
    }

    fun MutableList<MutableList<CharArray>>.extend3dCharArray() : MutableList<MutableList<CharArray>>{
        val map = this.map { it.extend2dCharArray() }.toMutableList()
        map.add(0,map.createDefault2DArray())
        map.add(map.createDefault2DArray())
        return map
    }

    fun MutableList<CharArray>.extend2dCharArray() : MutableList<CharArray> {
        val columnSize = this[0].size + 2
        val newCube = mutableListOf<CharArray>()
        this.forEach {
            val newLine2 = it.extend1dCharArray()
            newCube.add(newLine2)
        }
        newCube.add(0,createDefault1DArray(columnSize))
        newCube.add(createDefault1DArray(columnSize))
        return newCube
    }

    private fun CharArray.extend1dCharArray() = "$inactive${this.joinToString("")}$inactive".toCharArray()

    private fun MutableList<MutableList<MutableList<CharArray>>>.createDefault4dArray() : MutableList<MutableList<MutableList<CharArray>>> {
        val mutableListOf = mutableListOf<MutableList<MutableList<CharArray>>>()
        repeat(this.size) {mutableListOf.add(createDefault3dArray4d())}
        return mutableListOf
    }

    private fun MutableList<MutableList<MutableList<CharArray>>>.createDefault3dArray4d() : MutableList<MutableList<CharArray>> {
        val mutableList = this[0]
        return mutableList.createDefault3dArrayBase()
    }

    private fun MutableList<MutableList<CharArray>>.createDefault3dArrayBase() : MutableList<MutableList<CharArray>> {
        val mutableListOf = mutableListOf<MutableList<CharArray>>()
        repeat(this.size) { mutableListOf.add(createDefault2DArray()) }
        return mutableListOf
    }

    private fun MutableList<MutableList<CharArray>>.createDefault2DArray(): MutableList<CharArray> {
        val firstCubeArray = this[0]
        val rowSize = firstCubeArray.size

        val columnSize = firstCubeArray[0].size
        val newCube = mutableListOf<CharArray>()
        repeat(rowSize) { newCube.add(createDefault1DArray(columnSize)) }
        return newCube
    }

    private fun createDefault1DArray(columnSize: Int) = CharArray(columnSize) { inactive }

    fun MutableList<MutableList<CharArray>>.update3D(): MutableList<MutableList<CharArray>> {
        val chararray3d = this.createDefault3dArrayBase()

        for (z in this.indices) {
            val chararray2d = this[z]
            for (x in chararray2d.indices) {
                val chararray = chararray2d[x]
                for (y in chararray.indices) {
                    chararray3d[z][x][y] = getState3d(Triple(x,y,z), Triple(chararray2d.size,chararray.size,chararray3d.size))
                }
            }
        }
        return chararray3d
    }

    fun MutableList<MutableList<MutableList<CharArray>>>.update4D(): MutableList<MutableList<MutableList<CharArray>>> {
        val chararray4d = this.createDefault4dArray()

        for (w in this.indices) {
            val chararray3d = this[w]
            for (z in chararray3d.indices) {
                val chararray2d = chararray3d[z]
                for (x in chararray2d.indices) {
                    val chararray = chararray2d[x]
                    for (y in chararray.indices) {
                        chararray4d[w][z][x][y] = getState4d(Triple(x,y,z), Triple(chararray2d.size,chararray.size,chararray3d.size), w, this.size)
                    }
                }
            }
        }
        return chararray4d
    }

    private fun getState4d(triple: Triple<Int, Int, Int>, size: Triple<Int, Int, Int>, currentW: Int, wsize: Int): Char {
        val currentX = triple.first
        val currentY = triple.second
        val currentZ = triple.third
        val xStart = currentX + getStartIndex(currentX)
        val yStart = currentY + getStartIndex(currentY)
        val zStart = currentZ + getStartIndex(currentZ)
        val wStart = currentW + getStartIndex(currentW)
        val xEnd = currentX + size.first.getEndIndex(currentX)
        val yEnd = currentY + size.second.getEndIndex(currentY)
        val zEnd = currentZ + size.third.getEndIndex(currentZ)
        val wEnd = currentW + wsize.getEndIndex(currentW)

        var activeCount = 0
        for(w in wStart..wEnd) {
            for(z in zStart..zEnd) {
                for (x in xStart..xEnd) {
                    for(y in yStart..yEnd) {
                        if(x == currentX && y == currentY && z == currentZ && w == currentW) continue
                        activeCount += if(cubeArray4d[w][z][x][y] == active) 1 else 0
                    }
                }
            }
        }

        return when(val current = cubeArray4d[currentW][currentZ][currentX][currentY]) {
            active -> if(activeCount !in 2..3)  inactive else active
            inactive -> if(activeCount == 3) active else inactive
            else -> current
        }
    }

    private fun getState3d(triple: Triple<Int,Int,Int>, size: Triple<Int,Int,Int>) : Char {

        val currentX = triple.first
        val currentY = triple.second
        val currentZ = triple.third
        val xStart = currentX + getStartIndex(currentX)
        val yStart = currentY + getStartIndex(currentY)
        val zStart = currentZ + getStartIndex(currentZ)
        val xEnd = currentX + size.first.getEndIndex(currentX)
        val yEnd = currentY + size.second.getEndIndex(currentY)
        val zEnd = currentZ + size.third.getEndIndex(currentZ)

        var activeCount = 0
        for(z in zStart..zEnd) {
            for (x in xStart..xEnd) {
                for(y in yStart..yEnd) {
                    if(x == currentX && y == currentY && z == currentZ) continue
                    activeCount += if(cubeArray3d[z][x][y] == active) 1 else 0
                }
            }
        }

        val current = cubeArray3d[currentZ][currentX][currentY]
        return when(current) {
            active -> if(activeCount !in 2..3)  inactive else active
            inactive -> if(activeCount == 3) active else inactive
            else -> current
        }
    }

    private fun getStartIndex(i: Int) : Int {
        return when (i) {
            0 -> 0
            else -> -1
        }
    }

    private fun Int.getEndIndex(i: Int) : Int {
        return when (i) {
            this-1 -> 0
            else -> 1
        }
    }

    private fun printCubes3D() {
        cubeArray3d.forEach { list -> list.forEach{ println(it)} }
        println()
    }

    override fun executeGoal_1() {
        repeat(6) {
            cubeArray3d = cubeArray3d.extend3dCharArray()
            cubeArray3d = cubeArray3d.update3D()
        }
        val actives = cubeArray3d.sumBy { it.sumBy { row -> row.sumBy { c -> if (c == active) 1 else 0 } } }
        println(actives)
    }

    override fun executeGoal_2() {
        repeat(6) {
            cubeArray4d = cubeArray4d.extend4dCharArray()
            cubeArray4d = cubeArray4d.update4D()
        }
        val actives = cubeArray4d.sumBy { it.sumBy { thirdDim -> thirdDim.sumBy{ row -> row.sumBy { c -> if (c == active) 1 else 0 } } } }
        println(actives)
    }



}