package com.tagtech.ttt

import arrow.core.Tuple9

val EMPTY_MAP = mapOf<String, Any>()

// --- Player ---

sealed class Player {

  override fun toString(): String = getSymbol()

  fun getSymbol() =
    when (this) {
      is PlayerCross -> " ❌ "
      is PlayerOhh -> " ⭕ "
    }
}

object PlayerCross : Player()
object PlayerOhh : Player()

fun Player.getName() =
  when (this) {
    is PlayerCross -> "Player 1"
    is PlayerOhh -> "Player 2"
  }

// --- Fault ---

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

// --- Fault ---

enum class EventType {
  INIT,
  UPDATE_BOARD,
  SWITCH_USER,
  GAME_END
}

data class Event(val type: EventType, val move: Move?, val winner: Player?) {

  override fun toString(): String =
    " - ${type}"
      .let { if (move != null) "${it} | ${move}" else it }
      .let {
        if (type == EventType.GAME_END) {
          if (winner != null) "${it} | ${winner.getName()} (${winner.getSymbol()}) is the winner!"
          else "${it} | No player won the game. It's a tie!"
        } else it
      }
}

// --- GameState ---

data class GameState(
  val board: BoardState = EMPTY_BOARD,
  val vsComputer: Boolean,
  val player: Player = PlayerCross,
  val ended: Boolean = false,
  val winner: Player? = null,
  val _events: List<Event> = listOf(),
) {

  override fun toString(): String =
    """
    Game State:
    ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    Board: ${board}
    v/s Computer?: ${if (vsComputer) "Yes" else "No"}
    Current Player: ${player}
    Winner: ${winner?.getName() ?: "-"}
    Ended: ${if (ended) "Yes" else "No"}
    Events:
    ${_events.joinToString(separator = "\n\t")}
    ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    """
}

// --- Move ---

data class Move(val tileNumber: Int, val player: Player) {

  override fun toString() = "'${player.getName()}' marked tile #${tileNumber} with ${player.getSymbol()}"
}

// --- BoardState ---

typealias BoardState = Tuple9<Player?, Player?, Player?, Player?, Player?, Player?, Player?, Player?, Player?>

val EMPTY_BOARD: BoardState = Tuple9(null, null, null, null, null, null, null, null, null)

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

