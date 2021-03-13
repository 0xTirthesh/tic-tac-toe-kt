package com.tagtech.ttt

import arrow.core.Tuple3

val EMPTY_MAP = mapOf<String, Any>()

sealed class Player {}

object Cross : Player()
object Ohh : Player()

enum class Index(val idx: Int) {
  One(1),
  Two(2),
  Three(3)
}

typealias Row = Tuple3<Player?, Player?, Player?>
typealias BoardState = Tuple3<Row, Row, Row>
typealias Position = Pair<Index, Index>

typealias TileSet = (BoardState) -> List<Player?>

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
  val board: BoardState = emptyBoard,
  val player: Player = Cross,
  val ended: Boolean = false,
  val winner: Player? = null,
  val _events: List<Event> = listOf(),
)

data class Move(val tileNumber: Int, val player: Player)

val emptyBoard: BoardState = Tuple3(
  Tuple3(null, null, null),
  Tuple3(null, null, null),
  Tuple3(null, null, null)
)

val slots = sequence {
  Index.values().forEach { rowPos ->
    Index.values().forEach { colPos ->
      yield(Position(rowPos, colPos))
    }
  }
}

private val ROW_1: TileSet = { listOf(it.a.a, it.a.b, it.a.c) }
private val ROW_2: TileSet = { listOf(it.b.a, it.b.b, it.b.c) }
private val ROW_3: TileSet = { listOf(it.c.a, it.c.b, it.c.c) }

private val COL_1: TileSet = { listOf(it.a.a, it.b.a, it.c.a) }
private val COL_2: TileSet = { listOf(it.a.b, it.b.b, it.c.b) }
private val COL_3: TileSet = { listOf(it.a.c, it.b.c, it.c.c) }

private val DIAG_1: TileSet = { listOf(it.a.a, it.b.b, it.c.c) }
private val DIAG_2: TileSet = { listOf(it.a.c, it.b.b, it.c.a) }

val endGameCheckExecutionSequence = listOf(ROW_1, ROW_2, ROW_3, COL_1, COL_2, COL_3, DIAG_1, DIAG_2)
val allPositions: TileSet = { listOf(it.a.a, it.a.b, it.a.c, it.b.a, it.b.b, it.b.c, it.c.a, it.c.b, it.c.c) }
