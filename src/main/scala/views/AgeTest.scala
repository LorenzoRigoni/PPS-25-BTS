package views

import controllers.{GameController, GameViewCallback}
import models.*
import utils.{ColoredCountQuestion, MiniGames, Question, SimpleTextQuestion}
import utils.MiniGames.{CountWords, FastCalc, RightDirections, WordMemory}
import views.panels.{GamePanels, ResultPanels}
import utils.GUIConstants.*
import views.panels.GamePanelMapper.*

import javax.swing.*
import java.awt.*

/**
 * This class represents the view of the age test. The user will play 3 random mini-games.
 */
case class AgeTest(gamePanels: GamePanels, resultPanels: ResultPanels) extends GameViewCallback:
  private val frame          = new JFrame("Let's play!")
  private val mainPanel      = new JPanel(new BorderLayout())
  private val centerPanel    = new JPanel(new BorderLayout())
  private val simplePanelMap = simpleTextPanelMap(gamePanels)

  /**
   * Show the age test view with a mini-game.
   */
  def show(): Unit =
    frame.setBackground(Color.WHITE)
    UIHelper.centerFrame(frame, CENTER_FRAME_DIVISOR)
    mainPanel.add(centerPanel, BorderLayout.CENTER)
    frame.setContentPane(mainPanel)
    frame.setVisible(true)

    val initialController = GameController(viewCallback = Some(this)).nextGame
    initialController.currentGame.foreach(game => onGameChanged(game._2, initialController))

  private def updatePanel(panel: JPanel): Unit =
    SwingUtilities.invokeLater(() => UIHelper.centerPanel(centerPanel, panel))

  private def advanceGame(currentMiniGame: MiniGames)(nextController: GameController): Unit =
    if nextController.isCurrentGameFinished then
      val updatedController = nextController.nextGame
      updatedController.currentGame.foreach(game =>
        onGameChanged(game._2, updatedController)
      )
    else onGameChanged(currentMiniGame, nextController)

  private def showGame[Q <: Question](
      controller: GameController,
      panelFactory: (GameController, GameController => Unit, Q) => JPanel,
      miniGame: MiniGames,
      question: Q
  ): JPanel =
    panelFactory(controller, advanceGame(miniGame), question)

  private def gamePanelsForSimpleText(
      miniGame: MiniGames
  ): (GameController, GameController => Unit, SimpleTextQuestion) => JPanel =
    simplePanelMap.getOrElse(
      miniGame,
      throw new IllegalArgumentException(s"This mini-game is not a SimpleTextQuestion: $miniGame")
    )

  override def onGameChanged(miniGame: MiniGames, controller: GameController): Unit =
    val (questionController, question) = controller.getQuestion
    question match
      case q: SimpleTextQuestion   =>
        updatePanel(showGame(questionController, gamePanelsForSimpleText(miniGame), miniGame, q))
      case q: ColoredCountQuestion =>
        updatePanel(showGame(questionController, gamePanels.coloredCountPanel, miniGame, q))

  override def onGameFinished(controller: GameController): Unit =
    SwingUtilities.invokeLater(() =>
      val brainAge = controller.calculateBrainAge
      val panel    = resultPanels.TestResultPanel(controller, brainAge)
      UIHelper.centerPanel(centerPanel, panel)
    )
