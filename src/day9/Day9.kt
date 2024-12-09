package day9

import InputReader

class Day9 {

    private val input = InputReader.readInput(9, test = true, split = false)[0]

    fun solve1() {
        val numbers = input.split("").drop(1).dropLast(1).map { it.toInt() }.toMutableList()
        var checksum = 0L
        var startPointer = 0
        var endPointer = numbers.size - 1

        var computedPosition = 0L

        while (startPointer <= endPointer) {
            if (startPointer % 2 == 0) {
                checksum += (startPointer / 2) * computedPosition
                numbers[startPointer]--
            } else {
                checksum += (endPointer / 2) * computedPosition
                numbers[startPointer]--
                numbers[endPointer]--
            }
            while (numbers[startPointer] == 0 && startPointer < numbers.size - 1) {
                startPointer++
            }
            while (numbers[endPointer] == 0 && endPointer > 1) {
                endPointer -= 2
            }
            computedPosition++
        }

        println(checksum)
    }

    fun solve2() {
        val numbers = input.split("").drop(1).dropLast(1).map { it.toInt() }.toMutableList()
        var checksum = 0L
        var startPointer = 0
        var endPointer = numbers.size - 1

        var computedPosition = 0L

        while (startPointer <= endPointer) {
            val numbersDebug = numbers.joinToString("")

            if (startPointer % 2 == 0) {
                checksum += (startPointer / 2) * computedPosition
                numbers[startPointer]--
            } else {
                val freeSpaceSize = numbers[startPointer]
                var tempEndPointer = endPointer

                while (numbers[tempEndPointer] > freeSpaceSize || numbers[tempEndPointer] == 0) {
                    tempEndPointer -= 2
                    if (tempEndPointer < 0) {
                        println("what")
                    }
                }

                while (numbers[tempEndPointer] > 0) {
                    checksum += (tempEndPointer / 2) * computedPosition
                    computedPosition++
                    numbers[startPointer]--
                    numbers[tempEndPointer]--
                }
            }
            while (numbers[startPointer] == 0 && startPointer < numbers.size - 1) {
                startPointer++
            }
            while (numbers[endPointer] == 0 && endPointer > 1) {
                endPointer -= 2
            }
            computedPosition++
        }

        println(checksum)
    }

}
