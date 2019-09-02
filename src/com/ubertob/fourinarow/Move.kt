package com.ubertob.fourinarow

val COL_HEIGHT = 5

data class Move(val player: Player, val colName: ColName)

enum class ColName(val num: Int){ a(1), b(2), c(3), d(4), e(5), f(6), g(7)}

data class Column(val rows: List<Player>){

    fun render(row: Int): Char = when {
        row < rows.size -> rows[row].sign
        else -> ' '
    }
}
