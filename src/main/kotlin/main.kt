import com.tagtech.ttt.*

fun main() {
  displayWelcomeMessage()
  val game = initGame()

  displayBoard(game)
  val playerName = getPlayerName(game)

  println("${playerName}'s Turn ...")
  val input = promptInput("Please enter value from 1-9 as per the availability on the board")
  val gameState = playTurn(game, input)
}
