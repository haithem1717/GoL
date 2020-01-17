package com.staffbase.exercises.gameoflife

class TestScreen : Screen {
    private val _callCounts: MutableMap<Pair<Int, Int>, Int> = mutableMapOf()

    val callCounts: Map<Pair<Int, Int>, Int>
        get() = _callCounts

    override fun drawPixel(x: Int, y: Int) {
        _callCounts[x to y] = (_callCounts[x to y] ?: 0) + 1
    }
}