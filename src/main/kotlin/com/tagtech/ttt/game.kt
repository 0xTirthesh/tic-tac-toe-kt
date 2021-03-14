package com.tagtech.ttt

import arrow.core.Either
import arrow.core.computations.either
import arrow.core.left
import arrow.core.right
import org.slf4j.LoggerFactory

private val log = LoggerFactory.getLogger("TicTacToe")

// --- events ---

fun initGame(): GameState {
  val game = GameState()

  val events = game._events.toMutableList()
  events.add(Event(EventType.INIT, null, null))
  return game.copy(_events = events)
}

fun switchPlayer(game: GameState): GameState {
  val newPlayer = when (game.player) {
    is PlayerCross -> PlayerOhh
    is PlayerOhh -> PlayerCross
  }

  val events = game._events.toMutableList()
  events.add(Event(EventType.SWITCH_USER, null, null))
  return game.copy(player = newPlayer, _events = events)
}

fun makeMove(game: GameState, move: Move): GameState {
  val newBoard = updateBoard(game.board, move)!! // could lead to a runtime err.. impl `Either`
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

fun executeTurn(game: GameState, inputTileSelection: Int): Either<Fault, GameState> =
  either.eager {
    validateInput(game, inputTileSelection).bind()
    val newGameState = makeMove(game, Move(inputTileSelection, game.player))
    when {
      checkForTheWinner(newGameState) -> endGame(newGameState, isTie = false)
      getNoOfMovesLeft(newGameState) == 0 -> endGame(newGameState, isTie = true)
      else -> switchPlayer(newGameState)
    }
  }

// --- utils ---

fun getPlayerAtTile(game: GameState, tileNumber: Int): Player? = game.board.getAllTiles()[tileNumber - 1]

fun validateInput(game: GameState, inputTileSelection: Int): Either<Fault, Unit> =
  if (inputTileSelection in 1..9) {
    val player = getPlayerAtTile(game, inputTileSelection)
    if (player != null) Fault("err-invalid-move", FaultType.INVALID_INPUT).left()
    else Unit.right()
  } else Fault("err-invalid-tile-selected", FaultType.INVALID_INPUT).left()

fun getNoOfMovesLeft(game: GameState): Int =
  9 - game.board.getAllTiles().fold(0) { acc, elm -> elm?.let { acc + 1 } ?: acc }

fun checkForTheWinner(game: GameState): Boolean =
  game.board
    .getEndGameValidatorSequence()
    .fold(false) { acc, tileSet -> acc or tileSet.all { it != null && it == game.player } }

fun updateBoard(board: BoardState, move: Move): BoardState? =
  when (move.tileNumber) {
    1 -> board.copy(a = move.player)
    2 -> board.copy(b = move.player)
    3 -> board.copy(c = move.player)
    4 -> board.copy(d = move.player)
    5 -> board.copy(e = move.player)
    6 -> board.copy(f = move.player)
    7 -> board.copy(g = move.player)
    8 -> board.copy(h = move.player)
    9 -> board.copy(i = move.player)
    else -> null
  }
