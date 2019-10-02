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

    val row: Row = Row.values()[(colMap[column]?.rows?.size ?: 0 )- 1]

    private fun drawGrid(): String = Row.values()
        .map { row ->
            colMap.values
                .map { it.render(row) }
                .joinToString("|")
                .let { "|$it|" }
        }.reversed()
        .joinToString("\n")

    override fun show(): String = """
Move number ${moves.size} player ${player} column ${column} row ${row}
        
+-+-+-+-+-+-+-+
${drawGrid()}
+-+-+-+-+-+-+-+
|a|b|c|d|e|f|g|
        
Winner: ${winner()}        
"""

    fun playerAtCoord(c: Coord): Player? = colMap[c.col]?.player(c.row)

    fun countLine(f: (Coord) -> Coord?): Int {
        var cc: Coord? = Coord(column, row)
        var tot = 0
        while(cc != null){
            cc = f(cc)
            if (cc != null && playerAtCoord(cc) == player)
                tot += 1
            else
                break
        }
        return tot
    }

    fun winner(): Player? {
        val cc = Coord(column, row)

        val diag = countLine( Coord::se) + countLine(Coord::nw) + 1
        val revDiag = countLine(Coord::sw) + countLine(Coord::ne) + 1
        val horz = countLine(Coord::w) + countLine(Coord::e) + 1
        val vert = countLine(Coord::s) + 1
        if (diag >= 4 || revDiag >= 4 || horz >= 4 || vert >= 4)
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
