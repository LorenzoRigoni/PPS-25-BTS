package utils

/**
 * This enum represents the possible mini-games.
 *
 * @param displayName
 *   the name of the mini-game
 */
enum MiniGames(val displayName: String):
  case FastCalc        extends MiniGames("Fast Calc")
  case CountWords      extends MiniGames("Count Words")
  case RightDirections extends MiniGames("Right Directions")
  case ColoredCount    extends MiniGames("Colored Count")
  case WordMemory      extends MiniGames("Word Memory")
