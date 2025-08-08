package views

import controllers.{GameController, GameViewCallback}
import models.*
import utils.MiniGames
import utils.MiniGames.{ColoredCount, CountWords, FastCalc, RightDirections, WordMemory}
import views.panels.{GamePanels, ResultPanels}

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
   * @param gamePanels
   *   the mini-game to play
   */
  def show(): Unit =
    frame.setBackground(whiteColor)
    centerFrame(frame, 1.5)

    mainPanel.add(centerPanel, BorderLayout.CENTER)

    frame.setContentPane(mainPanel)
    frame.setVisible(true)

    val initialController = GameController(viewCallback = Some(this)).nextGame
    initialController.chooseNextGame()

  private def updatePanel(panel: JPanel): Unit =
    SwingUtilities.invokeLater(() => {
      centerPanel.removeAll()
      centerPanel.add(panel, BorderLayout.CENTER)
      centerPanel.revalidate()
      centerPanel.repaint()
    })

  private def showGame(
      controller: GameController,
      panelFactory: (GameController, GameController => Unit, String) => JPanel,
      miniGame: MiniGames
  ): JPanel =
    val (questionController, question) = controller.getQuestion
    panelFactory(
      questionController,
      nextController =>
        println(s"Controller ricevuto da submit: $nextController")
        if nextController.isCurrentGameFinished then
          val updatedController = nextController.nextGame
          println(
            s"Gioco finito, risultati finali: ${updatedController.results}"
          )
          if updatedController.currentGame.isDefined then
            onGameChanged(updatedController.currentGame.get.getGameId, updatedController)
        else updatePanel(showGame(nextController, panelFactory, miniGame))
      ,
      question
    )

  override def onGameChanged(miniGame: MiniGames, controller: GameController): Unit =
    val panelFactory = miniGame match
      case FastCalc        => gamePanels.fastCalcPanel
      case CountWords      => gamePanels.countWordsPanel
      case RightDirections => gamePanels.rightDirectionsPanel
      case ColoredCount    => gamePanels.coloredCountPanel
      case WordMemory      => gamePanels.wordMemoryPanel
    updatePanel(showGame(controller, panelFactory, miniGame))

  override def onGameFinished(controller: GameController): Unit =
    SwingUtilities.invokeLater(() =>
      centerPanel.removeAll()
      val brainAge = controller.calculateBrainAge
      val panel    = resultPanels.TestResultPanel(controller, brainAge)
      centerPanel.add(panel, BorderLayout.CENTER)
      mainPanel.revalidate()
      mainPanel.repaint()
    )
