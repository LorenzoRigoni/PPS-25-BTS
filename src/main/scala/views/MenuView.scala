package views

import javax.swing.*
import java.awt.*
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import javax.swing.WindowConstants.EXIT_ON_CLOSE

object MenuView:
  private val frame = new JFrame("Menù")
  private val pixelFont: Font =
    Font.createFont(Font.TRUETYPE_FONT, new File("src\\main\\resources\\font\\PixelFont.ttf")).deriveFont(25f)

  private class BackgroundImagePanel(imagePath: String) extends JPanel:
    private val image: BufferedImage =
      val file = new File(imagePath)
      if !file.exists() then
        throw new RuntimeException(s"File not found: ${file.getAbsolutePath}")
      ImageIO.read(file)

    override def paintComponent(g: Graphics): Unit =
      super.paintComponent(g)
      g.drawImage(image, 0,0, this)

  private def setButtonStyle(text: String, size: Dimension, font: Font, backgroundColor: Color, foregroundColor: Color): JButton =
    val button = new JButton(text)
    button.setPreferredSize(size)
    button.setMinimumSize(size)
    button.setMaximumSize(size)
    button.setFont(font)
    button.setBackground(backgroundColor)
    button.setForeground(foregroundColor)
    button

  def show(): Unit =
    val screenSize = Toolkit.getDefaultToolkit.getScreenSize
    val frameWidth = 1024
    val frameHeight = 1024
    val x = (screenSize.width - frameWidth) / 2
    val y = (screenSize.height - frameHeight) / 2
    val buttonSize = new Dimension(400, 80)
    val bgColor = new Color(120, 180, 210)
    val fgColor = Color.WHITE

    frame.setSize(frameWidth, frameHeight)
    frame.setLocation(x, y)
    frame.setDefaultCloseOperation(EXIT_ON_CLOSE)
    frame.setResizable(false)

    val backgroundPanel = new BackgroundImagePanel("src\\main\\resources\\MenuBackgroundImage.png")
    backgroundPanel.setLayout(new BorderLayout())

    val buttonPanel = new JPanel()
    buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT))

    val verticalButtons = new JPanel()
    verticalButtons.setLayout(new BoxLayout(verticalButtons, BoxLayout.Y_AXIS))
    verticalButtons.setOpaque(false)

    val brainAgingButton = setButtonStyle("Age Test", buttonSize, pixelFont, bgColor, fgColor)
    val brainTrainingButton = setButtonStyle("Brain Training", buttonSize, pixelFont, bgColor, fgColor)

    brainAgingButton.addActionListener(_ => {
      frame.dispose()
      MainView.show(GamePanels)
    })

    brainTrainingButton.addActionListener(_ =>
      frame.dispose()
      BrainTraining.show(GamePanels)
    )

    verticalButtons.add(brainAgingButton)
    verticalButtons.add(Box.createVerticalStrut(30))
    verticalButtons.add(brainTrainingButton)
    verticalButtons.add(Box.createVerticalStrut(120))

    buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 30))

    buttonPanel.setOpaque(false)
    buttonPanel.add(verticalButtons)
    backgroundPanel.add(buttonPanel, BorderLayout.SOUTH)

    frame.getContentPane.add(backgroundPanel)
    frame.setVisible(true)