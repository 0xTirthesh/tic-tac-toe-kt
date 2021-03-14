package com.tagtech.ttt

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class TestStateUpdates {

  @Test
  fun testBoardUpdate() {
    val board = BoardState(null, null, null, null, null, null, null, null, null)
    val move = Move(1, PlayerCross)
    val updatedBoard = updateBoard(board, move)
    assertNotNull(updatedBoard)
    assertEquals(BoardState(PlayerCross, null, null, null, null, null, null, null, null), updatedBoard)
  }

  @Test
  fun testMakeMove() {
    val game = initGame()
    assertEquals(1, game._events.size)

    val move = Move(1, PlayerCross)
    val updatedGame = makeMove(game, move)

    assertEquals(2, updatedGame._events.size)
    assertNull(updatedGame.winner)
    assertFalse(updatedGame.ended)

    val expectedGame = GameState(
      board = BoardState(PlayerCross, null, null, null, null, null, null, null, null),
      player = PlayerCross,
      _events = listOf(
        Event(EventType.INIT, null, null),
        Event(EventType.UPDATE_BOARD, move, null),
      )
    )

    assertEquals(expectedGame, updatedGame)
  }

  @Test
  fun testPlayRound() {
    val game = initGame()
    val move = Move(5, PlayerCross)
    val result = executeTurn(game, move.tileNumber)
    assertTrue(result.isRight(), "Unexpected Fault Encountered")

    result.fold({}, { updatedGame ->

      assertEquals(3, updatedGame._events.size)
      assertNull(updatedGame.winner)
      assertFalse(updatedGame.ended)

      val expectedGame = GameState(
        board = BoardState(null, null, null, null, PlayerCross, null, null, null, null),
        player = PlayerOhh,
        _events = listOf(
          Event(EventType.INIT, null, null),
          Event(EventType.UPDATE_BOARD, move, null),
          Event(EventType.SWITCH_USER, null, null),
        )
      )

      assertEquals(expectedGame, updatedGame)
    })
  }

  @Test
  fun testSwitchPlayer() {
    val game = initGame()
    val updatedGame = switchPlayer(game)

    assertEquals(2, updatedGame._events.size)
    assertNull(updatedGame.winner)
    assertFalse(updatedGame.ended)

    val expectedGame = GameState(
      board = BoardState(null, null, null, null, null, null, null, null, null),
      player = PlayerOhh,
      _events = listOf(
        Event(EventType.INIT, null, null),
        Event(EventType.SWITCH_USER, null, null),
      )
    )

    assertEquals(expectedGame, updatedGame)
  }
}
