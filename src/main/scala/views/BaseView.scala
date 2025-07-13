package views

import java.awt.Font
import java.io.File
import javax.swing.*
import java.awt.*

trait BaseView:
  val pixelFontBig: Font =
    Font.createFont(Font.TRUETYPE_FONT, new File("src\\main\\resources\\font\\PixelFont.ttf")).deriveFont(25f)

  val pixelFontSmall: Font =
    Font.createFont(Font.TRUETYPE_FONT, new File("src\\main\\resources\\font\\PixelFont.ttf")).deriveFont(15f)

  val customBlueColor = new Color(120, 180, 210)
  val whiteColor: Color = Color.WHITE

  def centerFrame(frame: JFrame, width: Int, height: Int): Unit =
    val screenSize = Toolkit.getDefaultToolkit.getScreenSize
    val x = (screenSize.width - width) / 2
    val y = (screenSize.height - height) / 2
    frame.setSize(width, height)
    frame.setLocation(x, y)
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
    frame.setResizable(false)

  def createStyledButton(text: String, size: Dimension, font: Font, background: Color = Color.BLUE, foreground: Color = Color.WHITE): JButton =
    val button = new JButton(text)
    button.setPreferredSize(size)
    button.setMinimumSize(size)
    button.setMaximumSize(size)
    button.setFont(font)
    button.setBackground(background)
    button.setForeground(foreground)
    button