package com.staffbase.exercises.gameoflife

import java.util.Random
import kotlin.streams.asSequence

data class LivingCell(val x: Int, val y: Int)
typealias DeadCell = LivingCell

interface Screen {
    fun drawPixel(x: Int, y: Int)
}

class Grid(private val livingCells: Set<LivingCell>) {
    companion object {
        fun generate(numLivingCells: Int = 10): Grid {
            return Grid(livingCells = Random().ints().asSequence()
                .chunked(2)
                .map { (x, y) -> LivingCell(x, y) }
                .distinct()
                .take(numLivingCells)
                .toSet())
        }

        private val RELATIVE_NEIGHBORS = arrayOf(
            -1 to -1, 0 to -1, 1 to -1,
            -1 to 0, 1 to 0,
            -1 to 1, 0 to 1, 1 to 1
        )
    }

    private val survivingCells: Set<LivingCell>
        get() = livingCells.asSequence()
            .filter { cell -> livingNeighborsOf(cell) in 2..3 }
            .toSet()

    private fun livingNeighborsOf(cell: LivingCell): Int {
        return RELATIVE_NEIGHBORS.asSequence()
            .map { (dx, dy) -> LivingCell(x = cell.x + dx, y = cell.y + dy) }
            .count { neighbor -> neighbor in livingCells }
    }

    private val newBornCells: Set<LivingCell>
        get() {
            return livingCells.asSequence()
                .flatMap { cell ->
                    RELATIVE_NEIGHBORS.asSequence()
                        .map { (dx, dy) -> DeadCell(x = cell.x + dx, y = cell.y + dy) }
                        .filterNot { neighbor -> neighbor in livingCells }
                }
                .distinct()
                .filter { cell -> livingNeighborsOf(cell) == 3 }
                .toSet()
        }

    fun progress(): Grid = Grid(livingCells = survivingCells + newBornCells)

    fun renderTo(target: Screen) {
        livingCells.forEach { (x, y) -> target.drawPixel(x, y) }
    }

    val numberOfLivingCells: Int
        get() = livingCells.size
}

object DummyScreen : Screen {
    override fun drawPixel(x: Int, y: Int) {
        println("rendered pixel at ($x, $y)")
    }
}

fun main() {
    (1..10).fold(Grid.generate()) { world, _ ->
        world.renderTo(DummyScreen)
        world.progress()
    }.renderTo(DummyScreen)
}