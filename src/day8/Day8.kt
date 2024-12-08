package day8

import InputReader
import kotlin.math.abs
import kotlin.math.sign

class Day8 {

    private val input = InputReader.readInput(8, test = false)

    private val OPEN = "."

    data class Position(val x: Int, val y: Int) {
        operator fun plus(rightHandSide: Position): Position {
            return Position(this.x + rightHandSide.x, this.y + rightHandSide.y)
        }
        operator fun minus(rightHandSide: Position): Position {
            return Position(this.x - rightHandSide.x, this.y - rightHandSide.y)
        }

        fun isWithinBounds(grid: List<List<String>>): Boolean {
            return y >= 0 && y < grid.size && x >= 0 && x < grid[y].size
        }
    }

    fun solve1() {
        val grid = input.toMutableList().map { it.split("").drop(1).dropLast(1).toMutableList() }
        val frequencyMap = mutableMapOf<String, MutableSet<Position>>()
        grid.forEachIndexed { y, row ->
            row.forEachIndexed { x, element ->
                if (element != OPEN) {
                    frequencyMap.computeIfAbsent(element) { mutableSetOf() }.add(Position(x, y))
                }
            }
        }
        val antinodeSet = mutableSetOf<Position>()
        frequencyMap.values.forEach { emitterList ->
            val stationPairs = emitterList.flatMap { emitter1 ->
                emitterList.map { emitter2 ->
                    Pair(emitter1, emitter2)
                }
            }.filter { pair ->
                pair.first != pair.second
            }
            stationPairs.forEach { pair ->
                val vector = pair.second - pair.first
                val node1 = pair.second + vector
                val node2 = pair.first - vector

                if (node1.isWithinBounds(grid)) {
                    antinodeSet.add(node1)
                }
                if (node2.isWithinBounds(grid)) {
                    antinodeSet.add(node2)
                }
            }
        }
        println(antinodeSet.size)
    }

    fun solve2() {
        val grid = input.toMutableList().map { it.split("").drop(1).dropLast(1).toMutableList() }
        val frequencyMap = mutableMapOf<String, MutableSet<Position>>()
        grid.forEachIndexed { y, row ->
            row.forEachIndexed { x, element ->
                if (element != OPEN) {
                    frequencyMap.computeIfAbsent(element) { mutableSetOf() }.add(Position(x, y))
                }
            }
        }
        val antinodeSet = mutableSetOf<Position>()
        frequencyMap.values.forEach { emitterList ->
            val stationPairs = emitterList.flatMap { emitter1 ->
                emitterList.map { emitter2 ->
                    Pair(emitter1, emitter2)
                }
            }.filter { pair ->
                pair.first != pair.second
            }
            stationPairs.forEach { pair ->
                val vector = reduceVector(pair.second - pair.first)
                antinodeSet.add(pair.first)
                antinodeSet.add(pair.second)

                var node1 = pair.second + vector
                while (node1.isWithinBounds(grid)) {
                    antinodeSet.add(node1)
                    node1 += vector
                }

                var node2 = pair.first - vector
                while (node2.isWithinBounds(grid)) {
                    antinodeSet.add(node2)
                    node2 -= vector
                }
            }
        }
        antinodeSet.forEach {
            grid[it.y][it.x] = "#"
        }
        println(antinodeSet.size)
    }

    private fun reduceVector(vector: Position): Position {
        val x = abs(vector.x)
        val y = abs(vector.y)
        val signX = sign(vector.x.toDouble()).toInt()
        val signY = sign(vector.y.toDouble()).toInt()

        if (x == y) {
            return Position(signX, signY)
        } else if (x > y) {
            val gcd = gcd(x, y)
            return Position(signX * x / gcd, signY * y / gcd)
        } else {
            val gcd = gcd(y, x)
            return Position(signX * x / gcd, signY * y / gcd)
        }
    }

    private fun gcd(a: Int, b: Int): Int {
        return if (b == 0) a else gcd(b, a % b)
    }
}
