package com.tagtech.ttt

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

class TestComputerPlay {

  private fun playAgainstComputer(game: GameState, playerTileSelection: Int): GameState =
    executeTurn(game, playerTileSelection)
      .fold({ fail("Err! Unexpected Error") }) {
        if (it.ended) it
        else {
          val nextSelection = getAvailableTiles(it).random()
          playAgainstComputer(it, nextSelection)
        }
      }

  @Test
  fun testRandomPlayWithComputer() {
    val noOfIterations = 100
    val result = listOf(true, false).map { computerPlaysRandom ->
      var totalWins = 0
      (1..noOfIterations).forEach {
        println("\n--------------- #${it} ---------------\n")
        val newGame = initGame(vsComputer = true, computerPlaysRandom = computerPlaysRandom)
        getAvailableTiles(newGame).apply { assertEquals(9, size) }
        playAgainstComputer(newGame, (1..9).random()).apply { assertTrue(ended) }
          .apply { if (this.winner == PlayerOhh) totalWins += 1 }
          .let(::println)
      }
      totalWins
    }

    println(
      """
      ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      Total Computer Wins (of ${noOfIterations} iterations):
         - Random: ${result[0]}
         - Smart: ${result[1]}
      ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    """.trimIndent()
    )
  }

  @Test
  fun testComputerMoveForBlocking() {
    val cases =
      listOf(
        BoardState(PlayerCross, PlayerCross, null, null, null, null, null, null, null) to 3, // row
        BoardState(PlayerCross, null, PlayerCross, null, null, null, null, null, null) to 2, // row with middle missing
        BoardState(PlayerCross, null, null, PlayerCross, null, null, null, null, null) to 7, // column
        BoardState(PlayerCross, null, null, null, PlayerCross, null, null, null, null) to 9, // diagonal
      )
    cases.forEach {
      val newGame = GameState(board = it.first, vsComputer = false, computerPlaysRandom = null)
      assertEquals(it.second, getCriticalTile(newGame, PlayerCross))
    }
  }
}

