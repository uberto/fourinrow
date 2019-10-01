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





    fun countLine(start: Coord, f: (Coord) -> Coord?): Int {
        val p = playerAtCoord(start)
        var cc: Coord? = start
        var tot = 0
        while(cc != null){
            cc = f(cc)
            if (cc != null && playerAtCoord(cc) == p)
                tot += 1
            else
                break
        }
        return tot
    }

    fun winner(): Player? {
        val cc = Coord(column, row)

        if ((countLine(cc, Coord::se) + countLine(cc, Coord::nw ) + 1 >= 4)
            || (countLine(cc, Coord::sw) + countLine(cc, Coord::ne ) + 1 >= 4)
            || (countLine(cc, Coord::w) + countLine(cc, Coord::e ) + 1 >= 4)
            || (countLine(cc, Coord::s) + 1 >= 4))
            return playerAtCoord(cc)

        return null
    }

}

data class Coord(val col: Column, val row: Row)


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
