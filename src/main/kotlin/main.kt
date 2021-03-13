import arrow.core.computations.either
import com.tagtech.ttt.Fault
import com.tagtech.ttt.GameState
import com.tagtech.ttt.displayBoard
import com.tagtech.ttt.displayWelcomeMessage
import com.tagtech.ttt.displayWinner
import com.tagtech.ttt.getName
import com.tagtech.ttt.initGame
import com.tagtech.ttt.playTurn
import com.tagtech.ttt.promptInput

private fun play(game: GameState): GameState {
  displayBoard(game)
  println(">>> ${game.player.getName()}'s Turn <<<")
  val input = promptInput("Please enter value from 1-9 as per the availability on the board")
  return with(playTurn(game, input)) { if (ended) game else play(game) }
}

fun main() {
  displayWelcomeMessage()
  val result =
    either.eager<Fault, GameState> {
      val game = initGame()
      play(game).apply { displayWinner(this).bind() }
    }

  result.fold({ println("Game Execution Failed. Message: ${it.message} | ErrType: ${it.type}") }) { println(it) }
}
