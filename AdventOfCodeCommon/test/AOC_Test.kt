import com.isor.aoc.common.AOC_Utility

fun main() {
    AOC_Test().test1()
}

class AOC_Test : AOC_Utility() {


    fun test1() {

//        println(0.boundedPlus(-90, 0, 359))
//        println(270.boundedPlus(100, 0, 359))


        println(gcd(listOf(42, 56, 21)))
        println(lcm(listOf(3,7,2)))
    }
}