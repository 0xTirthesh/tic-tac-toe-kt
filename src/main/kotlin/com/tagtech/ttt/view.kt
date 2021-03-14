package com.tagtech.ttt

import arrow.core.Either
import arrow.core.computations.either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import java.util.*

val reader = Scanner(System.`in`)

// --- User Input ---

private fun promptInput(message: String): String = print("|- ${message}: ").let { reader.next() }

private fun parseInput(input: String): Either<Fault, Int> =
  if (input.isNotEmpty() and input.isNotBlank()) {
    try {
      input.toInt().right()
    } catch (e: Exception) {
      Fault("err-invalid-input", FaultType.INVALID_INPUT, mapOf("expectation" to "a digit; range: 1-9")).left()
    }
  } else Fault("err-input-is-mandatory", FaultType.INVALID_INPUT).left()

// --- GameState ---

private fun GameState.getDisplayValue(tileNumber: Int): String =
  getPlayerAtTile(this, tileNumber)?.getSymbol() ?: " ${tileNumber} "

fun GameState.displayBoard() =
  println(
    """

      ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
      Board:
          ${getDisplayValue(1)}   ${getDisplayValue(2)}   ${getDisplayValue(3)}

          ${getDisplayValue(4)}   ${getDisplayValue(5)}   ${getDisplayValue(6)}

          ${getDisplayValue(7)}   ${getDisplayValue(8)}   ${getDisplayValue(9)}
      ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    """.trimIndent()
  )

private fun GameState.displayWinner(): Either<Fault, Unit> =
  if (ended) {
    displayBoard()
    val msg = if (winner != null) "${winner.getName()} won the game! Congratulations!" else "It's a Tie!"
    println("\n--------------------------------\nMatch Result: ${msg}\n--------------------------------\n").right()
  } else Fault("err-cannot-display-winner-game-not-ended-yet", FaultType.SYSTEM).left()

// --- Game Play ---

/**
 * the following method will parse user input and process selection
 * in the case of failure; it will retry (until successful)
 */
private fun GameState.processInputAndExecuteTurn(input: String): GameState =
  parseInput(input)
    .flatMap {
      executeTurn(this, it) { m -> println("\n>>> Computer played selected tile number #${m.tileNumber} <<<") }
    }
    .fold({ processInputAndExecuteTurn(promptInput("Err! Invalid Input. Please re-enter valid input")) }, { it })

private fun GameState.nextTurn(): GameState {
  displayBoard()
  println("\n>>> ${player.getName()}'s Turn <<<")
  val input = promptInput("Please enter value from 1-9 as per the availability on the board")
  return processInputAndExecuteTurn(input)
}

fun GameState.start(): Either<Fault, GameState> =
  either.eager {
    // initiate the 1st turn and keep on taking a the next turn until game ends
    // when game ends display the winner

    var game = nextTurn()
    while (!game.ended) game = game.nextTurn()
    game.apply { displayWinner().bind() }
  }

