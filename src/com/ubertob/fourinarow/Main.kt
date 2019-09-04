package com.ubertob.fourinarow

import com.ubertob.fourinarow.Column.*

fun main(){
    println("Ready to play?")

    val cols = listOf(
        c,d,d,b,e,e,c,g,c,c,e,g,a,d,e,g,g,a,d
    )

    val finalBoard: Board = cols.fold(EmptyBoard as Board) { b, col ->
        println(b.show())
        GameBoard(b, col)
    }

    println(finalBoard.show())


    //add ask move
    //add valid move
    //add check winner

}