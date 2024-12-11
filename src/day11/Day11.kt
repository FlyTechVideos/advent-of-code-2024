package day11

import InputReader

class Day11 {

    private val input = InputReader.readInput(11, test = false)[0]

    fun solve1() {
        println(calculateStones(25))
    }

    private fun calculateStones(blinks: Int): Long {
        val stones = input.split(" ").map { it.toLong() }

        var stoneFrequencies = stones.associateWith { 1L }
        for (i in 0..<blinks) {
            val newFrequencies = mutableMapOf<Long, Long>()
            stoneFrequencies.forEach { (stone, frequency) ->
                if (stone == 0L) {
                    val existing = newFrequencies[1] ?: 0
                    newFrequencies[1] = existing + frequency
                } else {
                    val stringStone = stone.toString()
                    if (stringStone.length % 2 == 0) {
                        val half = stringStone.length / 2
                        val firstHalf = stringStone.substring(0, half).toLong()
                        val secondHalf = stringStone.substring(half).toLong()

                        if (firstHalf == secondHalf) {
                            val existing = newFrequencies[firstHalf] ?: 0
                            newFrequencies[firstHalf] = existing + frequency * 2
                        } else {
                            val existingFirstHalf = newFrequencies[firstHalf] ?: 0
                            val existingSecondHalf = newFrequencies[secondHalf] ?: 0

                            newFrequencies[firstHalf] = existingFirstHalf + frequency
                            newFrequencies[secondHalf] = existingSecondHalf + frequency
                        }
                    } else {
                        val existing = newFrequencies[stone * 2024] ?: 0
                        newFrequencies[stone * 2024] = existing + frequency
                    }
                }
            }
            stoneFrequencies = newFrequencies
        }
        return stoneFrequencies.entries.sumOf {
            it.value
        }
    }

    fun solve2() {
        println(calculateStones(75))
    }
}