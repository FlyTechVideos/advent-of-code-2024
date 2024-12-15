package day15

import InputReader

class Day15 {

    private val input = InputReader.readInput(15, test = false, split = false)[0]

    fun solve1() {
        val parts = input.split("\n\n")

        val (map, moves) = parseInput(parts)
        var (x, y) = findTheStartingPoint(map)
        for (move in moves) {
            var nextX = x
            var nextY = y
            when (move) {
                "^" -> nextY--
                "v" -> nextY++
                "<" -> nextX--
                ">" -> nextX++
            }
            if (map[nextY][nextX] == "#") {
                continue
            }
            if (map[nextY][nextX] == ".") {
                map[y][x] = "."
                map[nextY][nextX] = "@"
                x = nextX
                y = nextY
                continue
            }
            if (map[nextY][nextX] == "O") {
                var nextLookaheadY = nextY
                var nextLookaheadX = nextX
                while (true) {
                    when (move) {
                        "^" -> nextLookaheadY--
                        "v" -> nextLookaheadY++
                        "<" -> nextLookaheadX--
                        ">" -> nextLookaheadX++
                    }
                    if (nextLookaheadY < 0 || nextLookaheadY >= map.size ||
                        nextLookaheadX < 0 || nextLookaheadX >= map[nextLookaheadY].size) {
                        break
                    }
                    if (map[nextLookaheadY][nextLookaheadX] == "#") {
                        break
                    }
                    if (map[nextLookaheadY][nextLookaheadX] == "O") {
                        continue
                    }
                    if (map[nextLookaheadY][nextLookaheadX] == ".") {
                        map[nextLookaheadY][nextLookaheadX] = "O"
                        map[nextY][nextX] = "@"
                        map[y][x] = "."
                        x = nextX
                        y = nextY
                        break
                    }
                }
            }
        }
        var sum = 0
        for (yPos in map.indices) {
            for (xPos in map[yPos].indices) {
                if (map[yPos][xPos] == "O") {
                    sum += 100 * yPos + xPos
                }
            }
        }
        println(sum)
    }

    private fun parseInput(parts: List<String>): Pair<List<MutableList<String>>, List<String>> {
        val map = parts[0].split("\n").map {
            it.trim().split("").drop(1).dropLast(1).toMutableList()
        }
        val moves = parts[1].replace("\n", "")
            .split("").drop(1).dropLast(1)
        return Pair(map, moves)
    }

    private fun findTheStartingPoint(map: List<List<String>>): Pair<Int, Int> {
        for (i in map.indices) {
            for (j in map[i].indices) {
                if (map[i][j] == "@") {
                    return Pair(j, i)
                }
            }
        }
        throw IllegalArgumentException("No starting point found")
    }

    fun solve2() {
        val parts = input
            .replace("#", "##")
            .replace("O", "[]")
            .replace(".", "..")
            .replace("@", "@.")
            .split("\n\n")

        val (map, moves) = parseInput(parts)
        var (x, y) = findTheStartingPoint(map)
        for (move in moves) {
            println(map.joinToString("\n") { it.joinToString("") })
            var nextX = x
            var nextY = y
            when (move) {
                "^" -> nextY--
                "v" -> nextY++
                "<" -> nextX--
                ">" -> nextX++
            }
            if (map[nextY][nextX] == "#") {
                continue
            }
            if (map[nextY][nextX] == ".") {
                map[y][x] = "."
                map[nextY][nextX] = "@"
                x = nextX
                y = nextY
                continue
            }
            if (map[nextY][nextX] == "[" || map[nextY][nextX] == "]") {
                val nextIsRight = map[nextY][nextX] == "]"
                if (listOf("^", "v").contains(move)) {
                    val nextLookaheadXLeft = if (nextIsRight) nextX - 1 else nextX
                    val nextLookaheadXRight = if (nextIsRight) nextX else nextX + 1

                    val isPossible = checkIfPossible(nextY, nextLookaheadXLeft, nextLookaheadXRight, map, move)
                    if (isPossible) {
                        doTheMove(nextY, nextX, map, move)
                        y = nextY
                    }
                } else {
                    var nextLookaheadX = nextX

                    while (true) {
                        when (move) {
                            "<" -> nextLookaheadX--
                            ">" -> nextLookaheadX++
                        }
                        if (nextLookaheadX < 0 || nextLookaheadX >= map[nextY].size) {
                            break
                        }
                        if (map[nextY][nextLookaheadX] == "#") {
                            break
                        }
                        if (map[nextY][nextLookaheadX] == "[" || map[nextY][nextLookaheadX] == "]") {
                            continue
                        }
                        if (map[nextY][nextLookaheadX] == ".") {
                            when (move) {
                                "<" -> {
                                    var next = "["
                                    for (i in nextLookaheadX..<nextX) {
                                        map[nextY][i] = next
                                        next = if (next == "[") "]" else "["
                                    }
                                }
                                ">" -> {
                                    var next = "]"
                                    for (i in nextX..nextLookaheadX) {
                                        map[nextY][i] = next
                                        next = if (next == "[") "]" else "["
                                    }
                                }
                            }
                            map[nextY][nextX] = "@"
                            map[y][x] = "."
                            x = nextX
                            y = nextY
                            break
                        }
                    }
                }
            }
        }
        println(map.joinToString("\n") { it.joinToString("") })
        var sum = 0
        for (yPos in map.indices) {
            for (xPos in map[yPos].indices) {
                if (map[yPos][xPos] == "[") {
                    // This is supposed to work but apparently it does not???
                    // val xValue = min(xPos, map[yPos].size - (xPos + 2))
                    // val yValue = min(yPos, map.size - (y + 1))
                    // sum += 100 * yValue + xValue
                    sum += 100 * yPos + xPos
                }
            }
        }
        println(sum)
    }

    private fun checkIfPossible(currentY: Int, xLeft: Int, xRight: Int, map: List<MutableList<String>>, move: String): Boolean {
        val nextY = if (move == "^") currentY - 1 else currentY + 1
        if (map[nextY][xLeft] == "#" || map[nextY][xRight] == "#") {
            return false
        }
        if (map[nextY][xLeft] == "." && map[nextY][xRight] == ".") {
            return true
        }
        if (map[nextY][xLeft] == "[" && map[nextY][xRight] == "]") {
            return checkIfPossible(nextY, xLeft, xRight, map, move)
        }
        var isPossible = true
        if (map[nextY][xLeft] == "]") {
            isPossible = isPossible && checkIfPossible(nextY, xLeft - 1, xLeft, map, move)
        }
        if (map[nextY][xRight] == "[") {
            isPossible = isPossible && checkIfPossible(nextY, xRight, xRight + 1, map, move)
        }
        return isPossible
    }

    private fun doTheMove(currentY: Int, currentX: Int, map: List<MutableList<String>>, move: String) {
        val previousY = if (move == "^") currentY + 1 else currentY - 1
        val nextY = if (move == "^") currentY - 1 else currentY + 1

        if (map[currentY][currentX] == "]") {
            doTheMove(nextY, currentX - 1, map, move)
            doTheMove(nextY, currentX, map, move)
        } else if (map[currentY][currentX] == "[") {
            doTheMove(nextY, currentX, map, move)
            doTheMove(nextY, currentX + 1, map, move)
        }

        map[currentY][currentX] = map[previousY][currentX]
        map[previousY][currentX] = "."
    }

}
