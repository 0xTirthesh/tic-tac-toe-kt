package com.tagtech.ttt

import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

class TestComputerPlay {
  companion object {

    private val log = LoggerFactory.getLogger(TestComputerPlay::class.java)
  }

  private fun playAgainstComputer(game: GameState, playerTileSelection: Int, expectedTilesLeft: Int = 7): GameState =
    executeTurn(game, playerTileSelection)
      .fold({ fail("Err! Unexpected Error") }) {
        if (it.ended) it
        else {
          val nextSelection = getAvailableTiles(it).apply { assertEquals(expectedTilesLeft, size) }.random()
          playAgainstComputer(it, nextSelection, expectedTilesLeft - 2)
        }
      }

  @Test
  fun testGameWinner() {
    (1..100).forEach {
      println("\n--------------- #${it} ---------------\n")
      val newGame = initGame(playingAgainstComputer = true)
      getAvailableTiles(newGame).apply { assertEquals(9, size) }
      val gameEnded = playAgainstComputer(newGame, 1)
      assertTrue(gameEnded.ended)
    }
  }
}

