package views

import controllers.GameController
import utils.MiniGames.{CountWords, FastCalc, RightDirections}
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
   * the panel of the mini-game chose
   */
  def show(initialController: GameController, gamePanels: GamePanels): Unit =
    val frame = new JFrame("Brain Testing")
    val buttonDimension = new Dimension(300, 50)
    val mainPanel = new JPanel(new BorderLayout())
    val buttonPanel = new JPanel()
    val centerPanel = new JPanel()
    val bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT))

    frame.setBackground(whiteColor)
    centerFrame(frame, 1.5)

    def loadGamePanel(controller: GameController, miniGameName: String): Unit =
      centerPanel.removeAll()
      val panel = miniGameName match
        case "Fast Calc" =>
          gamePanels.fastCalcPanel(
            controller,
            nextController => loadGamePanel(nextController, miniGameName)
          )
        case "Count Words" =>
          gamePanels.countWordsPanel(
            controller,
            nextController => loadGamePanel(nextController, miniGameName)
          )
        case "Right Directions" =>
          gamePanels.rightDirectionsPanel(
            controller,
            nextController => loadGamePanel(nextController, miniGameName)
          )
        case _ => new JPanel()
      centerPanel.add(panel, BorderLayout.CENTER)
      centerPanel.revalidate()
      centerPanel.repaint()
      mainPanel.remove(buttonPanel)

    val buttons = Seq(
      "Fast Calc",
      "Count Words",
      "Right Directions"
    )

    centerPanel.setLayout(new BorderLayout())

    buttons.foreach(name => {
      val button = createStyledButton(
        name,
        buttonDimension,
        pixelFont15,
        customBlueColor,
        whiteColor
      )
      button.addActionListener(_ => {
        val updatedController = name match
          case "Fast Calc" => initialController.chooseCurrentGame(FastCalc)
          case "Count Words" => initialController.chooseCurrentGame(CountWords)
          case "Right Directions" => initialController.chooseCurrentGame(RightDirections)
        loadGamePanel(updatedController, name)
      })
      button.setAlignmentX(Component.CENTER_ALIGNMENT)
      buttonPanel.add(Box.createVerticalStrut(40))
      buttonPanel.add(button)
    })

    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS))

    val backButton =
      createStyledButton("← Home", new Dimension(100, 30), pixelFont8, customBlueColor, whiteColor)
    backButton.addActionListener(_ => {
      frame.dispose()
      if mainPanel.isAncestorOf(buttonPanel) then MenuView.apply(GameController()).show()
      else BrainTraining.apply(GameController()).show(GameController(), GamePanelsImpl())
    })

    bottomPanel.add(backButton)

    mainPanel.add(buttonPanel, BorderLayout.NORTH)
    mainPanel.add(centerPanel, BorderLayout.CENTER)
    mainPanel.add(bottomPanel, BorderLayout.SOUTH)

    frame.setContentPane(mainPanel)
    frame.setVisible(true)
