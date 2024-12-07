package day6

import InputReader

class Day6 {

    private val input = InputReader.readInput(6, test = false)

    private val OPEN = "."
    private val OBSTACLE = "#"
    private val ME = "^"
    private val VISITED = "X"
    private val VISITED_NEW_ONCE = "x"
    private val VISITED_NEW_TWICE = "o"

    enum class Direction(val xDiff: Int, val yDiff: Int) {
        UP(0, -1),
        DOWN(0, 1),
        LEFT(-1, 0),
        RIGHT(1, 0);

        fun rotateRight(): Direction {
            return when (this) {
                UP -> RIGHT
                RIGHT -> DOWN
                DOWN -> LEFT
                LEFT -> UP
            }
        }
    }

    fun solve1() {
        val grid = input.toMutableList().map { it.split("").toMutableList() }
        var y = grid.indexOfFirst { it.contains(ME) }
        var x = grid[y].indexOf(ME)

        var direction = Direction.UP
        while (y < grid.size && x < grid[y].size) {
            grid[y][x] = VISITED
            if (y + direction.yDiff !in grid.indices || x + direction.xDiff !in grid[y].indices) {
                break
            }
            if (grid[y + direction.yDiff][x + direction.xDiff] == OBSTACLE) {
                direction = direction.rotateRight()
                continue
            }
            y += direction.yDiff
            x += direction.xDiff
        }
        println(grid.sumOf { it.count { field -> field == VISITED } })
    }

    fun solve2() {
        val grid = input.toMutableList().map { it.split("").drop(1).dropLast(1).toMutableList() }
        val y = grid.indexOfFirst { it.contains(ME) }
        val x = grid[y].indexOf(ME)

        val newObstacles = findNewObstacles(x, y, grid)
        println(newObstacles.size)
    }

    private fun findNewObstacles(xInput: Int, yInput: Int, grid: List<MutableList<String>>): List<Pair<Int, Int>> {
        var x = xInput
        var y = yInput
        var direction = Direction.UP
        val obstacles = mutableListOf<Pair<Int, Int>>()
        while (y < grid.size && x < grid[y].size) {
            grid[y][x] = VISITED
            if (y + direction.yDiff !in grid.indices || x + direction.xDiff !in grid[y].indices) {
                break
            }
            if (grid[y + direction.yDiff][x + direction.xDiff] == OBSTACLE) {
                direction = direction.rotateRight()
                continue
            }
            if (puttingAnObstacleOnTheNextFieldWouldCauseANeverEndingTimeLoop(x, y, direction, grid)) {
                obstacles.add(Pair(x + direction.xDiff, y + direction.yDiff))
            }
            y += direction.yDiff
            x += direction.xDiff
        }
        return obstacles
    }

    private fun puttingAnObstacleOnTheNextFieldWouldCauseANeverEndingTimeLoop(
        x: Int, y: Int, initialDirection: Direction, grid: List<List<String>>
    ): Boolean {
        val next = grid[y + initialDirection.yDiff][x + initialDirection.xDiff]
        if (next != OPEN) {
            return false
        }

        val gridCopy = grid.map { it.toMutableList() }.toMutableList()
        var direction = initialDirection
        var currentX = x
        var currentY = y

        gridCopy[currentY + direction.yDiff][currentX + direction.xDiff] = OBSTACLE

        var visitedTwiceCount = 0
        while (currentY < gridCopy.size && currentX < gridCopy[currentY].size) {
            if (gridCopy[currentY][currentX] == VISITED_NEW_ONCE) {
                gridCopy[currentY][currentX] = VISITED_NEW_TWICE
            } else if (gridCopy[currentY][currentX] == VISITED_NEW_TWICE) {
                visitedTwiceCount++
            } else {
                gridCopy[currentY][currentX] = VISITED_NEW_ONCE
                visitedTwiceCount = 0
            }
            if (currentY + direction.yDiff !in grid.indices || currentX + direction.xDiff !in grid[y].indices) {
                break
            }
            if (gridCopy[currentY + direction.yDiff][currentX + direction.xDiff] == OBSTACLE) {
                direction = direction.rotateRight()
                continue
            }
            if (visitedTwiceCount > 500) {
                return true
            }
            currentY += direction.yDiff
            currentX += direction.xDiff
        }
        return false
    }

}
