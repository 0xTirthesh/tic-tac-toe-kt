package com.tagtech.ttt

import arrow.core.Either
import arrow.core.computations.either
import arrow.core.left
import arrow.core.right
import java.util.*

val reader = Scanner(System.`in`)

const val CROSS = "❌"
const val OHH = "⭕"

val DEFAULT_TILES = listOf("1️⃣", "2️⃣", "3️⃣", "4️⃣", "5️⃣", "6️⃣", "7️⃣", "8️⃣", "9️⃣")

fun displayWelcomeMessage(): Unit {
  """
    Welcome! Let's Play `TicTacToe`

    Rules:
     - Game will prompt for the player's turn. Player 1 marks '{CROSS}' and Player 2 marks '{OH}'
     - Player will have to choose the number from the board they wish to mark.
     - The first player to get 3 of their marks in a row (up, down, across, or diagonally) is the winner.
     - When all 9 squares are full, the game is over.
  """.trimIndent()
}

fun promptInput(message: String): String = print(">> ${message}: ").let { reader.next() }

fun getPlayerName(game: GameState): String =
  when (game.player) {
    is Cross -> "Player 1"
    is Ohh -> "Player 2"
  }

fun displayBoard(game: GameState) =
  println(
    """
    ${getDisplayValue(game, 1)}  ${getDisplayValue(game, 2)}  ${getDisplayValue(game, 3)}

    ${getDisplayValue(game, 4)}  ${getDisplayValue(game, 5)}  ${getDisplayValue(game, 6)}

    ${getDisplayValue(game, 7)}  ${getDisplayValue(game, 8)}  ${getDisplayValue(game, 9)}
    """.trimIndent()
  )

fun declareWinner(): Unit = TODO()

fun getSymbol(player: Player) =
  when (player) {
    is Cross -> CROSS
    is Ohh -> OHH
  }

private fun tileNumberToPosition(input: Int): Position? =
  when (input) {
    1 -> Pair(Index.One, Index.One)
    2 -> Pair(Index.One, Index.Two)
    3 -> Pair(Index.One, Index.Three)
    4 -> Pair(Index.Two, Index.One)
    5 -> Pair(Index.Two, Index.Two)
    6 -> Pair(Index.Two, Index.Three)
    7 -> Pair(Index.Three, Index.One)
    8 -> Pair(Index.Three, Index.Two)
    9 -> Pair(Index.Three, Index.Three)
    else -> null
  }

fun parseInput(game: GameState, input: String): Either<Fault, Move> =
  if (input.isNotEmpty() and input.isNotBlank()) {
    try {
      val tileNumber = input.toInt()
      if (tileNumber in 1..9) Move(tileNumber, game.player).right()
      else Fault("err-tile-selected", FaultType.INVALID_INPUT, mapOf("expectation" to "a digit; range: 1-9")).left()
    } catch (e: Exception) {
      Fault("err-invalid-input", FaultType.INVALID_INPUT, mapOf("expectation" to "a digit; range: 1-9")).left()
    }
  } else Fault("err-input-is-mandatory", FaultType.INVALID_INPUT).left()

fun onUserInputErr(game: GameState): (Fault) -> GameState =
  {
    val newInput = promptInput("Err! Invalid Input. Please re-enter valid input")
    playTurn(game, newInput)
  }

fun playTurn(game: GameState, input: String): GameState {
  val result: Either<Fault, GameState> = either.eager {
    val move = parseInput(game, input).bind()
    playRound(game, move).bind()
  }

  return result.fold(onUserInputErr(game), { it })
}

fun getDisplayValue(game: GameState, tileNumber: Int): String =
  getValue(game = game, tileNumber)?.let { getSymbol(it) } ?: DEFAULT_TILES[tileNumber - 1]
