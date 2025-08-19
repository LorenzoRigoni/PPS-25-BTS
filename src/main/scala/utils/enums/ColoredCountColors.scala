package utils.enums

import java.awt.Color

/**
 * This enum represents the possible color of the numbers in the mini-game "Colored Count".
 *
 * @param color
 *   The color for the GUI
 */
enum ColoredCountColors(val color: Color):
  case RED    extends ColoredCountColors(Color.RED)
  case YELLOW extends ColoredCountColors(Color.YELLOW)
  case BLACK  extends ColoredCountColors(Color.BLACK)
  case BLUE   extends ColoredCountColors(Color.BLUE)
