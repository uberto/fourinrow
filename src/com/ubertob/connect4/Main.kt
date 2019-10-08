package com.ubertob.connect4


/*
* who is winning
 */


data class Board(private val moves: List<Move> = listOf()) {

    fun nextPlayer(): Player = moves.lastOrNull()?.player?.opponent() ?: Player.Nought

    fun show() {
        Row.values().reversed().forEach { println(it.show(moves)) }
        println("|-+-+-+-+-+-+-|")
        println(Column.values().map { it.name }.joinToString(separator = "|", prefix = "|", postfix = "|"))
    }

    fun playMove(d: Column): Board = Board(moves + Move(d, nextPlayer()))

    fun whoIsWinning(): Player? = null


}


enum class Column { a, b, c, d, e, f, g }
enum class Row { `1`, `2`, `3`, `4`, `5`, `6` }

fun Row.show(moves: List<Move>): String {

    return Column.values().map { col ->
        val pile = moves.filter { it.col == col }
        if (pile.size <= this.ordinal)
            ' '
        else
            pile[this.ordinal].player.sign
    }.joinToString(separator = "|", prefix = "|", postfix = "|")


}

data class Move(val col: Column, val player: Player)

private fun Player.opponent(): Player =
    if (this == Player.Nought)
        Player.Cross
    else
        Player.Nought

enum class Player(val sign: Char){
    Cross('X'), Nought('O')
}


fun main() {

    val emptyBoard = Board()
    val moves = listOf(
        Column.d, Column.d,
        Column.a, Column.b,
        Column.c, Column.d,
        Column.d, Column.c,
        Column.c, Column.d,
        Column.b
    )

    val board = moves.fold(emptyBoard) {board, move -> board.playMove(move)}
    board.show()

    val winner = board.whoIsWinning()
    if (winner != null){
        println("\n\n${winner.name} has won!!")
    } else {
        println("\n\nnobody won")
    }

}
