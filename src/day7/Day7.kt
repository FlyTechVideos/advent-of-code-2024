package day7

import InputReader
import kotlin.math.pow

class Day7 {

    private val input = InputReader.readInput(7, test = false)

    class Equation(input: String) {
        val result: Long
        private val operands: List<Long>

        init {
            val parts = input.split(": ")
            result = parts[0].toLong()
            operands = parts[1].split(" ").map(String::toLong)
        }

        fun isSolveable(): Boolean {
            val amountOfOperations = 2.0.pow(operands.size.toDouble()).toInt()

            for (operatorMask in 0..<amountOfOperations) {
                val localResult = operands.reduceIndexed { index, acc, l ->
                    if (operatorMask and (1 shl index) != 0) {
                        acc * l
                    } else {
                        acc + l
                    }
                }
                if (localResult == result) {
                    return true
                }
            }

            return false
        }

        fun isSolveablePart2(): Boolean {
            val amountOfOperations = 3.0.pow(operands.size.toDouble()).toInt()

            for (operatorMask in 0..<amountOfOperations) {
                val operators = operatorMask.toString(3).padStart(operands.size, '0')

                val localResult = operands.reduceIndexed { index, acc, l ->
                    when (operators[index]) {
                        '0' -> acc + l
                        '1' -> acc * l
                        '2' -> (acc.toString() + l.toString()).toLong()
                        else -> throw IllegalArgumentException("Invalid operator")
                    }
                }
                if (localResult == result) {
                    return true
                }
            }

            return false
        }
    }

    fun solve1() {
        input.map { Equation(it) }
            .filter(Equation::isSolveable)
            .sumOf { it.result }
            .let(::println)
    }

    fun solve2() {
        input.map { Equation(it) }
            .filter(Equation::isSolveablePart2)
            .sumOf { it.result }
            .let(::println)
    }

}
