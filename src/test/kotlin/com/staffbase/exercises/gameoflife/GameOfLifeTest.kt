package com.staffbase.exercises.gameoflife

import kotlin.test.Test
import kotlin.test.assertEquals

class GameOfLifeTest {
    @Test
    fun `generate should generate exactly n values`() {
        repeat(30) { numberOfLivingCells ->
            val grid = Grid.generate(numLivingCells = numberOfLivingCells)
            assertEquals(expected = numberOfLivingCells, actual = grid.numberOfLivingCells)

            val screen = TestScreen()
            grid.renderTo(screen)
            assertEquals(expected = numberOfLivingCells, actual = screen.callCounts.size)
            assertEquals(expected = numberOfLivingCells, actual = screen.callCounts.values.sum())
        }
    }
}
