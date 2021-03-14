@file:Suppress("ComplexRedundantLet")

package com.tagtech.ttt

import arrow.core.Either
import arrow.core.computations.either
import arrow.core.left
import arrow.core.right
import org.slf4j.LoggerFactory
import java.util.*

private val log = LoggerFactory.getLogger("TicTacToe")

// --- events ---

fun initGame(playingAgainstComputer: Boolean): GameState {
  val game = GameState(vsComputer = playingAgainstComputer)

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

fun playComputerTurn(game: GameState): GameState {
  val move = getComputerMove(game)
  log.debug("Executing Computers' Turn; Selected Tile: ${move.tileNumber}")
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
    log.debug("Executing Players' Turn; Selected Tile: ${inputTileSelection}")
    validateInput(game, inputTileSelection).bind()
    makeMove(game, Move(inputTileSelection, game.player))
      .let { endGameOrSwitchUser(it) }
      .let { if (!it.ended && it.vsComputer) playComputerTurn(it).let { g -> endGameOrSwitchUser(g) } else it }
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

fun endGameOrSwitchUser(game: GameState) =
  when {
    checkForTheWinner(game) -> endGame(game, isTie = false)
    getNoOfMovesLeft(game) == 0 -> endGame(game, isTie = true)
    else -> switchPlayer(game)
  }

fun getAvailableTiles(game: GameState): List<Int> =
  game.board
    .apply { log.debug(this.toString()) }
    .getAllTiles().withIndex().filter { it.value == null }.map { it.index + 1 }
    .apply { log.debug(this.toString()) }

fun getTileWhichBlocksPlayersWin(game: GameState): Int? =
  game.board.getEndGameValidatorSequence().let { combinations ->
    val winningCombinations =
      combinations.withIndex().filter { Collections.frequency(it.value, PlayerCross) == 2 }.map { it.index }

    if (winningCombinations.isNotEmpty()) {
      val winningCombination = winningCombinations.first()
        .apply { log.debug("Player might win at: ${WINNING_COMBINATIONS[this].first}") }

      winningCombination.let { combinations[it] }
        .withIndex().filter { it.value == null }.map { it.index }.first()
        .let { WINNING_COMBINATIONS[winningCombination].second[it] }

    } else null
  }

fun getComputerMove(game: GameState): Move {

  // optimization

  // select tile such that that player get blocked
  // select tile such that that computer get
  // find

  return Move(getAvailableTiles(game).random(), game.player)
}

