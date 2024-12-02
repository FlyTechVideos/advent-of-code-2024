package day1

import kotlin.math.abs

class Day1 {

    private val input = InputReader.readInput(1, test = false)

    fun solve1() {
        println(
            input.map { row ->
                row.split("   ")
                    .map { it.toInt() }
                    .let{ it[0] to it[1] }
            }.unzip()
            .toList()
            .map { it.sorted() }
            .let { it[0].zip(it[1]) }
            .fold(0) { acc, pair -> acc + abs(pair.second - pair.first) }
        )
    }

    fun solve2() {
        val lists = input.map { row ->
            row.split("   ")
                .map { it.toInt() }
                .let{ it[0] to it[1] }
        }.unzip()
        val frequencies = lists.second.groupingBy { it }.eachCount()
        println(
            lists.first.sumOf { frequencies.getOrDefault(it, 0) * it }
        )
    }
}