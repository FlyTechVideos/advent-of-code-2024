package day2

import InputReader
import kotlin.math.abs

class Day2 {

    private val input = InputReader.readInput(2, test = false)

    fun solve1() {
        input.sumOf { row ->
            row.split(" ")
                .map { it.toInt() }
                .windowed(2, 1)
                .map { it[1] - it[0] }
                .let { list ->
                    @Suppress("USELESS_CAST")
                    if (checkIsSafe(list)) 1 else 0 as Int
                }
        }.let {
            println(it)
        }
    }

    private fun sign(i: Int): Int {
        return if (i > 0) 1 else -1
    }

    fun solve2() {
        input.sumOf { row ->
            row.split(" ")
                .map { it.toInt() }
                .let { list ->
                    @Suppress("USELESS_CAST")
                    if (isSafeDampened(list)) 1 else 0 as Int
                }
        }.let {
            println(it)
        }
    }

    private fun isSafeDampened(list: List<Int>): Boolean {
        if (checkIsSafeList(list)) return true
        return list.indices.any {
            checkIsSafeList(list.subList(0, it) + list.subList(it + 1, list.size))
        }
    }

    private fun checkIsSafeList(numberList: List<Int>): Boolean {
        return checkIsSafe(numberList.windowed(2).map { it[1] - it[0] })
    }

    private fun checkIsSafe(diffList: List<Int>): Boolean {
        val sign = sign(diffList[0])
        return diffList.all { sign == sign(it) && 1 <= abs(it) && abs(it) <= 3 }
    }
}