package utils.constants

import java.awt.{Color, Font, Toolkit}
import java.io.File

/**
 * This object contains the constants for the GUI
 */
object GUIConstants:
  val HALF_DIVISOR                = 2
  val CENTER_FRAME_DIVISOR: Float = 1.5
  val HOME_BUTTON_W               = 120
  val HOME_BUTTON_H               = 30
  val BORDER_VALUE                = 10
  val BUTTON_DISTANCE             = 30
  val SCREEN_WIDTH: Int           = Toolkit.getDefaultToolkit.getScreenSize.width
  val SCREEN_HEIGHT: Int          = Toolkit.getDefaultToolkit.getScreenSize.height
  val CUSTOM_BLUE                 = new Color(120, 180, 210)

  private def loadFont(resourcePath: String, size: Float): Font =
    val is = getClass.getResourceAsStream(resourcePath)
    require(is != null, s"Font resource not found: $resourcePath")
    Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(size)

  val PIXEL_FONT70: Font = loadFont("/font/PixelFont.ttf", 70f)

  val PIXEL_FONT25: Font = loadFont("/font/PixelFont.ttf", 25f)

  val PIXEL_FONT15: Font = loadFont("/font/PixelFont.ttf", 15f)

  val PIXEL_FONT8: Font = loadFont("/font/PixelFont.ttf", 8f)

  val RULES: String =
    """
    | --GAME RULES--
    |
    |Brain Training Scala features two game modes: Age Test and Training.
    |
    |In the first mode, the player will complete three mini-games. At the end, they will be shown an estimate of their brain age, based on the mistakes made and the time taken to complete the games.
    |
    |The Training mode allows the player to choose any of the mini-games they wish to practice.
    |
    |Mini games:
    |
    |FAST CALC: Enter the result of the displayed expressions, calculated mentally.
    |
    |WORD COUNT: Enter the number of displayed words as quickly as possible.
    |
    |RIGHT DIRECTIONS: Use the WASD keys to follow the indicated directions.
    |
    |COLORED COUNT: Determine how many numbers of the requested color are shown.
    |
    |WORD MEMORY: You will have 10 seconds to memorize the displayed words. After this time, the words will be hidden, and you must enter the words you remember. Be careful to spell the words correctly.
    |""".stripMargin
