import arrow.core.computations.either
import com.tagtech.ttt.*

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
      val game = initGame().bind()
      play(game).apply { displayWinner(this).bind() }
    }

  result.fold({ println("Game Execution Failed. Message: ${it.message} | ErrType: ${it.type}") }) {
    it._events.forEach { it.print() }
  }
}
