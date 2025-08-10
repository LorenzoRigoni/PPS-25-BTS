package utils

import java.awt.Toolkit

object GUIConstants:
  val screenWidth: Int  = Toolkit.getDefaultToolkit.getScreenSize.width
  val screenHeight: Int = Toolkit.getDefaultToolkit.getScreenSize.height
  val RULES             =
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
    | - FAST CALC: Enter the result of the displayed expressions, calculated mentally.
    |
    | - WORD COUNT: Enter the number of displayed words as quickly as possible.
    |
    | - RIGHT DIRECTIONS: Use the WASD keys to follow the indicated directions.
    |
    | - COLORED COUNT: Determine how many numbers of the requested color are shown.
    |
    | - WORD MEMORY: You will have 10 seconds to memorize the displayed words. After this time, the words will be hidden, and you must enter the words you remember. Be careful to spell the words correctly.
    |""".stripMargin
