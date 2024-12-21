package day21

class DirectionalKeypad(private val nextKeypad: Keypad) : Keypad {
    override val positionMap = mutableMapOf(
        "^" to Position(1, 0),
        "A" to Position(2, 0),
        "<" to Position(0, 1),
        "v" to Position(1, 1),
        ">" to Position(2, 1),
    )
    override val forbidden = Position(0, 0)

    override fun calculatePath(p1: String, p2: String): String {
        val calculatedPath = "A" + nextKeypad.calculatePath(p1, p2)
        val path = calculatedPath.toList()
            .windowed(2)
            .joinToString("") { super.calculatePath(it[0].toString(), it[1].toString()) }

        return path
    }
}