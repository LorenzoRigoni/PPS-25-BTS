package views

import javax.swing.*
import java.awt.*

object BrainTraining extends BaseView:

  def show(gamePanels: GamePanels): Unit =
    val frame = new JFrame("Brain Testing")
    val frameWidth = 500
    val frameHeight = 600
    val buttonDimension = new Dimension(300,50)
    val mainPanel = new JPanel(new BorderLayout())
    val buttonPanel = new JPanel()
    val centerPanel = new JPanel()

    frame.setSize(frameWidth, frameHeight)
    frame.setBackground(whiteColor)
    centerFrame(frame, frameWidth, frameHeight)

    val buttons = Seq(
      createStyledButton("Fast Calc", buttonDimension, pixelFontSmall, customBlueColor, whiteColor) -> gamePanels.fastCalcPanel _,
      createStyledButton("Count Words", buttonDimension, pixelFontSmall, customBlueColor, whiteColor) -> gamePanels.countWordsPanel _,
      createStyledButton("Right Directions", buttonDimension, pixelFontSmall, customBlueColor, whiteColor) -> gamePanels.rightDirectionsPanel _
    )

    centerPanel.setLayout(new BorderLayout())

    buttons.foreach((button, panelSupplier) => {
      button.addActionListener(_ => {
        mainPanel.remove(buttonPanel)
        centerPanel.removeAll()
        centerPanel.add(panelSupplier(), BorderLayout.CENTER)
        centerPanel.revalidate()
        centerPanel.repaint()
      })
      button.setAlignmentX(Component.CENTER_ALIGNMENT)
      buttonPanel.add(Box.createVerticalStrut(40))
      buttonPanel.add(button)
    })

    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS))

    mainPanel.add(buttonPanel, BorderLayout.NORTH)
    mainPanel.add(centerPanel, BorderLayout.CENTER)
    frame.setContentPane(mainPanel)
    frame.setVisible(true)