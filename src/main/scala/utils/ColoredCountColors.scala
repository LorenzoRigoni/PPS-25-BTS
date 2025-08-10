package utils

import java.awt.Color

enum ColoredCountColors(val color: Color):
  case RED    extends ColoredCountColors(Color.RED)
  case YELLOW extends ColoredCountColors(Color.YELLOW)
  case BLACK  extends ColoredCountColors(Color.BLACK)
  case BLUE   extends ColoredCountColors(Color.BLUE)
