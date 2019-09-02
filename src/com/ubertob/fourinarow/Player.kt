package com.ubertob.fourinarow

enum class Player(val sign: Char) { One('X'), Two('O') }

fun Player.other(): Player = when(this){
    Player.One -> Player.Two
    Player.Two -> Player.One
}