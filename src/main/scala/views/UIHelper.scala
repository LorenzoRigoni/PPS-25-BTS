package views

import utils.constants.GUIConstants.*
import utils.enums.MiniGames

import java.awt.Font
import javax.swing.*
import java.awt.*

/**
 * Utility object for creating and managing common UI components.
 */
object UIHelper:
  private def loadIcon(name: String, size: Int): ImageIcon =
    val url     = getClass.getResource(s"/" + name)
    val image   = new ImageIcon(url).getImage
    val resized = image.getScaledInstance(size, size, Image.SCALE_SMOOTH)
    new ImageIcon(resized)

  private def findTextField(c: Component): Option[JTextField] = c match
    case tf: JTextField => Some(tf)
    case p: JPanel      =>
      p.getComponents.iterator.map(findTextField).collectFirst { case Some(tf) => tf }
    case _              => None

  /**
   * Set the frame in the center of the panel.
   * @param frame
   *   the frame to set
   * @param divisor
   *   the divisor to calculate frame size relative to screen dimensions
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
   * Create a button with the predefined style.
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
   * Calculates a responsive icon size based on the screen width.
   * @param divisor
   *   the value to divide the screen width
   * @return
   *   the calculated icon size in pixels
   */
  def getResponsiveIconSize(divisor: Int): Int =
    (SCREEN_WIDTH / divisor.toDouble).toInt

  /**
   * Centers a panel inside a container panel and optionally sets focus on the first JTextField.
   * @param centerPanel
   *   the container panel
   * @param panel
   *   the panel to center
   * @param miniGame
   *   optional mini-game type to determine focus behavior
   */
  def centerPanel(centerPanel: JPanel, panel: JPanel, miniGame: Option[MiniGames]): Unit =
    SwingUtilities.invokeLater { () =>
      centerPanel.removeAll()
      centerPanel.add(panel, BorderLayout.CENTER)
      centerPanel.revalidate()
      centerPanel.repaint()
      if miniGame.isDefined && miniGame.get != MiniGames.RightDirections then
        findTextField(panel).foreach(tf =>
          SwingUtilities.invokeLater(() => tf.requestFocusInWindow())
        )
    }

  /**
   * Creates a JLabel with a specified font and optional center alignment.
   * @param text
   *   the text to display
   * @param font
   *   the font to apply
   * @param center
   *   whether to center the text horizontally (default true)
   * @return
   *   the created JLabel
   */
  def createLabel(text: String, font: Font, center: Boolean = true): JLabel =
    val label = new JLabel(text)
    label.setFont(font)
    if center then label.setHorizontalAlignment(SwingConstants.CENTER)
    label

  /**
   * Creates a panel representing a row in a result summary, including an icon and text.
   * @param iconName
   *   the name of the icon file
   * @param text
   *   the text to display next to the icon
   * @param iconSize
   *   the size of the icon in pixels
   * @return
   *   a JPanel containing the icon and text horizontally
   */
  def createResultRow(iconName: String, text: String, iconSize: Int): JPanel =
    val ROW_SPACE = 10
    val rowPanel  = new JPanel(new FlowLayout(FlowLayout.CENTER))
    val iconLabel = new JLabel(loadIcon(iconName, iconSize))
    val textLabel = new JLabel(text)
    rowPanel.setOpaque(false)
    textLabel.setFont(PIXEL_FONT15)
    rowPanel.add(iconLabel)
    rowPanel.add(Box.createRigidArea(new Dimension(ROW_SPACE, 0)))
    rowPanel.add(textLabel)
    rowPanel
