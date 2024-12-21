package day21

class StandaloneDirectionalKeypad : Keypad {
    override val positionMap = mutableMapOf(
        "^" to Position(1, 0),
        "A" to Position(2, 0),
        "<" to Position(0, 1),
        "v" to Position(1, 1),
        ">" to Position(2, 1),
    )
    override val forbidden = Position(0, 0)
}