# Tic Tac Toe

This is a kotlin implementation of the game `Tic Tac Toe`.
Where two players ('x' and 'o') compete with each other to win the game.

## Side Effects

**IO operations:**

- Read Input
- Write Input

**State Maintenance:**

- Board: tracking the 'tiles' at position and checking end of the game
- Player: tracking whose turn it is?

## How does the game end?

- Either all moves are exhausted which results into a 'tie'
- Or a player wins by claiming 3 tiles of a single row or a column or either of the diagonals of the board

## Common Terms

- Current Player: Either 'x' or 'o'
- Board: Grid of 3 rows and 3 columns
- Cell or a Tile: a slot on the board for a player to choose
- Position: coordinate of a tile on the board represented as (index, index)
- Move: In the player's turn, he/she choose a position to claim

## Play

- To play against a player just execute the jar
- To play against a player execute the jar with additional arg to the command, i.e., `play-vs-computer`

## Assumptions:

- Game Play: the first player is always ❌ v/s the second player is always ⭕
- Player v/s Computer: always player will play first
