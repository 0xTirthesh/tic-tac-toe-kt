package com.tagtech.ttt

import arrow.core.computations.either
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.test.fail

class TestGame {
  companion object {

    private val log = LoggerFactory.getLogger(TestGame::class.java)
  }

  @Test
  fun testGameWinner() {
    val result =
      either.eager<Fault, GameState> {
        initGame()
          .let { executeTurn(it, 1).bind() }
          .let { executeTurn(it, 2).bind() }
          .let { executeTurn(it, 3).bind() }
          .let { executeTurn(it, 4).bind() }
          .let { executeTurn(it, 5).bind() }
          .let { executeTurn(it, 6).bind() }
          .let { executeTurn(it, 7).bind() }
      }

    result.fold({ fail("Err! ${it.message}") }, {
      val expectedBoard =
        BoardState(PlayerCross, PlayerOhh, PlayerCross, PlayerOhh, PlayerCross, PlayerOhh, PlayerCross, null, null)

      assertEquals(expectedBoard, it.board)
      assertTrue(it.ended)
      assertNotNull(it.winner)
      assertEquals(PlayerCross, it.player)
    })
  }

  @Test
  fun testTie() {
    val result =
      either.eager<Fault, GameState> {
        initGame()
          .let { executeTurn(it, 5).bind() } // x
          .let { executeTurn(it, 1).bind() } // o
          .let { executeTurn(it, 2).bind() } // x
          .let { executeTurn(it, 8).bind() } // o
          .let { executeTurn(it, 3).bind() } // x
          .let { executeTurn(it, 7).bind() } // o
          .let { executeTurn(it, 9).bind() } // x
          .let { executeTurn(it, 6).bind() } // o
          .let { executeTurn(it, 4).bind() } // x
      }

    result.fold({ fail("Err! ${it.message}") }, {
      val expectedBoard =
        BoardState(PlayerOhh, PlayerCross, PlayerCross, PlayerCross, PlayerCross, PlayerOhh, PlayerOhh, PlayerOhh, PlayerCross)

      assertEquals(expectedBoard, it.board)
      assertTrue(it.ended)
      assertNull(it.winner)
    })
  }
}
