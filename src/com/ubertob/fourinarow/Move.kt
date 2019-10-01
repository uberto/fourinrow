package com.ubertob.fourinarow

import java.lang.Exception



data class Move(val player: Player, val column: Column)


data class NonValidMove(val column: String) : Exception()




enum class Column{ a,b,c,d,e,f,g}

enum class Row{ r1,r2,r3,r4,r5,r6}

inline fun <reified E: Enum<E>> Enum<E>.next(): E? =  if( ordinal == Column.values().size - 1)
    null
else
    enumValues<E>()[ordinal + 1]

inline fun <reified E: Enum<E>> Enum<E>.prev(): E? =  if( ordinal == 0)
    null
else
    enumValues<E>()[ordinal - 1]




data class Pile(val rows: List<Player>){

    fun render(row: Int): Char = when {
        row < rows.size -> rows[row].sign
        else -> ' '
    }
}
