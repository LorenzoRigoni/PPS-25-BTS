package utils

enum MiniGames(val displayName: String):
  case FastCalc        extends MiniGames("Fast Calc")
  case CountWords      extends MiniGames("Count Words")
  case RightDirections extends MiniGames("Right Directions")
  case ColoredCount    extends MiniGames("Colored Count")
  case WordMemory      extends MiniGames("Word Memory")
