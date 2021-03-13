package com.tagtech.ttt

import arrow.core.Either
import arrow.core.computations.either
import arrow.core.left
import arrow.core.right

// --- events ---

fun initGame(): GameState {
  val game = GameState()

  val events = game._events.toMutableList()
  events.add(Event(EventType.INIT, null, null))
  return game.copy(_events = events)
}

fun switchPlayer(game: GameState): GameState {
  val newPlayer = when (game.player) {
    is Cross -> Ohh
    is Ohh -> Cross
  }

  val events = game._events.toMutableList()
  events.add(Event(EventType.SWITCH_USER, null, null))
  return game.copy(player = newPlayer, _events = events)
}

fun makeMove(game: GameState, move: Move): GameState {
  val newBoard = updateBoard(game.board, move)
  val events = game._events.toMutableList()
  events.add(Event(EventType.UPDATE_BOARD, move, null))
  return game.copy(board = newBoard, _events = events)
}

fun endGame(game: GameState, isTie: Boolean): GameState {
  val events = game._events.toMutableList()
  val winner = if (isTie) null else game.player
  events.add(Event(EventType.GAME_END, null, winner))
  return game.copy(ended = true, winner = winner, _events = events)
}

// --- executor ---

fun playRound(game: GameState, move: Move): Either<Fault, GameState> =
  either.eager {
    validateMove(game, move).bind()
    val newGameState = makeMove(game, move)
    when {
      checkForTheWinner(newGameState) -> endGame(newGameState, isTie = false)
      getNoOfMovesLeft(newGameState) == 0 -> endGame(newGameState, isTie = true)
      else -> switchPlayer(newGameState)
    }
  }

// --- utils ---

fun validateMove(game: GameState, move: Move): Either<Fault, Unit> =
  getValue(game, move.tileNumber)?.let { Fault("err-invalid-move", FaultType.INVALID_INPUT).left() } ?: Unit.right()

fun getValue(game: GameState, tileNumber: Int): Player? = allPositions(game.board)[tileNumber - 1]

fun getNoOfMovesLeft(game: GameState): Int =
  9 - allPositions(game.board).fold(0) { acc, elm -> elm?.let { acc + 1 } ?: acc }

fun checkForTheWinner(game: GameState): Boolean =
  endGameCheckExecutionSequence.fold(false) { acc, block ->
    acc or block(game.board).all { it != null && it == game.player }
  }

fun updateBoard(board: BoardState, move: Move): BoardState = TODO()