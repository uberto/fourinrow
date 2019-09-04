package com.ubertob.fourinarow

sealed class Board() {
    abstract fun show(): String
    abstract val moves: List<Move>
    abstract val nextPlayer: Player

}

object EmptyBoard : Board() {
    override val moves: List<Move> = emptyList()

    override val nextPlayer = Player.One

    override fun show(): String = """
        +-+-+-+-+-+-+-+
        | | | | | | | |
        | | | | | | | |
        | | | | | | | |
        | | | | | | | |
        | | | | | | | |
        | | | | | | | |
        +-+-+-+-+-+-+-+
        |a|b|c|d|e|f|g|

       """.trimIndent()
}

data class GameBoard(val prevBoard: Board, val column: Column) : Board() {

    override val nextPlayer = prevBoard.nextPlayer.other()

    val player = nextPlayer.other()

    override val moves: List<Move> = prevBoard.moves + Move(prevBoard.nextPlayer, column)

    val colMap =
        Column.values().map { c -> c to moves.filter { it.column == c }.map { it.player }.let { Pile(it) } }.toMap()

    val currRow: Int = colMap[column]?.rows?.size ?: 0

    fun playerInBoard(col: Column?, row: Row?): Player? = if (row == null || col == null) null else colMap[col]?.rows?.getOrNull(row.value)

    private fun drawGrid(): String = (1..COL_HEIGHT)
        .map { row -> colMap.values
                .map { it.render(COL_HEIGHT - row ) }
                .joinToString("|")
                .let { "|$it|" }
        }
        .joinToString("\n")

    override fun show(): String = """
Move number ${moves.size} player ${player} column ${column}
        
+-+-+-+-+-+-+-+
${drawGrid()}
+-+-+-+-+-+-+-+
|a|b|c|d|e|f|g|
        
        
"""


    fun winner(): Player? = playerInBoard(column.prev(),  Row.fromInt(currRow -1))

}
