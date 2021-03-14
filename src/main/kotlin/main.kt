import com.tagtech.ttt.initGame
import com.tagtech.ttt.start

@ExperimentalStdlibApi
fun main(args: Array<String>) {
  println(
    """
    Welcome! Let's Play `TicTacToe`

    Rules:
     - Game will prompt for the player's turn. Player 1 marks '{CROSS}' and Player 2 marks '{OH}'
     - Player will have to choose the number from the board they wish to mark.
     - The first player to get 3 of their marks in a row (up, down, across, or diagonally) is the winner.
     - When all 9 squares are full, the game is over.

  """.trimIndent()
  )

  val vsComputer = args.isNotEmpty() && (args[0]).lowercase() == "play-vs-computer"
  val computerPlaysRandom = vsComputer && args.size > 1 && (args[1]).lowercase() != "hard"

  initGame(vsComputer, computerPlaysRandom)
    .apply { println(this) }
    .start()
    .fold({ println("Game Execution Failed. Message: ${it.message} | ErrType: ${it.type}") }) { println(it) }
}
