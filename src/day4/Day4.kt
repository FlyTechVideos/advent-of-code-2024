package day4

import InputReader

class Day4 {

    private val input = InputReader.readInput(4, test = false)

    fun solve1() {
        val inputGrid = input.map { it.split("") }
        println(
            countHorizontalMatches(inputGrid) +
                    countVerticalMatches(inputGrid) +
                    countDiagonalTopLeftBottomRightMatches(inputGrid) +
                    countDiagonalTopRightBottomLeftMatches(inputGrid)
        )
    }

    private fun countHorizontalMatches(inputGrid: List<List<String>>): Int {
        return inputGrid.sumOf { row ->
            row.windowed(4).count { listOf("XMAS", "SAMX").contains(it.joinToString("")) }
        }
    }

    private fun countVerticalMatches(inputGrid: List<List<String>>): Int {
        return inputGrid[0].indices.sumOf { col ->
            inputGrid.windowed(4).count { listOf("XMAS", "SAMX").contains(it.joinToString("") { row -> row[col] }) }
        }
    }

    private fun countDiagonalTopLeftBottomRightMatches(inputGrid: List<List<String>>): Int {
        var count = 0
        for (y in 0..inputGrid.size - 4) {
            for (x in 0..inputGrid[0].size - 4) {
                val word = inputGrid[y][x] + inputGrid[y + 1][x + 1] + inputGrid[y + 2][x + 2] + inputGrid[y + 3][x + 3]
                if (listOf("XMAS", "SAMX").contains(word)) {
                    count++
                }
            }
        }
        return count
    }

    private fun countDiagonalTopRightBottomLeftMatches(inputGrid: List<List<String>>): Int {
        var count = 0
        for (y in 0..inputGrid.size - 4) {
            for (x in 4..<inputGrid[0].size) {
                val word = inputGrid[y][x] + inputGrid[y + 1][x - 1] + inputGrid[y + 2][x - 2] + inputGrid[y + 3][x - 3]
                if (listOf("XMAS", "SAMX").contains(word)) {
                    count++
                }
            }
        }
        return count
    }

    fun solve2() {
        val inputGrid = input.map { it.split("") }
        println(countCrossMasMatches(inputGrid))
    }

    private fun countCrossMasMatches(inputGrid: List<List<String>>): Int {
        var count = 0
        val potentialStarters = listOf("M", "S")
        for (y in 0..<inputGrid.size - 2) {
            for (x in 0..<inputGrid[0].size - 2) {
                if (potentialStarters.contains(inputGrid[y][x])) {
                    count += if (checkIsCrossMas(inputGrid, x, y)) 1 else 0
                }
            }
        }
        return count
    }

    private fun checkIsCrossMas(inputGrid: List<List<String>>, x: Int, y: Int): Boolean {
        if (inputGrid[y + 1][x + 1] != "A") {
            return false
        }

        val topLeft = inputGrid[y][x]
        val topRight = inputGrid[y][x + 2]

        val bottomLine = inputGrid[y + 2][x] + inputGrid[y + 2][x + 2]
        return if (topLeft == "M" && topRight == "M") {
            bottomLine == "SS"
        } else if (topLeft == "M" && topRight == "S") {
            bottomLine == "MS"
        } else if (topLeft == "S" && topRight == "M") {
            bottomLine == "SM"
        } else if (topLeft == "S" && topRight == "S") {
            bottomLine == "MM"
        } else {
            false
        }
    }
}
