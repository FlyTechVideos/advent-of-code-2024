package day5

import InputReader

class Day5 {

    private val input = InputReader.readInput(5, test = false, split = false)[0]

    fun solve1() {
        val parts = input.split("\n\n")
        val rulePairs = parts[0].trim().split("\n")
            .map { it.split("|") }
            .map { Pair(it[0].toInt(), it[1].toInt()) }
            .toSet()
        val updates = parts[1].trim().split("\n")
            .asSequence()
            .map { it.split(",") }
            .map { it.map { n -> n.toInt() } }
            .filter { isValid(it, rulePairs) }
            .map { it[it.size / 2] }
            .sum()
        println(updates)
    }

    private fun isValid(update: List<Int>, rulePairs: Set<Pair<Int, Int>>): Boolean {
        (0..<update.size-1).forEach { i ->
            if (rulePairs.contains(Pair(update[i+1], update[i]))) {
                return false
            }
        }
        return true
    }

    fun solve2() {
        val parts = input.split("\n\n")
        val rulePairs = parts[0].trim().split("\n")
            .map { it.split("|") }
            .map { Pair(it[0].toInt(), it[1].toInt()) }
            .toSet()
        val updates = parts[1].trim().split("\n")
            .asSequence()
            .map { it.split(",") }
            .map { it.map { n -> n.toInt() } }
            .filter { !isValid(it, rulePairs) }
            .map { reorder(it, rulePairs) }
            .map { it[it.size / 2] }
            .sum()
        println(updates)
    }

    private fun reorder(update: List<Int>, rulePairs: Set<Pair<Int, Int>>): List<Int> {
        val updatedUpdate = update.toMutableList()
        var hadMovements = false
        (0..<updatedUpdate.size-1).forEach { i ->
            val pair = Pair(updatedUpdate[i+1], updatedUpdate[i])
            if (rulePairs.contains(pair)) {
                updatedUpdate[i] = pair.first
                updatedUpdate[i+1] = pair.second
                hadMovements = true
            }
        }
        if (hadMovements) {
            return reorder(updatedUpdate, rulePairs)
        }
        return updatedUpdate
    }

}
