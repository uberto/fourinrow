package com.ubertob

import com.ubertob.Column.*
import com.ubertob.Player.Cross
import com.ubertob.Player.Nought

/*

TODO:
+ show empty board
+ place one chip
+ pile multiple chips
+ add second player
+ show winner




1. start with main
2. draw board with println
3. cross todo empty board
4. create Board class with show method
5. add Column enum
6. add Row enum
7. add placeChip(c: Column)
8. add moves to Board
9. add render row and reversed
10. cross todo place one chip
11. add new moves to main
12. cross todo pile multiple chips
11. add new enum Player
14. add class Move and change Board ctr
15. add prop nextPlayer and opposite
16. change render Row
17. use fold on main
18. cross todo add second player
19. add fun whoIsWinning and print it with move and player name
20. add Cell
21. add countInLine
22. add south fun
23. add playerAtCell fun
24. calculate whoIsWinning
25. add rest of the compass fun
26. cross todo show winner


 */

enum class Column { a, b, c, d, e, f, g }

fun Column.prev(): Column? = when (this) {
    a -> null
    else -> Column.values()[this.ordinal - 1]
}

fun Column.next(): Column? = when (this) {
    g -> null
    else -> Column.values()[this.ordinal + 1]
}

enum class Row { r1, r2, r3, r4, r5, r6 }


fun Row.prev(): Row? = when (this) {
    Row.r1 -> null
    else -> Row.values()[this.ordinal - 1]
}

fun Row.next(): Row? = when (this) {
    Row.r6 -> null
    else -> Row.values()[this.ordinal + 1]
}

enum class Player(val sign: Char) { Nought('O'), Cross('X') }

data class Move(val column: Column, val player: Player)

data class Board(val moves: List<Move>) {

    val lastMove: Move? = moves.lastOrNull()
    val nextPlayer = lastMove?.player?.opposite() ?: Nought

    fun show() {

        if (lastMove != null)
            println("Move ${moves.size}   Player ${lastMove.player} on ${lastMove.column}")
        Row.values().map { it.render() }
            .reversed()
            .forEach { println(it) }

        println("|-+-+-+-+-+-+-|")

        println(Column.values()
            .joinToString(separator = "|", prefix = "|", postfix = "|")
            { it.name })

        val winner = whoIsWinning()
        println(if (winner == null) "No winner" else "Winner is $winner")

        println("\n\n")

    }

    val piles = moves.groupBy({ it.column }, { it.player })

    private fun whoIsWinning(): Player? {

        if (lastMove == null)
            return null

        val lastRow = (piles[lastMove.column]?.size ?: 0) - 1
        val cell = Cell(lastMove.column, Row.values()[lastRow])

        if (cell.countLine(south) >= 3
            || cell.countLine(east) + cell.countLine(west) >= 3
            || cell.countLine(southeast) + cell.countLine(northwest) >= 3
            || cell.countLine(southwest) + cell.countLine(northeast) >= 3
        )
            return nextPlayer.opposite()

        return null
    }

    private fun Cell.countLine(f: (Cell) -> Cell?): Int {
        var cc: Cell? = this
        var tot = 0
        while (cc != null) {
            cc = f(cc)
            if (playerAtCell(cc) != nextPlayer.opposite())
                break
            tot += 1
        }
        return tot
    }

    private fun playerAtCell(cell: Cell?): Player? =
        if (cell == null)
            null
        else if (piles[cell.column]?.size ?: 0 > cell.row.ordinal)
            piles[cell.column]?.get(cell.row.ordinal)
        else
            null


    private val south: (Cell) -> Cell? = { it.row.prev()?.let { newRow -> Cell(it.column, newRow) } }
    private val east: (Cell) -> Cell? = { it.column.next()?.let { newCol -> Cell(newCol, it.row) } }
    private val west: (Cell) -> Cell? = { it.column.prev()?.let { newCol -> Cell(newCol, it.row) } }
    private val southwest: (Cell) -> Cell? =
        { it.column.prev()?.let { newCol -> it.row.prev()?.let { newRow -> Cell(newCol, newRow) } } }
    private val southeast: (Cell) -> Cell? =
        { it.column.next()?.let { newCol -> it.row.prev()?.let { newRow -> Cell(newCol, newRow) } } }
    private val northwest: (Cell) -> Cell? =
        { it.column.prev()?.let { newCol -> it.row.next()?.let { newRow -> Cell(newCol, newRow) } } }
    private val northeast: (Cell) -> Cell? =
        { it.column.next()?.let { newCol -> it.row.next()?.let { newRow -> Cell(newCol, newRow) } } }

    private fun Row.render(): String =
        Column.values().map { col ->
            val pile = moves.filter { it.column == col }
            if (pile.size > this.ordinal)
                pile[this.ordinal].player.sign
            else
                ' '
        }.joinToString(separator = "|", prefix = "|", postfix = "|")

    fun placeChip(column: Column): Board =
        Board(moves + Move(column, nextPlayer)).also { it.show() }
}


data class Cell(val column: Column, val row: Row)

private fun Player.opposite(): Player =
    when (this) {
        Nought -> Cross
        Cross -> Nought
    }

val emptyBoard = Board(emptyList())

fun main() {

    val moves = listOf(
        d, e,
        e, f,
        d, d,
        a, b,
        c, a,
        b, b,
        c, f,
        c, f,
        c, f
    )

    moves
        .fold(emptyBoard) { b, c -> b.placeChip(c) }

}