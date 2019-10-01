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

    val player = prevBoard.nextPlayer

    override val nextPlayer = player.other()


    override val moves: List<Move> = prevBoard.moves + Move(prevBoard.nextPlayer, column)

    val colMap =
        Column.values().map { c -> c to moves.filter { it.column == c }.map { it.player }.let { Pile(it) } }.toMap()

    val row: Row = Row.values()[colMap[column]?.rows?.size ?: 0]

    fun playerInBoard(col: Column?, row: Row?): Player? =
        if (row == null || col == null) null else colMap[col]?.rows?.getOrNull(row.ordinal)

    private fun drawGrid(): String = Row.values()
        .map { row ->
            colMap.values
                .map { it.render(6 - row.ordinal) }
                .joinToString("|")
                .let { "|$it|" }
        }
        .joinToString("\n")

    override fun show(): String = """
Move number ${moves.size} player ${player} column ${column} row ${row}
        
+-+-+-+-+-+-+-+
${drawGrid()}
+-+-+-+-+-+-+-+
|a|b|c|d|e|f|g|
        
        
"""

    fun playerAtCoord(c: Coord): Player? = playerInBoard(c.col, c.row)


    private fun nullCoord(column: Column?, row: Row?): Coord? =
        if (column != null && row != null)
            Coord(column, row)
        else
            null

    fun Coord.se(): Coord? = nullCoord(col.next(), row.prev())
    fun Coord.ne(): Coord? = nullCoord(col.next(), row.next())
    fun Coord.e(): Coord? = nullCoord(col.next(), row)
    fun Coord.sw(): Coord? = nullCoord(col.prev(), row.prev())
    fun Coord.nw(): Coord? = nullCoord(col.prev(), row.next())
    fun Coord.w(): Coord? = nullCoord(col.prev(), row)
    fun Coord.s(): Coord? = nullCoord(col, row.prev())

    fun winner(): Player? = playerInBoard(column, row)
//    move.s().s().s() >= 3
//    move.ne().ne().ne() + move.sw().sw().sw() >= 3 //diagonal  move is +1
//    move.nw().nw().nw() + move.se().se().se() >= 3 //diagonal  move is +1
//    move.e().e().e() + move.w().w().w() >= 3 //horz  move is +1


}

data class Coord(val col: Column, val row: Row)
