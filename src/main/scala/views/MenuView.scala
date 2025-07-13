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


  def show(): Unit =
    frame.setSize(1024, 1024)
    frame.setDefaultCloseOperation(EXIT_ON_CLOSE)
    frame.setResizable(false)
    val backgroundPanel = new BackgroundImagePanel("src\\main\\resources\\MenuBackgroundImage.png")
    backgroundPanel.setLayout(new BorderLayout())

    val buttonPanel = new JPanel()
    buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT))

    val verticalButtons = new JPanel()
    verticalButtons.setLayout(new BoxLayout(verticalButtons, BoxLayout.Y_AXIS))
    verticalButtons.setOpaque(false)

    val brainAgingButton = new JButton("Age Test")
    val brainTrainingButton = new JButton("Brain Training")
    val buttonSize = new Dimension(400,80)

    brainAgingButton.setPreferredSize(buttonSize)
    brainAgingButton.setMinimumSize(buttonSize)
    brainAgingButton.setMaximumSize(buttonSize)
    brainAgingButton.setFont(pixelFont)
    brainAgingButton.setBackground(new Color(120, 180, 210))
    brainAgingButton.setForeground(Color.WHITE)


    brainTrainingButton.setPreferredSize(buttonSize)
    brainTrainingButton.setMinimumSize(buttonSize)
    brainTrainingButton.setMaximumSize(buttonSize)
    brainTrainingButton.setFont(pixelFont)
    brainTrainingButton.setBackground(new Color(120, 180, 210))
    brainTrainingButton.setForeground(Color.WHITE)


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