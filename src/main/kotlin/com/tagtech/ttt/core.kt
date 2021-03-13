package com.tagtech.ttt

import arrow.core.Tuple9

val EMPTY_MAP = mapOf<String, Any>()
val EMPTY_BOARD: BoardState = Tuple9(null, null, null, null, null, null, null, null, null)

sealed class Player
object Cross : Player()
object Ohh : Player()

typealias BoardState = Tuple9<Player?, Player?, Player?, Player?, Player?, Player?, Player?, Player?, Player?>

enum class FaultType {
  SYSTEM,
  INVALID_INPUT,

}

data class Fault(
  val message: String,
  val type: FaultType,
  val args: Map<String, Any> = EMPTY_MAP,
  val ex: Throwable? = null
)

enum class EventType {
  INIT,
  UPDATE_BOARD,
  SWITCH_USER,
  GAME_END
}

data class Event(val eventType: EventType, val move: Move?, val winner: Player?)

data class GameState(
  val board: BoardState = EMPTY_BOARD,
  val player: Player = Cross,
  val ended: Boolean = false,
  val winner: Player? = null,
  val _events: List<Event> = listOf(),
)

data class Move(val tileNumber: Int, val player: Player)

fun BoardState.getEndGameValidatorSequence() =
  listOf(
    listOf(a, b, c), // row 1
    listOf(d, e, f), // row 2
    listOf(g, h, i), // row 3
    listOf(a, d, g), // column 1
    listOf(b, e, h), // column 2
    listOf(c, f, i), // column 3
    listOf(a, e, i), // diagonal 1
    listOf(c, e, g), // diagonal 2
  )

fun BoardState.getAllTiles() = listOf(a, b, c, d, e, f, g, h, i)
