import com.isor.aoc.common.AOC_Utility

fun main() {
//    AOC_Test().test1()
    AOC_Test().regexTest()
}

class AOC_Test : AOC_Utility() {


    fun regexTest() {

        val test = Regex("a((aa|bb)(ab|ba)|(ab|ba)(aa|bb))b")

        println(test.matches("ababbb"))
        println(test.matches("bababa"))
        println(test.matches("abbbab"))
        println(test.matches("aaabbb"))
        println(test.matches("aaaabbb"))

    }

    fun test1() {

//        println(0.boundedPlus(-90, 0, 359))
//        println(270.boundedPlus(100, 0, 359))


        println(gcd(listOf(42, 56, 21)))
        println(lcm(listOf(3,7,2)))
    }
}