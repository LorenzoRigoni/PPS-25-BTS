package views

import java.awt.Font
import java.io.File
import javax.swing.*
import java.awt.*

/**
 * This trait represents the base view. It is extended by AgeTest view and BrainTrainingView
 */
trait BaseView:
  val pixelFont25: Font =
    Font
      .createFont(Font.TRUETYPE_FONT, new File("src\\main\\resources\\font\\PixelFont.ttf"))
      .deriveFont(25f)

  val pixelFont15: Font =
    Font
      .createFont(Font.TRUETYPE_FONT, new File("src\\main\\resources\\font\\PixelFont.ttf"))
      .deriveFont(15f)

  val pixelFont8: Font =
    Font
      .createFont(Font.TRUETYPE_FONT, new File("src\\main\\resources\\font\\PixelFont.ttf"))
      .deriveFont(8f)

  val customBlueColor   = new Color(120, 180, 210)
  val whiteColor: Color = Color.WHITE

  private val screenWidth: Int  = Toolkit.getDefaultToolkit.getScreenSize.width
  private val screenHeight: Int = Toolkit.getDefaultToolkit.getScreenSize.height

  /**
   * Set the frame in the center of the panel.
   *
   * @param frame
   *   the frame to set
   * @param divisor
   *   the value of divisor
   */
  def centerFrame(frame: JFrame, divisor: Float): Unit =
    val screenSize = (Math.min(screenWidth, screenHeight) / divisor).toInt
    val x          = (screenWidth - screenSize) / 2
    val y          = (screenHeight - screenSize) / 2
    frame.setSize(screenSize, screenSize)
    frame.setLocation(x, y)
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
    frame.setResizable(false)

  /**
   * Create a button with the prefixed style.
   * @param text
   *   the text of the button
   * @param size
   *   the size of the button
   * @param font
   *   the font of the button
   * @param background
   *   the background color of the button
   * @param foreground
   *   the foreground color of the button
   * @return
   *   the button created
   */
  def createStyledButton(
      text: String,
      size: Dimension,
      font: Font,
      background: Color = Color.BLUE,
      foreground: Color = Color.WHITE
  ): JButton =
    val button = new JButton(text)
    button.setPreferredSize(size)
    button.setMinimumSize(size)
    button.setMaximumSize(size)
    button.setFont(font)
    button.setBackground(background)
    button.setForeground(foreground)
    button
