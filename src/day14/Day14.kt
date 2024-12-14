package day14

import InputReader
import java.io.File

class Day14 {
    private val test = false
    private val xBound = if (test) 11 else 101
    private val yBound = if (test) 7 else 103

    private val input = InputReader.readInput(14, test = test, split = false)[0]

    data class Robot(val x: Int, val y: Int, val dX: Int, val dY: Int) {
        fun move(xBound: Int, yBound: Int) = Robot(
            Math.floorMod(x + dX,  xBound),
            Math.floorMod(y + dY,  yBound),
            dX,
            dY
        )
    }
    data class RobotCounter(
        private val xBound: Int,
        private val yBound: Int,
        var q1: Int = 0,
        var q2: Int = 0,
        var q3: Int = 0,
        var q4: Int = 0,
    ) {
        private val middleColumn = xBound / 2
        private val middleRow = yBound / 2

        fun countRobot(robot: Robot): RobotCounter {
            if (robot.x < middleColumn && robot.y < middleRow) q1++
            if (robot.x > middleColumn && robot.y < middleRow) q2++
            if (robot.x < middleColumn && robot.y > middleRow) q3++
            if (robot.x > middleColumn && robot.y > middleRow) q4++

            return this
        }

        fun factor() = q1 * q2 * q3 * q4
    }

    fun solve1() {
        val robots = parseRobots()
        robots.map {
            var robot = it
            for (i in 0..<100) {
                robot = robot.move(xBound, yBound)
            }
            robot
        }.fold(RobotCounter(xBound, yBound)) { acc, robot ->
            acc.countRobot(robot)
        }.let { println(it.factor()) }
    }

    private fun parseRobots() = "p=(\\d+),(\\d+) v=(-?\\d+),(-?\\d+)".toRegex()
        .findAll(input)
        .map { it.groupValues }
        .map { Robot(it[1].toInt(), it[2].toInt(), it[3].toInt(), it[4].toInt()) }
        .toList()

    fun solve2() {
        if (test) {
            println("Part 2 does not really make sense on the test input.")
            return
        }
        /*
            Proof by looking at the generated field in the output file:
            - First vertical anomaly at 68
            - First horizontal anomaly at 143
            - Second vertical anomaly at 68 + 101(=width)
            - Second horizontal anomaly at 43 + 103(=height)
            - Thus we must figure out a point where they meet
            - 68 + 101x = 43 + 103x => 2x = 25 => x = 12.5
            - Sadly 12.5 is not an integer. If you use 12, the two anomalies will be 1 apart (1279 and 1280)
            - ... and if you use 13, they will be at 1381 and 1382
            - Now, if you add lcm(68, 143) = 10403 to either pair, you will find that they will be 1 apart again
            - So let's say
            - So let's meet in the middle. Luckily 10403 is odd!
            - So we just subtract floor(10403/2) = 5201 from the latter pair (alternatively, ceil and earlier pair)
            - ... and then my math stops working out, because the result is 6583 (or 6482 for earlier pair)
            - ... though I generated all items from 6500 to 6600 and found the answer to be 6532 by simply looking.
            - Sorry to disappoint.
         */

        var robots = parseRobots()
        val outputFile = File("resources/day14_output.txt")
        outputFile.writeText("")
        for (i in 0..6600) {
            println("Current second: $i")
            if (i >= 6500) {
                outputFile.appendText("Current second: $i\n")
                val field = getBlankField()
                robots.forEach {
                    field[it.y * xBound + it.x] = '#'
                }
                val fieldState = field.chunked(xBound).joinToString("\n") { it.joinToString("") }
                outputFile.appendText(fieldState)
                outputFile.appendText("\n\n")
            }
            robots = robots.map { it.move(xBound, yBound) }
        }

    }

    private fun getBlankField() = (0..<xBound * yBound).map { ' ' }.toMutableList()

}
