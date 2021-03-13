package com.tagtech.ttt

import arrow.core.Option

fun displayWelcomeMessage(): Unit = TODO()

fun promptInput(): Unit = TODO()

fun displayBoard(): Unit = TODO()

fun declareWinner(): Unit = TODO()

fun getSymbol(player: Option<Player>): String =
  player.fold({ " - " }) {
    when (it) {
      is Cross -> " x "
      is Ohh -> " o "
    }
  }
