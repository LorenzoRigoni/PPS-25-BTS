package views

import controllers.GameController
import views.panels.{GamePanels, GamePanelsImpl}

import javax.swing.*
import java.awt.*

/**
 * This object represents the view of the training mode. In this mode, the user can train his mind
 * with all the mini-games.
 */
case class BrainTraining(controller: GameController) extends BaseView:
  /**
   * Show the brain training view with a mini-game.
   *
   * @param gamePanels
   *   the panel of the mini-game chose
   */
  def show(controller: GameController, gamePanels: GamePanels): Unit =
    val frame           = new JFrame("Brain Testing")
    val buttonDimension = new Dimension(300, 50)
    val mainPanel       = new JPanel(new BorderLayout())
    val buttonPanel     = new JPanel()
    val centerPanel     = new JPanel()

    frame.setBackground(whiteColor)
    centerFrame(frame, 1.5)

    val buttons = Seq(
      createStyledButton(
        "Fast Calc",
        buttonDimension,
        pixelFont15,
        customBlueColor,
        whiteColor
      ) -> gamePanels.fastCalcPanel _,
      createStyledButton(
        "Count Words",
        buttonDimension,
        pixelFont15,
        customBlueColor,
        whiteColor
      ) -> gamePanels.countWordsPanel _,
      createStyledButton(
        "Right Directions",
        buttonDimension,
        pixelFont15,
        customBlueColor,
        whiteColor
      ) -> gamePanels.rightDirectionsPanel _
    )

    centerPanel.setLayout(new BorderLayout())

    buttons.foreach((button, panelSupplier) => {
      button.addActionListener(_ => {
        val updatedController = controller.chooseCurrentGame(button.getText)
        val updatedGamePanels = GamePanelsImpl()
        val updatedPanel = new JPanel()
        /*val updatedPanel = button.getText match
          case "Fast Calc"        => updatedGamePanels.fastCalcPanel(updatedController)
          case "Count Words"      => updatedGamePanels.countWordsPanel(updatedController)
          case "Right Directions" => updatedGamePanels.rightDirectionsPanel(updatedController)
          case _                  => new JPanel()*/
        mainPanel.remove(buttonPanel)
        centerPanel.removeAll()
        centerPanel.add(updatedPanel, BorderLayout.CENTER)
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
