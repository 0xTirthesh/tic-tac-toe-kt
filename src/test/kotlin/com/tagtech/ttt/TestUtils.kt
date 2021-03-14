package com.tagtech.ttt

import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import kotlin.test.assertTrue

class TestUtils {
  companion object {

    private val log = LoggerFactory.getLogger(TestUtils::class.java)
  }

  @Test
  fun testValidateMovePositive() {
    val game = initGame()
    val result = executeTurn(game, 5)
    assertTrue(result.isRight())
  }

  @Test
  fun testValidateMoveNegative() {
    val game = initGame()
    val result = executeTurn(game, 10)
    assertTrue(result.isLeft())
  }
}
