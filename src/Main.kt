import com.ubertob.fourinarow.*
import com.ubertob.fourinarow.ColName.*

fun main(){
    println("Ready to play?")

    var currBoard: Board = EmptyBoard

    val cols = listOf(
        c,d,d,b,e,e,c,g,c,c,e,g,a,d,e,g,g,a,d
    )

    cols.fold(listOf(Move(Player.One, d))){l, c -> l.plus(Move(l.last().player.other(), c)) }.forEach {
        println(currBoard.show())
        currBoard = GameBoard(currBoard, it)
    }

    println(currBoard.show())


}