package day3

import InputReader

class Day3 {

    private val input = InputReader.readInput(3, test = false, split = false)[0]

    fun solve1() {
        "mul\\(\\d{1,3},\\d{1,3}\\)".toRegex()
            .findAll(input)
            .map {
                it.value
                    .replace("mul(", "")
                    .replace(")", "")
                    .split(",")
                    .map(String::toInt)
                    .reduce(Int::times)
            }
            .sum()
            .let(::println)
    }

    fun solve2() {
        val instructionMatches = "do(n't)?\\(\\)".toRegex().findAll(input).iterator()

        val doRanges = mutableListOf<IntRange>()

        var currentlyDoRange = true
        var lastIndex = 0
        while (instructionMatches.hasNext()) {
            val match = instructionMatches.next()

            if (currentlyDoRange && !match.value.contains("n't")) {
                continue
            }
            if (!currentlyDoRange && match.value.contains("n't")) {
                continue
            }

            if (currentlyDoRange) {
                doRanges.add(lastIndex..match.range.first)
                currentlyDoRange = false
            } else {
                lastIndex = match.range.first
                currentlyDoRange = true
            }
        }

        "mul\\(\\d{1,3},\\d{1,3}\\)".toRegex()
            .findAll(input)
            .filter { match ->
                doRanges.any { range -> match.range.first in range }
            }
            .map {
                it.value
                    .replace("mul(", "")
                    .replace(")", "")
                    .split(",")
                    .map(String::toInt)
                    .reduce(Int::times)
            }
            .sum()
            .let(::println)
    }
}
