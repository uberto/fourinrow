package com.ubertob.fourinarow

sealed class Board() {
    abstract fun show(): String
    abstract val moves: List<Move>

}

object EmptyBoard : Board() {
    override val moves: List<Move> = emptyList()

    override fun show(): String = """
        +-+-+-+-+-+-+-+
        | | | | | | | |
        | | | | | | | |
        | | | | | | | |
        | | | | | | | |
        | | | | | | | |
        +-+-+-+-+-+-+-+
        |a|b|c|d|e|f|g|

       """.trimIndent()
}

data class GameBoard(val prevBoard: Board, val move: Move) : Board() {

    override val moves: List<Move> = prevBoard.moves + move

    val colMap =
        ColName.values().map { c -> c to moves.filter { it.colName == c }.map { it.player }.let { Column(it) } }.toMap()

    private fun drawGrid(): String = (1..COL_HEIGHT)
        .map { row -> colMap.values
                .map { it.render(COL_HEIGHT - row ) }
                .joinToString("|")
                .let { "|$it|" }
        }
        .joinToString("\n")

    override fun show(): String = """
Move number ${moves.size} player ${move.player} column ${move.colName}
        
+-+-+-+-+-+-+-+
${drawGrid()}
+-+-+-+-+-+-+-+
|a|b|c|d|e|f|g|
        
        
"""

}
