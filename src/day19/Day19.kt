package day19

import InputReader

class Day19 {

    private val input = InputReader.readInput(19, test = false, split = false)[0]
    private val availableTowels = input.split(System.lineSeparator().repeat(2))[0].split(", ")
    private val targetPatterns = input.split(System.lineSeparator().repeat(2))[1].split("\n").map { it.trim() }
    private val patternCache = mutableMapOf<String, Long>()

    fun solve1() {
        availableTowels.sortedByDescending { it.length }
        val possiblePatterns = targetPatterns.map { isPossibleToConstruct(it) }.count { it }
        println(possiblePatterns)
    }

    private fun isPossibleToConstruct(target: String): Boolean {
        if (target.isEmpty()) {
            return true
        }
        for (availablePattern in availableTowels) {
            if (target.startsWith(availablePattern)) {
                val remaining = target.substring(availablePattern.length)
                if (isPossibleToConstruct(remaining)) {
                    return true
                }
            }
        }
        return false
    }

    fun solve2() {
        println(targetPatterns.sumOf { findAllPossiblePatterns(it, availableTowels) })
    }

    private fun findAllPossiblePatterns(target: String, availableTowels: List<String>): Long {
        if (target.isEmpty()) {
            return 0
        }
        if (patternCache.containsKey(target)) {
            return patternCache[target]!!
        }
        var possiblePatterns = 0L
        val currentlyAvailableTowels = availableTowels.filter { target.contains(it) }
        for (availableTowel in currentlyAvailableTowels) {
            if (target == availableTowel) {
                possiblePatterns++
            } else if (target.startsWith(availableTowel)) {
                val remaining = target.substring(availableTowel.length)
                possiblePatterns += findAllPossiblePatterns(remaining, currentlyAvailableTowels)
            }
        }
        patternCache[target] = possiblePatterns
        return possiblePatterns
    }

}