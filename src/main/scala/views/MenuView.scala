package views

import controllers.GameController
import utils.GUIConstants
import utils.GUIConstants.RULES
import views.panels.{BackgroundImagePanel, GamePanels, GamePanelsImpl, ResultPanelsImpl}

import javax.swing.*
import java.awt.*
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

/**
 * This object represents the initial menu where the player can choose between Age Test and Brain
 * Training mode.
 */
class MenuView(controller: GameController) extends BaseView:
  private val frame                       = new JFrame("Menù")
  private def showGameRulesDialog(): Unit =
    val textArea   = new JTextArea(RULES)
    textArea.setEditable(false)
    textArea.setLineWrap(true)
    textArea.setWrapStyleWord(true)
    textArea.setFont(pixelFont15)
    val scrollPane = new JScrollPane(textArea)
    val size       = Math.min(GUIConstants.screenWidth, GUIConstants.screenHeight) / 2
    scrollPane.setPreferredSize(new Dimension(size, size))
    JOptionPane.showMessageDialog(
      frame,
      scrollPane,
      "Game Rules",
      JOptionPane.INFORMATION_MESSAGE
    )

  /**
   * Show the Menu view.
   */
  def show(): Unit =
    centerFrame(frame, 1)
    val buttonSize =
      new Dimension((frame.getSize.width * 0.4).toInt, (frame.getSize.height * 0.08).toInt)

    val backgroundPanel = new BackgroundImagePanel("src\\main\\resources\\MenuBackgroundImage.png")
    backgroundPanel.setLayout(new BorderLayout())

    val buttonPanel = new JPanel()
    buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT))
    buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 30))

    val verticalPanel = new JPanel()
    verticalPanel.setLayout(new BoxLayout(verticalPanel, BoxLayout.Y_AXIS))
    verticalPanel.setOpaque(false)

    val brainAgingButton    =
      createStyledButton("Age Test", buttonSize, pixelFont25, customBlueColor, whiteColor)
    val brainTrainingButton =
      createStyledButton("Training", buttonSize, pixelFont25, customBlueColor, whiteColor)
    val gameRulesButton     =
      createStyledButton("Game Rules", buttonSize, pixelFont25, customBlueColor, whiteColor)

    brainAgingButton.addActionListener(_ => {
      frame.dispose()
      AgeTest.apply(GamePanelsImpl(), ResultPanelsImpl()).show()
    })

    brainTrainingButton.addActionListener(_ =>
      frame.dispose()
      BrainTraining.apply(ResultPanelsImpl()).show(GamePanelsImpl())
    )
    gameRulesButton.addActionListener(_ => showGameRulesDialog())

    verticalPanel.add(brainAgingButton)
    verticalPanel.add(Box.createVerticalStrut(30))
    verticalPanel.add(brainTrainingButton)
    verticalPanel.add(Box.createVerticalStrut(30))
    verticalPanel.add(gameRulesButton)
    verticalPanel.add(Box.createVerticalStrut(120))

    buttonPanel.setOpaque(false)
    buttonPanel.add(verticalPanel)
    backgroundPanel.add(buttonPanel, BorderLayout.SOUTH)

    frame.getContentPane.add(backgroundPanel)
    frame.setVisible(true)
