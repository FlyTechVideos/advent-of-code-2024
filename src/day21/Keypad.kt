package day21

import kotlin.math.abs

interface Keypad {
    val positionMap: Map<String, Position>
    val forbidden: Position

    fun calculatePath(p1: String, p2: String): String {
        val position1 = positionMap[p1]!!
        val position2 = positionMap[p2]!!
        val distanceX = position2.x - position1.x
        val distanceY = position2.y - position1.y

        val yRaw = if (distanceY < 0) "^" else "v"
        val xRaw = if (distanceX < 0) "<" else ">"

        val y = yRaw.repeat(abs(distanceY))
        val x = xRaw.repeat(abs(distanceX))

        val wouldHitX = position1.x + distanceX == forbidden.x && position1.y == forbidden.y
        val wouldHitY = position1.y + distanceY == forbidden.y && position1.x == forbidden.x

        val path = if (wouldHitY) {
            x + y
        } else if (wouldHitX) {
            y + x
        } else when (Pair(xRaw, yRaw)) {
            Pair(">", "^") -> y + x
            Pair(">", "v") -> y + x
            Pair("<", "^") -> x + y
            Pair("<", "v") -> x + y
            else -> throw IllegalArgumentException("Invalid path")
        }
        return path + "A"
    }
}