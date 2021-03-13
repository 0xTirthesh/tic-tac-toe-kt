package com.tagtech.ttt

fun initGame(): GameState {
  return GameState(emptyBoard, Cross)
}

fun validateMove(game: GameState, move: Move): GameState = TODO()

fun makeMove(game: GameState, move: Move): GameState = TODO()

fun getValue(game: GameState, position: Position): Cell = TODO()

fun switchPlayer(player: Player): Player =
  when (player) {
    is Cross -> Ohh
    is Ohh -> Cross
  }

fun checkForWinner(game: GameState): Boolean = TODO()

fun playRound(): Unit = TODO()
