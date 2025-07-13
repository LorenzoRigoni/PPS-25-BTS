package views

import javax.swing.*
import java.awt.*
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

object MenuView extends BaseView:
  private val frame = new JFrame("Menù")

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
    val frameWidth = 1024
    val frameHeight = 1024
    val buttonSize = new Dimension(400, 80)
    centerFrame(frame, frameWidth, frameHeight)

    val backgroundPanel = new BackgroundImagePanel("src\\main\\resources\\MenuBackgroundImage.png")
    backgroundPanel.setLayout(new BorderLayout())

    val buttonPanel = new JPanel()
    buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT))
    buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 30))

    val verticalPanel = new JPanel()
    verticalPanel.setLayout(new BoxLayout(verticalPanel, BoxLayout.Y_AXIS))
    verticalPanel.setOpaque(false)

    val brainAgingButton = createStyledButton("Age Test", buttonSize, pixelFontBig, customBlueColor, whiteColor)
    val brainTrainingButton = createStyledButton("Brain Training", buttonSize, pixelFontBig, customBlueColor, whiteColor)

    brainAgingButton.addActionListener(_ => {
      frame.dispose()
      MainView.show(GamePanels)
    })

    brainTrainingButton.addActionListener(_ =>
      frame.dispose()
      BrainTraining.show(GamePanels)
    )

    verticalPanel.add(brainAgingButton)
    verticalPanel.add(Box.createVerticalStrut(30))
    verticalPanel.add(brainTrainingButton)
    verticalPanel.add(Box.createVerticalStrut(120))

    buttonPanel.setOpaque(false)
    buttonPanel.add(verticalPanel)
    backgroundPanel.add(buttonPanel, BorderLayout.SOUTH)

    frame.getContentPane.add(backgroundPanel)
    frame.setVisible(true)