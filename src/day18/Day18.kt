package day18

import InputReader

class Day18 {

    private val test = false
    private val input = InputReader.readInput(18, test = test)
    private val size = if (test) 7 else 71
    private val bytesToFall = if (test) 12 else 1024

    data class Coordinate(val x: Int, val y: Int) {
        operator fun plus(nextDirection: Direction): Coordinate {
            return Coordinate(x + nextDirection.x, y + nextDirection.y)
        }
    }

    enum class Direction(val x: Int, val y: Int) {
        NORTH(0, -1),
        SOUTH(0, 1),
        WEST(-1, 0),
        EAST(1, 0)
    }

    data class State(val x: Int, val y: Int, val cost: Int, val history: Set<Pair<Int, Int>> = setOf()): Comparable<State> {
        override fun compareTo(other: State): Int = if (this.cost > other.cost) 1 else if (this.cost < other.cost) -1 else 0

        fun move(newDirection: Direction): State {
            return State(x + newDirection.x, y + newDirection.y, cost + 1, history + (x to y))
        }
    }

    fun solve1() {
        val inputPairs = input.take(bytesToFall)
            .map { it.split(",") }
            .map { Coordinate(it[0].trim().toInt(), it[1].trim().toInt()) }
        val winner = findPath(inputPairs)
        println(winner?.cost ?: "No path was found")
    }

    fun solve2() {
        val inputPairs = input.map { it.split(",") }
            .map { Coordinate(it[0].trim().toInt(), it[1].trim().toInt()) }

        var currentMin = 0
        var currentMax = inputPairs.size
        while (currentMin != currentMax) {
            val currentNext = (currentMax + currentMin) / 2
            val winner = findPath(inputPairs.take(currentNext))
            if (winner != null) {
                currentMin = currentNext + 1
            } else {
                currentMax = currentNext
            }
        }
        val blocker = currentMin - 1
        println("Blocking coordinate: ${inputPairs[blocker]} at $blocker")
    }

    private fun findPath(inputPairs: List<Coordinate>): State? {
        val grid = Array(size) { Array(size) { "." } }
        inputPairs.forEach { grid[it.y][it.x] = "#" }

        val queue = mutableListOf<State>()
        queue.add(State(0, 0, 0))
        var winner: State? = null
        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()

            if (current.x < 0 || current.x >= size || current.y < 0 || current.y >= size) {
                continue
            }
            if (grid[current.y][current.x] == "#" || grid[current.y][current.x] == "O") {
                continue
            }
            if (current.x == size - 1 && current.y == size - 1) {
                winner = current
                break
            }
            grid[current.y][current.x] = "O"
            queue.add(current.move(Direction.NORTH))
            queue.add(current.move(Direction.SOUTH))
            queue.add(current.move(Direction.WEST))
            queue.add(current.move(Direction.EAST))
        }
        return winner
    }
}