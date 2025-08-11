package views

import utils.GUIConstants.*

import java.awt.Font
import javax.swing.*
import java.awt.*

/**
 * This trait represents the base view. It is extended by AgeTest view and BrainTrainingView
 */
trait BaseView:
  /**
   * Set the frame in the center of the panel.
   *
   * @param frame
   *   the frame to set
   * @param divisor
   *   the value of divisor
   */
  def centerFrame(frame: JFrame, divisor: Float): Unit =
    val screenSize = (Math.min(SCREEN_WIDTH, SCREEN_HEIGHT) / divisor).toInt
    val x          = (SCREEN_WIDTH - screenSize) / HALF_DIVISOR
    val y          = (SCREEN_HEIGHT - screenSize) / HALF_DIVISOR
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
      background: Color = CUSTOM_BLUE,
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

  /**
   * Calculate the right size for icon considering screenwidth
   * @param divisor
   * @return
   */
  def getResponsiveIconSize(divisor: Int): Int =
    (SCREEN_WIDTH / divisor.toDouble).toInt

  def centerPanel(centerPanel: JPanel, panel: JPanel): Unit =
    SwingUtilities.invokeLater { () =>
      centerPanel.removeAll()
      centerPanel.add(panel, BorderLayout.CENTER)
      centerPanel.revalidate()
      centerPanel.repaint()
    }
