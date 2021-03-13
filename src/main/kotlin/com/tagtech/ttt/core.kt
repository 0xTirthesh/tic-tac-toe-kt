package com.tagtech.ttt

import arrow.core.None
import arrow.core.Option
import arrow.core.Tuple3

sealed class Player {}

object Cross : Player()
object Ohh : Player()

enum class Index(val idx: Int) {
  One(1),
  Two(2),
  Three(3)
}

typealias Cell = Option<Player>
typealias Row = Tuple3<Cell, Cell, Cell>
typealias Board = Tuple3<Row, Row, Row>
typealias Position = Pair<Index, Index>

typealias Sequence = (Board) -> Tuple3<Cell, Cell, Cell>

data class AppError(val message: String)
data class GameState(val board: Board, val player: Player)
data class Move(val coordinate: Position, val player: Player)

val emptyBoard: Board = Tuple3(
  Tuple3(None, None, None),
  Tuple3(None, None, None),
  Tuple3(None, None, None)
)

val slots = sequence {
  Index.values().forEach { rowPos ->
    Index.values().forEach { colPos ->
      yield(Position(rowPos, colPos))
    }
  }
}

val ROW_1: Sequence = { it.a }
val ROW_2: Sequence = { it.b }
val ROW_3: Sequence = { it.c }

val COL_1: Sequence = { Tuple3(it.a.a, it.b.a, it.c.a) }
val COL_2: Sequence = { Tuple3(it.a.b, it.b.b, it.c.b) }
val COL_3: Sequence = { Tuple3(it.a.c, it.b.c, it.c.c) }

val DIAG_1: Sequence = { Tuple3(it.a.a, it.b.b, it.c.c) }
val DIAG_2: Sequence = { Tuple3(it.a.c, it.b.b, it.c.a) }


