package day20

import InputReader
import kotlin.math.abs

class Day20 {

    private val input = InputReader.readInput(20, test = false)
    private val map = input.map { it.split("").drop(1).dropLast(1) }
    private val sizeY = map.size
    private val sizeX = map[0].size

    private val sY = map.indexOfFirst { it.contains("S") }
    private val sX = map[sY].indexOf("S")
    private val eY = map.indexOfFirst { it.contains("E") }
    private val eX = map[eY].indexOf("E")
    private val start = Coordinate(sX, sY)
    private val end = Coordinate(eX, eY)

    enum class Direction(val x: Int, val y: Int) {
        NORTH(0, -1),
        SOUTH(0, 1),
        WEST(-1, 0),
        EAST(1, 0)
    }

    data class State(val x: Int, val y: Int, val distance: Int = 0) {
        fun move(newDirection: Direction): State {
            return State(x + newDirection.x, y + newDirection.y, distance + 1)
        }

        val position = Coordinate(x, y)
    }

    data class Coordinate(val x: Int, val y: Int) {
        operator fun plus(other: Coordinate): Coordinate {
            return Coordinate(x + other.x, y + other.y)
        }

        val distance = abs(x) + abs(y)
    }

    fun solve1() {
        val distancesToTarget = findShortestPathWellIMeanActuallyTheOnlyPathSinceThereIsOnlyOne()
        val removableWalls = map.flatMapIndexed {
            y, row -> row.mapIndexedNotNull { x, cell ->
                if (cell == "#" && y > 0 && y < sizeY - 1 && x > 0 && x < sizeX - 1) Coordinate(x, y) else null
            }
        }
        val shortcuts = mutableMapOf<Int, Int>()
        removableWalls.flatMap {
            val adjacents = listOf(
                Coordinate(it.x, it.y - 1),
                Coordinate(it.x, it.y + 1),
                Coordinate(it.x - 1, it.y),
                Coordinate(it.x + 1, it.y),
            ).filter { map[it.y][it.x] == "." || it == start || it == end }

            if (adjacents.size < 2) {
                listOf()
            } else if (adjacents.size == 2) {
                listOf(Pair(adjacents[0], adjacents[1]))
            } else if (adjacents.size == 3) {
                listOf(
                    Pair(adjacents[0], adjacents[1]),
                    Pair(adjacents[1], adjacents[2]),
                    Pair(adjacents[0], adjacents[2]),
                )
            } else {
                listOf(
                    Pair(adjacents[0], adjacents[1]),
                    Pair(adjacents[0], adjacents[2]),
                    Pair(adjacents[0], adjacents[3]),
                    Pair(adjacents[1], adjacents[2]),
                    Pair(adjacents[1], adjacents[3]),
                    Pair(adjacents[2], adjacents[3]),
                )
            }
        }.forEach {
            val diff = abs(distancesToTarget[it.first]!! - distancesToTarget[it.second]!!) - 2
            if (diff > 0) {
                shortcuts[diff] = (shortcuts[diff] ?: 0) + 1
            }
        }
        println(
            shortcuts.entries
                .filter { it.key >= 100 }
                .sumOf { it.value }
        )
    }

    private fun findShortestPathWellIMeanActuallyTheOnlyPathSinceThereIsOnlyOne(): Map<Coordinate, Int> {
        val ms = System.currentTimeMillis()
        val path = mutableListOf(State(sX, sY))

        val mapCopy = map.map { it.toMutableList() }
        while (path.isNotEmpty()) {
            val current = path.last()

            val next = listOf(
                current.move(Direction.NORTH),
                current.move(Direction.SOUTH),
                current.move(Direction.EAST),
                current.move(Direction.WEST)
            ).first {
                mapCopy[it.y][it.x] == "." || it.position == end
            }
            mapCopy[current.y][current.x] = "o"
            path.add(next)

            if (next.position == end) {
                println("Time: ${System.currentTimeMillis() - ms}")
                return mappify(path)
            }
        }
        throw IllegalStateException("Did not find shortest path.")
    }

    private fun mappify(path: MutableList<State>): Map<Coordinate, Int> {
        return path.mapIndexed { index, state -> state.position to index }.toMap()
    }

    fun solve2() {
        val distancesFromStart = findShortestPathWellIMeanActuallyTheOnlyPathSinceThereIsOnlyOne()
        val shortcuts = mutableMapOf<Int, Int>()
        val potentials = (0..20).flatMap { x ->
            (0.. 20 - x).flatMap { y ->
                listOf(
                    Coordinate(x, y),
                    Coordinate(-x, y),
                    Coordinate(x, -y),
                    Coordinate(-x, -y),
                )
            }
        }.toSet()

        for (distance in distancesFromStart) {
            val currentPosition = distance.key
            val currentDistanceFromStart = distance.value

            for (potentialMovement in potentials) {
                val newPosition = currentPosition + potentialMovement
                if (!map.indices.contains(newPosition.y) ||
                    !map[newPosition.y].indices.contains(newPosition.x)) {
                    continue
                }
                if (!listOf(".", "E").contains(map[newPosition.y][newPosition.x])) {
                    continue
                }

                val distanceToEnd = distancesFromStart[end]!! - distancesFromStart[newPosition]!!
                val totalDistanceSpent = currentDistanceFromStart + potentialMovement.distance + distanceToEnd
                val savedDistance = distancesFromStart[end]!! - totalDistanceSpent
                if (savedDistance > 0) {
                    shortcuts[savedDistance] = (shortcuts[savedDistance] ?: 0) + 1
                }
            }
        }
        println(
            shortcuts.entries
                .filter { it.key >= 100 }
                .sumOf { it.value }
        )
    }

}
