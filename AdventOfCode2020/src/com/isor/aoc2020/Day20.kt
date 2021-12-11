import com.isor.aoc.common.AOC_Runner
import com.isor.aoc.common.TestResources
import com.isor.aoc.common.Year
import com.isor.aoc.common.getColumn

fun main() {
    Day20().executeGoals()
}

@TestResources
@Year(2020)
class Day20: AOC_Runner() {

    val tiles = mutableListOf<Tile>()
    
    init {
        var tile = mutableListOf<List<Char>>()
        var id = 0L
        for (l in allLines) {
            if(l.startsWith("Tile")) {
                tile = mutableListOf<List<Char>>()
                id = l.substring("Tile ".length, l.length-1).toLong()
                continue
            }
            if(l.isEmpty()) {
                tiles.add(Tile(id, tile.toList()))
                continue
            }
            tile.add(l.toCharArray().toList())
        }
        tiles.add(Tile(id, tile.toList()))
    }
    
    class Tile (val id: Long, var tileArray : List<List<Char>>) {
        val adjacentTiles = LongArray(4){-1L} //0 up 1 right 2 down 3 left

        var checked = false

        fun matches(other: Tile): Boolean {
            if(this == other) {
                return false
            }
            val otherTileArray = other.tileArray
            val otherSides = mutableListOf<String>()
            val firstRow = otherTileArray[0].joinToString("")
            otherSides.add(firstRow)
            otherSides.add(firstRow.reversed())
            val lastCol = otherTileArray.getColumn(otherTileArray.size-1)
            otherSides.add(lastCol)
            otherSides.add(lastCol.reversed())
            val lastRow = otherTileArray.last().joinToString("")
            otherSides.add(lastRow)
            otherSides.add(lastRow.reversed())
            val firstCol = otherTileArray.getColumn(0)
            otherSides.add(firstCol)
            otherSides.add(firstCol.reversed())

            val thisFirstRow = this.tileArray[0].joinToString("")

            if(otherSides.any{ it == thisFirstRow} && this.adjacentTiles[0] == -1L) {
                this.adjacentTiles[0] = other.id
                other.adjacentTiles[2] = this.id
                //Depending on which side matches, the other shall be flipped and rotated
                modifyToMatchFirstRow(otherSides, thisFirstRow, other)
                return true
            }
            val thisLastRow = this.tileArray.last().joinToString("")
            if(otherSides.any{ it == thisLastRow} && this.adjacentTiles[2] == -1L) {
                this.adjacentTiles[2] = other.id
                other.adjacentTiles[0] = this.id
                modifyToMatchLastRow(otherSides, thisLastRow, other)
                return true
            }
            val thisFirstCol = this.tileArray.getColumn(0)
            if(otherSides.any{ it == thisFirstCol} && this.adjacentTiles[1] == -1L) {
                this.adjacentTiles[1] = other.id
                other.adjacentTiles[3] = this.id
                modifyToMatchFirstCol(otherSides, thisFirstCol, other)
                return true
            }
            val thisLastCol = this.tileArray.getColumn(tileArray.size-1)
            if(otherSides.any{ it == thisLastCol} && this.adjacentTiles[3] == -1L) {
                this.adjacentTiles[3] = other.id
                other.adjacentTiles[1] = this.id
                modifyToMatchLastCol(otherSides, thisLastCol, other)
                return true
            }
            return false
        }

        private fun modifyToMatchLastCol(otherSides: MutableList<String>, thisLastCol: String, other: Tile) {
            when(otherSides.indexOf(thisLastCol)) {
                0 -> repeat(3){other.rotate90()}
                1 -> {
                    repeat(3){other.rotate90()}
                    other.flipHorizontally()
                }
                2 -> other.flipVertically()
                3 -> {
                    other.flipVertically()
                    other.flipHorizontally()
                }
                4 -> other.rotate90()
                5 -> {
                    other.rotate90()
                    other.flipHorizontally()
                }
                7 -> {
                    other.flipHorizontally()
                }
            }
        }

        private fun modifyToMatchFirstCol(otherSides: MutableList<String>, thisFirstCol: String, other: Day20.Tile) {
            when(otherSides.indexOf(thisFirstCol)) {
                0 -> other.rotate90()
                1 -> {
                    other.rotate90()
                    other.flipHorizontally()
                }
                3 -> other.flipHorizontally()
                4 -> {
                    repeat(3) {
                        other.rotate90()
                    }
                    other.flipHorizontally()
                }
                5 -> {
                    repeat(3) {
                        other.rotate90()
                    }
                }
                6 -> other.flipVertically()
                7 -> {
                    other.flipHorizontally()
                    other.flipVertically()
                }
            }
        }

        private fun modifyToMatchLastRow(otherSides: MutableList<String>, thisLastRow: String, other: Day20.Tile) {
            when (otherSides.indexOf(thisLastRow)) {
                1 -> other.flipVertically()
                2 -> {
                    repeat(3) {
                        other.rotate90()
                    }
                }
                3 ->  {
                    repeat(3){ other.rotate90()}
                    other.flipVertically()
                }
                4 -> other.flipHorizontally()
                5 -> {
                    other.flipHorizontally()
                    other.flipVertically()
                }
                6 -> {
                    other.rotate90()
                    other.flipVertically()
                }
                7 -> {
                    other.rotate90()
                }
            }
        }

        private fun modifyToMatchFirstRow(otherSides: MutableList<String>, thisFirstRow: String, other: Tile) {
            when (otherSides.indexOf(thisFirstRow)) {
                0 -> other.flipHorizontally()
                1 -> {
                    other.flipVertically()
                    other.flipHorizontally()
                }
                2 -> {
                    other.rotate90()
                    other.flipVertically()
                }
                3 -> other.rotate90()

                5 -> other.flipVertically()
                6 -> repeat(3) { other.rotate90() }
                7 -> {
                    repeat(3) { other.rotate90() }
                    other.flipVertically()
                }
            }
        }

        fun flipVertically() {
            this.tileArray = this.tileArray.map { it.joinToString ("").reversed().toCharArray().toList() }
        }

        fun flipHorizontally() {
            val newArray = mutableListOf<List<Char>>()
            this.tileArray.forEach { newArray.add(0, it) }
            this.tileArray = newArray
        }

        fun rotate90() {
            val newArray = mutableListOf<List<Char>>()
            this.tileArray.indices.forEach { newArray.add(this.tileArray.getColumn(it).toCharArray().toList()) }
        }

        override fun equals(other: Any?): Boolean {
            return other is Tile && other.id == this.id
        }

        override fun hashCode(): Int {
            return id.toInt()
        }
    }

    fun findSides(starttile : Tile) {
        repeat(4) {tiles.any { starttile.matches(it) }}
        starttile.checked = true
        starttile.adjacentTiles.forEach {
            if(it != -1L) {
                val nextTile = tiles.filter { t -> t.id == it }.first()
                if(!nextTile.checked) {
                    findSides(nextTile)
                }
            }
        }
    }

    override fun executeGoal_1() {
        findSides(tiles[0])
        val filter = tiles.filter { tile -> tile.adjacentTiles.count { it == -1L } == 2 }
        println(filter.map { it.id }.reduce{a,i -> a * i})
    }

    override fun executeGoal_2() {
    }
}