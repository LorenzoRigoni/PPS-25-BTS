package views

import controllers.GameController
import views.panels.{BackgroundImagePanel, GamePanelsImpl, ResultPanelsImpl}
import utils.GUIConstants.*

import javax.swing.*
import java.awt.*

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
    val size       = Math.min(screenWidth, screenHeight) / 2
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
    buttonPanel.setOpaque(false)

    val verticalPanel = new JPanel()
    verticalPanel.setLayout(new BoxLayout(verticalPanel, BoxLayout.Y_AXIS))
    verticalPanel.setOpaque(false)

    val buttonsData = Seq(
      (
        "Age Test",
        () => {
          frame.dispose()
          AgeTest(GamePanelsImpl(), ResultPanelsImpl()).show()
        }
      ),
      (
        "Training",
        () => {
          frame.dispose()
          BrainTraining(ResultPanelsImpl()).show(GamePanelsImpl())
        }
      ),
      ("Game Rules", () => showGameRulesDialog())
    )

    val components = for ((btnData, idx) <- buttonsData.zipWithIndex) yield {
      val button =
        createStyledButton(btnData._1, buttonSize, pixelFont25, customBlueColor, whiteColor)
      button.addActionListener(_ => btnData._2())
      val strut  =
        if (idx < buttonsData.size - 1) Box.createVerticalStrut(30)
        else Box.createVerticalStrut(120)
      Seq(button, strut)
    }
    components.flatten.foreach(verticalPanel.add)

    buttonPanel.add(verticalPanel)
    backgroundPanel.add(buttonPanel, BorderLayout.SOUTH)

    frame.getContentPane.add(backgroundPanel)
    frame.setVisible(true)
