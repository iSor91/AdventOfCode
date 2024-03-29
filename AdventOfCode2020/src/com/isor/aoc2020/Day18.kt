package com.isor.aoc2020

import com.isor.aoc.common.AOC_Runner
import com.isor.aoc.common.Year
import java.util.function.BiFunction

fun main() {
    Day18().executeGoals()
}

//@TestResources
@Year(2020)
class Day18 : AOC_Runner() {

    val elementRegex = Regex("\\d+||\\+|\\*")

    override fun executeGoal_1() {
        val asts = allLines.map { buildAst(it) }
        asts.forEach { println(it.eval()) }
        var sum = 0L
        for (ast in asts) {
            sum += ast.eval()
        }
        println(sum)
    }

    override fun executeGoal_2() {
        println()
        val asts = allLines.map { buildAst(it, "+") }
//        asts.forEach { println(it.eval()) }
        var sum = 0L
//        val fw: FileWriter = FileWriter("C:\\Projects\\AdventOfCode\\myoutput.txt")
        for (ast in asts) {
            val eval = ast.eval()
//            fw.write("${eval.toString()} \n")
            sum += eval
        }
//        fw.close()
        println(sum)
        //171259538712010 -should be
    }

    private fun buildAst(it: String, vararg precedence: String) : Ast {
        val root = Ast(null)
        var currentAst =root
        var bracketAst =root
        var astElement = currentAst.addElement("+")
        val elementStrings = it.split(" ")
        for (element in elementStrings) {
            var currentElement = element
            var stepup = 0
            while (!elementRegex.matches(currentElement)) {
                if (currentElement.startsWith("(")) {
                    currentElement = currentElement.substring(1)
                    astElement = currentAst.addElement("?")
                    currentAst = astElement.addSubAst()
                    bracketAst = currentAst
                } else {
                    currentElement = currentElement.substring(0, currentElement.length - 1)
                    stepup++
                }
            }
            if(precedence.contains(currentElement)) {
                if(!currentAst.precedenceAst) { //with this any number of precedence operator with same precendence can be used (if the precendence boolean is changed to precendence level, multiple precendence level could be handled
                    val tmp = astElement.element
                    astElement.element = ""
                    currentAst = astElement.addSubAst()
                    currentAst.setPrecedence()
                    astElement.subAst!!.addElement(tmp)
                }
            } else if(!AstElement(null, currentElement).isNumeric() ) {
                currentAst = bracketAst
            }
            astElement = currentAst.addElement(currentElement)
            repeat(stepup) {
                astElement = bracketAst.getNonPrecedenceParent()
                bracketAst = astElement.parent!!
                currentAst = bracketAst
            }
        }
        return root
    }

    class Ast (val parent: AstElement?){

        var precedenceAst: Boolean = false

        val elements: MutableList<AstElement> = mutableListOf()

        val functionMap = mapOf<String, BiFunction<Long, Long, Long>>("+" to BiFunction{a,b -> a + b}, "*" to BiFunction{a,b -> a * b})

        fun addElement(value: String): AstElement {
            val element = AstElement(this, value)
            elements.add(element)
            return element
        }

        fun setPrecedence() {
            this.precedenceAst = true
        }

        fun getNonPrecedenceParent() : AstElement {
            var parentAst = this.parent!!
            while(parentAst.parent!!.precedenceAst) {
                parentAst = parentAst.parent!!.parent!!
            }
            return parentAst
        }

        override fun toString(): String {
            val joinToString = this.elements.map { it.toString() }.joinToString { " " }
            return joinToString
        }

        fun eval() : Long {
            var sum = 0L
            var currentOperation = functionMap["+"]!!
            for (element in this.elements) {
                if(element.isNumeric()) {
                    sum = currentOperation.apply(sum,element.element.toLong())
                } else {
                    val biFunction = functionMap[element.element]
                    if(biFunction != null)
                        currentOperation = biFunction
                }
                if( element.subAst != null ){
                    sum = currentOperation.apply(sum,element.subAst!!.eval())
                }
            }
            return sum
        }
    }

    class AstElement(val parent: Ast?, var element: String) {
        var subAst: Ast? = null

        fun isNumeric(): Boolean {
            return element.matches(Regex("\\d+"))
        }

        fun setAst(ast : Ast) {
            this.subAst = ast
        }

        fun addSubAst(): Ast {
            if (this.subAst == null)
                this.subAst = Ast(this)
            return this.subAst!!
        }
    }
}