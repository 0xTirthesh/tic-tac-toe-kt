package com.tagtech.ttt

import arrow.core.Either
import arrow.core.computations.either
import arrow.core.left
import arrow.core.right

// --- events ---

fun initGame(): Either<Fault, GameState> =
  either.eager {
    val game = GameState()

    val events = game._events.toMutableList()
    events.add(Event(EventType.INIT, null, null))
    game.copy(_events = events)
  }

fun switchPlayer(game: GameState): Either<Fault, GameState> =
  either.eager {
    val newPlayer = when (game.player) {
      is Cross -> Ohh
      is Ohh -> Cross
    }

    val events = game._events.toMutableList()
    events.add(Event(EventType.SWITCH_USER, null, null))
    game.copy(player = newPlayer, _events = events)
  }

fun makeMove(game: GameState, move: Move): Either<Fault, GameState> =
  either.eager {
    val newBoard = updateBoard(game.board, move).bind()
    val events = game._events.toMutableList()
    events.add(Event(EventType.UPDATE_BOARD, move, null))
    game.copy(board = newBoard, _events = events)
  }

fun endGame(game: GameState, isTie: Boolean): Either<Fault, GameState> =
  either.eager {
    val events = game._events.toMutableList()
    val winner = if (isTie) null else game.player
    events.add(Event(EventType.GAME_END, null, winner))
    game.copy(ended = true, winner = winner, _events = events)
  }

// --- executor ---

fun playRound(game: GameState, move: Move): Either<Fault, GameState> =
  either.eager {
    validateMove(game, move).bind()
    val newGameState = makeMove(game, move).bind()
    val result =
      when {
        checkForTheWinner(newGameState) -> endGame(newGameState, isTie = false)
        getNoOfMovesLeft(newGameState) == 0 -> endGame(newGameState, isTie = true)
        else -> switchPlayer(newGameState)
      }
    result.bind()
  }

// --- utils ---

fun validateMove(game: GameState, move: Move): Either<Fault, Unit> =
  getValue(game, move.tileNumber)?.let { Fault("err-invalid-move", FaultType.INVALID_INPUT).left() } ?: Unit.right()

fun getValue(game: GameState, tileNumber: Int): Player? = game.board.getAllTiles()[tileNumber - 1]

fun getNoOfMovesLeft(game: GameState): Int =
  9 - game.board.getAllTiles().fold(0) { acc, elm -> elm?.let { acc + 1 } ?: acc }

fun checkForTheWinner(game: GameState): Boolean =
  game.board
    .getEndGameValidatorSequence()
    .fold(false) { acc, tileSet -> acc or tileSet.all { it != null && it == game.player } }

fun updateBoard(board: BoardState, move: Move): Either<Fault, BoardState> =
  when (move.tileNumber) {
    1 -> board.copy(a = move.player).right()
    2 -> board.copy(b = move.player).right()
    3 -> board.copy(c = move.player).right()
    4 -> board.copy(d = move.player).right()
    5 -> board.copy(e = move.player).right()
    6 -> board.copy(f = move.player).right()
    7 -> board.copy(g = move.player).right()
    8 -> board.copy(h = move.player).right()
    9 -> board.copy(i = move.player).right()
    else -> Fault("err-tile-number", FaultType.SYSTEM).left()
  }
