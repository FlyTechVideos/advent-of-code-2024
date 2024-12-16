package day16

import InputReader
import java.util.*

class Day16 {

    private val input = InputReader.readInput(16, test = false)

    enum class Direction(val x: Int, val y: Int, val symbol: String) {
        NORTH(0, -1, "^"),
        SOUTH(0, 1, "v"),
        WEST(-1, 0, "<"),
        EAST(1, 0, ">");

        fun movingScore(direction: Direction): Int {
            if (this == NORTH || this == SOUTH) {
                if (direction == WEST || direction == EAST) {
                    return 1001
                }
            }
            if (this == WEST || this == EAST) {
                if (direction == NORTH || direction == SOUTH) {
                    return 1001
                }
            }
            return 1
        }
    }

    data class State(val x: Int, val y: Int, val cost: Int, val direction: Direction, val history: Set<Pair<Int, Int>> = setOf()): Comparable<State> {
        override fun compareTo(other: State): Int = if (this.cost > other.cost) 1 else if (this.cost < other.cost) -1 else 0

        fun move(newDirection: Direction): State {
            return State(x + newDirection.x, y + newDirection.y, cost + direction.movingScore(newDirection), newDirection, history + (x to y))
        }
    }

    fun solve1() {
        val map = input.map { it.split("").drop(1).dropLast(1) }

        val startY = map.indexOfFirst { it.contains("S") }
        val startX = map[startY].indexOf("S")

        val initialState = State(startX, startY, 0, Direction.EAST)

        val visited = mutableSetOf<Triple<Int, Int, Direction>>()
        val queue = PriorityQueue<State>()
        queue.add(initialState)
        
        while (queue.isNotEmpty()) {
            val next = queue.poll()

            if (visited.contains(Triple(next.x, next.y, next.direction))) {
                continue
            }

            val nextField = map[next.y][next.x]
            if (nextField == "E") {
                printField(drawField(next, map))
                println(next.cost)
                return
            }
            if (nextField == "#") {
                continue
            }

            visited += Triple(next.x, next.y, next.direction)
            queue.add(next.move(Direction.NORTH))
            queue.add(next.move(Direction.SOUTH))
            queue.add(next.move(Direction.EAST))
            queue.add(next.move(Direction.WEST))
        }
    }

    private fun drawField(next: State, map: List<List<String>>): List<List<String>> {
        val new = map.map { it.toMutableList() }
        new[next.y][next.x] = next.direction.symbol
        next.history.forEach { (x, y) -> new[y][x] = "o" }
        return new
    }

    private fun printField(map: List<List<String>>) {
        println(map.joinToString("\n") { it.joinToString("") })
    }

    fun solve2() {
        println("\n#################################################################################################\n")
        val map = input.map { it.split("").drop(1).dropLast(1) }

        val startY = map.indexOfFirst { it.contains("S") }
        val startX = map[startY].indexOf("S")

        val initialState = State(startX, startY, 0, Direction.EAST)

        val visited = mutableMapOf<Triple<Int, Int, Direction>, Int>()
        val queue = PriorityQueue<State>()
        queue.add(initialState)

        val bestPaths = mutableListOf<State>()
        var bestPathCost = Int.MAX_VALUE
        while (queue.isNotEmpty()) {
            val next = queue.poll()

            if (visited.containsKey(Triple(next.x, next.y, next.direction))) {
                if (visited[Triple(next.x, next.y, next.direction)]!! < next.cost) {
                    continue
                }
            }

            val nextField = map[next.y][next.x]
            if (nextField == "E") {
                if (bestPathCost == Int.MAX_VALUE || next.cost == bestPathCost) {
                    bestPathCost = next.cost
                    bestPaths.add(next)
                }
                continue
            }
            if (nextField == "#") {
                continue
            }

            visited += Triple(next.x, next.y, next.direction) to next.cost
            queue.add(next.move(Direction.NORTH))
            queue.add(next.move(Direction.SOUTH))
            queue.add(next.move(Direction.EAST))
            queue.add(next.move(Direction.WEST))
        }
        var renderedMap = map
        bestPaths.forEach {
            renderedMap = drawField(it, renderedMap)
        }
        printField(renderedMap)
        println(renderedMap.sumOf { it.count { it == "o" } } + 1)
    }

}
