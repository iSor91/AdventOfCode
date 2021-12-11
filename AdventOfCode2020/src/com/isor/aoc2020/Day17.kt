import com.isor.aoc.common.AOC_Runner
import com.isor.aoc.common.Year

fun main() {
    Day17().executeGoals()
}

//@TestResources
@Year(2020)
class Day17 : AOC_Runner() {

    val active = '#'
    val inactive = '.'
    val baseState: MutableList<CharArray>

    var cubeArray3d = mutableListOf<MutableList<CharArray>>()
    var cubeArray4d = mutableListOf<MutableList<MutableList<CharArray>>>()

    init {
        baseState = allLines.map { it.toCharArray() }.toMutableList()
        cubeArray3d.add(baseState)
        cubeArray4d.add(cubeArray3d)
    }

    fun MutableList<MutableList<MutableList<CharArray>>>.extend4dCharArray(): MutableList<MutableList<MutableList<CharArray>>> {
        val map = this.map { it.extend3dCharArray() }.toMutableList()
        map.add(0, createDefault3dArray(map[0].size, map[0][0].size, map[0][0][0].size))
        map.add(createDefault3dArray(map[0].size, map[0][0].size, map[0][0][0].size))
        return map
    }

    fun MutableList<MutableList<CharArray>>.extend3dCharArray(): MutableList<MutableList<CharArray>> {
        val map = this.map { it.extend2dCharArray() }.toMutableList()
        map.add(0, createDefault2DArray(map[0].size, map[0][0].size))
        map.add(createDefault2DArray(map[0].size, map[0][0].size))
        return map
    }

    fun MutableList<CharArray>.extend2dCharArray(): MutableList<CharArray> {
        val map = this.map { it.extend1dCharArray() }.toMutableList()
        map.add(0, createDefault1DArray(map[0].size))
        map.add(createDefault1DArray(map[0].size))
        return map
    }

    fun CharArray.extend1dCharArray() = "${'-'}${this.joinToString("")}${'.'}".toCharArray()

    private fun createDefault4dArray(cube: Int, vararg subSizes: Int): MutableList<MutableList<MutableList<CharArray>>> {
        val mutableListOf = mutableListOf<MutableList<MutableList<CharArray>>>()
        repeat(cube) { mutableListOf.add(createDefault3dArray(subSizes[0], *subSizes.sliceArray(1 until subSizes.size))) }
        return mutableListOf
    }

    private fun createDefault3dArray(matrixes: Int, vararg subSizes: Int): MutableList<MutableList<CharArray>> {
        val mutableListOf = mutableListOf<MutableList<CharArray>>()
        repeat(matrixes) { mutableListOf.add(createDefault2DArray(subSizes[0], *subSizes.sliceArray(1 until subSizes.size))) }
        return mutableListOf
    }

    private fun createDefault2DArray(rows: Int, vararg subSizes: Int): MutableList<CharArray> {
        val newCube = mutableListOf<CharArray>()
        repeat(rows) { newCube.add(createDefault1DArray(subSizes[0])) }
        return newCube
    }

    private fun createDefault1DArray(columnSize: Int) = CharArray(columnSize) { '.' }

    private fun countActiveAdjacent4d(currentX: Int, currentY: Int, currentZ: Int, currentW: Int, current4Dim: List<List<List<CharArray>>>, skip: Boolean): Int {
        val wStart = currentW + getStartIndex(currentW)
        val wEnd = currentW + current4Dim.size.getEndIndex(currentW)
        var activeCount = 0
        for (w in wStart..wEnd) {
            activeCount += countActiveAdjacent3d(currentX, currentY, currentZ, current4Dim[w], w == currentW && skip)
        }
        return activeCount
    }

    private fun countActiveAdjacent3d(currentX: Int, currentY: Int, currentZ: Int, currentCube: List<List<CharArray>>, skip: Boolean): Int {
        val zStart = currentZ + getStartIndex(currentZ)
        val zEnd = currentZ + currentCube.size.getEndIndex(currentZ)
        var activeCount = 0
        for (z in zStart..zEnd) {
            activeCount += countActiveAdjacent2d(currentX, currentY, currentCube[z], z == currentZ && skip)
        }
        return activeCount
    }

    private fun countActiveAdjacent2d(currentX: Int, currentY: Int, currentMatrix: List<CharArray>, skip: Boolean): Int {
        val xStart = currentX + getStartIndex(currentX)
        val xEnd = currentX + currentMatrix.size.getEndIndex(currentX)
        var activeCount = 0
        for (x in xStart..xEnd) {
            activeCount += countActiveAdjacent1d(currentY, currentMatrix[x], x == currentX && skip)
        }
        return activeCount
    }

    private fun countActiveAdjacent1d(currentY: Int, currentArray: CharArray, skip: Boolean): Int {
        val yStart = currentY + getStartIndex(currentY)
        val yEnd = currentY + currentArray.size.getEndIndex(currentY)
        var activeCount = 0
        for (y in yStart..yEnd) {
            if (y == currentY && skip) continue
            activeCount += if (currentArray[y] == active) 1 else 0
        }
        return activeCount
    }

    private fun getDefaultChar() = inactive

    private fun getStartIndex(i: Int): Int {
        return when (i) {
            0 -> 0
            else -> -1
        }
    }

    private fun Int.getEndIndex(i: Int): Int {
        return when (i) {
            this - 1 -> 0
            else -> 1
        }
    }

    fun MutableList<MutableList<MutableList<CharArray>>>.update4D(): MutableList<MutableList<MutableList<CharArray>>> {
        val chararray4d = createDefault4dArray(this.size, this[0].size, this[0][0].size, this[0][0][0].size)

        for (w in this.indices) {
            val chararray3d = this[w]
            for (z in chararray3d.indices) {
                val chararray2d = chararray3d[z]
                for (x in chararray2d.indices) {
                    val chararray = chararray2d[x]
                    for (y in chararray.indices) {
                        chararray4d[w][z][x][y] = getState(this[w][z][x][y], x, y, z, w, this)
                    }
                }
            }
        }
        return chararray4d
    }

    private fun getState(current:Char, currentX: Int, currentY: Int, currentZ: Int, currentW: Int, list: MutableList<MutableList<MutableList<CharArray>>>): Char {
        val activeCount = countActiveAdjacent4d(currentX, currentY, currentZ, currentW, list, true)
        return when (current) {
            active -> if (activeCount !in 2..3) inactive else active
            inactive -> if (activeCount == 3) active else inactive
            else -> current
        }
    }

    override fun executeGoal_1() {
        repeat(6) {
            cubeArray3d = cubeArray3d.extend3dCharArray()
            cubeArray3d = mutableListOf(cubeArray3d).update4D()[0]
        }
        val actives = cubeArray3d.sumBy { it.sumBy { row -> row.sumBy { c -> if (c == active) 1 else 0 } } }
        println(actives)
    }

    override fun executeGoal_2() {
        repeat(6) {
            cubeArray4d = cubeArray4d.extend4dCharArray()
            cubeArray4d = cubeArray4d.update4D()
        }
        val actives = cubeArray4d.sumBy { it.sumBy { thirdDim -> thirdDim.sumBy { row -> row.sumBy { c -> if (c == active) 1 else 0 } } } }
        println(actives)
    }


}