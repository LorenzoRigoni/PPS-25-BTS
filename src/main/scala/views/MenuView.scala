package views

import controllers.GameController
import views.panels.{BackgroundImagePanel, GamePanelsFactoryImpl, ResultPanelsImpl}
import utils.constants.GUIConstants.*

import javax.swing.*
import java.awt.*

/**
 * This object represents the initial menu where the player can choose between Age Test and Brain
 * Training mode.
 */
class MenuView(controller: GameController):
  private val MENU_BUTTON_W_SCALE_FACTOR  = 0.4
  private val MENU_BUTTON_H_SCALE_FACTOR  = 0.08
  private val LAST_BUTTON_DISTANCE        = 120
  private val frame                       = new JFrame("Menù")
  private def showGameRulesDialog(): Unit =
    val textArea   = new JTextArea(RULES)
    textArea.setEditable(false)
    textArea.setLineWrap(true)
    textArea.setWrapStyleWord(true)
    textArea.setFont(PIXEL_FONT15)
    val scrollPane = new JScrollPane(textArea)
    val size       = Math.min(SCREEN_WIDTH, SCREEN_HEIGHT) / HALF_DIVISOR
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
    UIHelper.centerFrame(frame, 1)
    val buttonSize      =
      new Dimension(
        (frame.getSize.width * MENU_BUTTON_W_SCALE_FACTOR).toInt,
        (frame.getSize.height * MENU_BUTTON_H_SCALE_FACTOR).toInt
      )
    val backgroundPanel = new BackgroundImagePanel("src\\main\\resources\\MenuBackgroundImage.png")
    backgroundPanel.setLayout(new BorderLayout())
    val buttonPanel     = new JPanel()
    buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT))
    buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, BUTTON_DISTANCE))
    buttonPanel.setOpaque(false)
    val verticalPanel   = new JPanel()
    verticalPanel.setLayout(new BoxLayout(verticalPanel, BoxLayout.Y_AXIS))
    verticalPanel.setOpaque(false)
    val buttonsData     = Seq(
      (
        "Age Test",
        () =>
          frame.dispose()
          AgeTest(GamePanelsFactoryImpl(), ResultPanelsImpl()).show()
      ),
      (
        "Training",
        () =>
          frame.dispose()
          BrainTraining(ResultPanelsImpl()).show(GamePanelsFactoryImpl())
      ),
      ("Game Rules", () => showGameRulesDialog())
    )
    val components      = for ((btnData, idx) <- buttonsData.zipWithIndex) yield
      val button =
        UIHelper.createStyledButton(btnData._1, buttonSize, PIXEL_FONT25)
      button.addActionListener(_ => btnData._2())
      val strut  =
        if (idx < buttonsData.size - 1) Box.createVerticalStrut(BUTTON_DISTANCE)
        else Box.createVerticalStrut(LAST_BUTTON_DISTANCE)
      Seq(button, strut)
    components.flatten.foreach(verticalPanel.add)
    buttonPanel.add(verticalPanel)
    backgroundPanel.add(buttonPanel, BorderLayout.SOUTH)
    frame.getContentPane.add(backgroundPanel)
    frame.setVisible(true)
