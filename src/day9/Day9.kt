package day9

import InputReader

class Day9 {

    private val input = InputReader.readInput(9, test = false, split = false)[0]

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

        val additionalEmptySpaces = mutableSetOf<Int>()

        while (startPointer <= numbers.size - 1) {
            if (startPointer % 2 == 0 && !additionalEmptySpaces.contains(startPointer)) {
                checksum += (startPointer / 2) * computedPosition
                numbers[startPointer]--
                computedPosition++
            } else {
                if (endPointer == 0) {
                    computedPosition += numbers[startPointer]
                    startPointer++
                    continue
                }
                val freeSpaceSize = numbers[startPointer]
                var tempEndPointer = endPointer

                while (
                    tempEndPointer > startPointer &&
                    (
                            numbers[tempEndPointer] > freeSpaceSize ||
                            numbers[tempEndPointer] == 0 ||
                            additionalEmptySpaces.contains(tempEndPointer)
                    )
                ) {
                    tempEndPointer -= 2
                }

                if (tempEndPointer <= startPointer) {
                    computedPosition += freeSpaceSize
                    numbers[startPointer] = 0
                    startPointer++
                } else {
                    var movedFileSize = numbers[tempEndPointer]
                    while (movedFileSize > 0) {
                        checksum += (tempEndPointer / 2) * computedPosition
                        computedPosition++
                        numbers[startPointer]--
                        movedFileSize--
                    }
                    additionalEmptySpaces.add(tempEndPointer)
                }
            }
            while (startPointer < numbers.size && numbers[startPointer] == 0) {
                startPointer++
            }
            while (numbers[endPointer] == 0 && endPointer > 1) {
                endPointer -= 2
            }
        }

        println(checksum)
    }

}
