package views

import controllers.{GameController, GameViewCallback}
import models.*
import utils.{ColoredCountQuestion, MiniGames, Question, SimpleTextQuestion}
import utils.MiniGames.{CountWords, FastCalc, RightDirections, WordMemory}
import views.panels.{GamePanels, ResultPanels}
import utils.GUIConstants.*

import javax.swing.*
import java.awt.*

/**
 * This class represents the view of the age test. The user will play 3 random mini-games.
 */
case class AgeTest(gamePanels: GamePanels, resultPanels: ResultPanels)
    extends BaseView
    with GameViewCallback:
  private val frame       = new JFrame("Let's play!")
  private val mainPanel   = new JPanel(new BorderLayout())
  private val centerPanel = new JPanel(new BorderLayout())

  /**
   * Show the age test view with a mini-game.
   */
  def show(): Unit =
    frame.setBackground(whiteColor)
    centerFrame(frame, 1.5)
    mainPanel.add(centerPanel, BorderLayout.CENTER)
    frame.setContentPane(mainPanel)
    frame.setVisible(true)

    val initialController = GameController(viewCallback = Some(this)).nextGame
    initialController.currentGame.foreach(game => onGameChanged(game.getGameId, initialController))

  private def updatePanel(panel: JPanel): Unit =
    SwingUtilities.invokeLater(() => {
      centerPanel.removeAll()
      centerPanel.add(panel, BorderLayout.CENTER)
      centerPanel.revalidate()
      centerPanel.repaint()
    })

  private def showGame[Q <: Question](
      controller: GameController,
      panelFactory: (GameController, GameController => Unit, Q) => JPanel,
      miniGame: MiniGames,
      question: Q
  ): JPanel =
    panelFactory(
      controller,
      nextController => {
        if nextController.isCurrentGameFinished then
          val updatedController = nextController.nextGame
          updatedController.currentGame.foreach(game =>
            onGameChanged(game.getGameId, updatedController)
          )
        else
          onGameChanged(miniGame, nextController)
      },
      question
    )

  private def gamePanelsForSimpleText(
      miniGame: MiniGames
  ): (GameController, GameController => Unit, SimpleTextQuestion) => JPanel =
    miniGame match
      case FastCalc        => gamePanels.fastCalcPanel
      case CountWords      => gamePanels.countWordsPanel
      case RightDirections => gamePanels.rightDirectionsPanel
      case WordMemory      => gamePanels.wordMemoryPanel
      case _ => throw new IllegalArgumentException("This mini-game is not a SimpleTextQuestion")

  override def onGameChanged(miniGame: MiniGames, controller: GameController): Unit =
    val (questionController, question) = controller.getQuestion
    question match
      case q: SimpleTextQuestion   =>
        updatePanel(showGame(questionController, gamePanelsForSimpleText(miniGame), miniGame, q))
      case q: ColoredCountQuestion =>
        updatePanel(showGame(questionController, gamePanels.coloredCountPanel, miniGame, q))

  override def onGameFinished(controller: GameController): Unit =
    SwingUtilities.invokeLater(() =>
      centerPanel.removeAll()
      val brainAge = controller.calculateBrainAge
      val panel    = resultPanels.TestResultPanel(controller, brainAge)
      centerPanel.add(panel, BorderLayout.CENTER)
      mainPanel.revalidate()
      mainPanel.repaint()
    )
