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
  fun testRandomPlayWithComputer() {
    (1..100).forEach {
      println("\n--------------- #${it} ---------------\n")
      val newGame = initGame(playingAgainstComputer = true)
      getAvailableTiles(newGame).apply { assertEquals(9, size) }
      playAgainstComputer(newGame, 1).apply { assertTrue(ended) }.let(::println)
    }
  }

  @Test
  fun testComputerMove() {
    val cases =
      listOf(
        BoardState(PlayerCross, PlayerCross, null, null, null, null, null, null, null) to 3, // row
        BoardState(PlayerCross, null, PlayerCross, null, null, null, null, null, null) to 2, // row with middle missing
        BoardState(PlayerCross, null, null, PlayerCross, null, null, null, null, null) to 7, // column
        BoardState(PlayerCross, null, null, null, PlayerCross, null, null, null, null) to 9, // diagonal
      )
    cases.forEach {
      val newGame = GameState(board = it.first)
      assertEquals(it.second, getTileWhichBlocksPlayersWin(newGame))
    }
  }
}

