package com.ubertob.fourinarow


data class Move(val player: Player, val column: Column)


enum class Column { a, b, c, d, e, f, g }

enum class Row { r1, r2, r3, r4, r5, r6 }

inline fun <reified E : Enum<E>> Enum<E>.next(): E? = if (ordinal >= enumValues<E>().size - 1)
    null
else
    enumValues<E>()[ordinal + 1]

inline fun <reified E : Enum<E>> Enum<E>.prev(): E? = if (ordinal == 0)
    null
else
    enumValues<E>()[ordinal - 1]


data class Pile(val tokens: List<Player>) {

    fun render(row: Row): Char = if (row.ordinal < tokens.size)
        tokens[row.ordinal].sign
    else
        ' '


    fun player(row: Row): Player? =
        tokens.getOrNull(row.ordinal)

    fun lastRow(): Row = Row.values()[tokens.size- 1]

}
