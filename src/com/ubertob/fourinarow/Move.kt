package com.ubertob.fourinarow

val COL_HEIGHT = 6

data class Move(val player: Player, val column: Column)

@Suppress("DataClassPrivateConstructor")
data class Row private constructor(val value: Int) {
    companion object {
        fun fromInt(x: Int): Row? = if (x in 0.. COL_HEIGHT) Row(x) else null
    }
}

enum class Column{ a,b,c,d,e,f,g}

fun Column.prev(): Column? = when (this) {
    Column.a -> null
    else -> Column.values()[this.ordinal - 1]
}

fun Column.next(): Column? = when (this) {
    Column.g -> null
    else -> Column.values()[this.ordinal + 1]
}

data class Pile(val rows: List<Player>){

    fun render(row: Int): Char = when {
        row < rows.size -> rows[row].sign
        else -> ' '
    }
}
