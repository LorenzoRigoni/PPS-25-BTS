package views

import controllers.{GameController, GameViewCallback}
import models.{CountWordsLogic, FastCalcLogic}
import utils.MiniGames
import utils.MiniGames.{ColoredCount, CountWords, FastCalc, RightDirections, WordMemory}
import views.panels.{GamePanels, GamePanelsImpl, ResultPanels}

import javax.swing.*
import java.awt.*
import java.util.{Timer, TimerTask}
import java.util.concurrent.atomic.{AtomicInteger, AtomicReference}

/**
 * This class represents the view of the age test. The user will play 3 random mini-games.
 */
case class AgeTest(gamePanels: GamePanels, resultPanels: ResultPanels)
    extends BaseView
    with GameViewCallback:
  private val frame       = new JFrame("Let's play!")
  private val mainPanel   = new JPanel(new BorderLayout())
  private val timeLabel   = new JLabel("Time left: 120 seconds", SwingConstants.CENTER)
  private val centerPanel = new JPanel(new BorderLayout())

  /**
   * Show the age test view with a mini-game.
   * @param gamePanels
   *   the mini-game to play
   */
  def show(): Unit =
    frame.setBackground(whiteColor)
    centerFrame(frame, 1.5)

    timeLabel.setFont(pixelFont15)
    mainPanel.add(timeLabel, BorderLayout.NORTH)

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

  private def showFastCalc(controller: GameController): JPanel =
    gamePanels.fastCalcPanel(
      controller,
      nextController => updatePanel(showFastCalc(nextController))
    )

  private def showCountWords(controller: GameController): JPanel =
    gamePanels.countWordsPanel(
      controller,
      nextController => updatePanel(showCountWords(nextController))
    )

  private def showRightDirections(controller: GameController): JPanel =
    gamePanels.rightDirectionsPanel(
      controller,
      nextController => updatePanel(showRightDirections(nextController))
    )

  private def showColoredCount(controller: GameController): JPanel =
    gamePanels.coloredCountPanel(
      controller,
      nextController => updatePanel(showColoredCount(nextController))
    )

  private def showWordMemory(controller: GameController): JPanel =
    gamePanels.wordMemoryPanel(
      controller,
      nextController => updatePanel(showWordMemory(nextController))
    )

  override def onTimerUpdate(secondsLeft: Int): Unit =
    SwingUtilities.invokeLater(() => timeLabel.setText(s"Time left: $secondsLeft seconds"))

  override def onGameChanged(miniGame: MiniGames, controller: GameController): Unit =
    println(s"Controller ricevuto con ${controller.results.size} risultati")
    miniGame match
      case FastCalc        => updatePanel(showFastCalc(controller))
      case CountWords      => updatePanel(showCountWords(controller))
      case RightDirections => updatePanel(showRightDirections(controller))
      case ColoredCount    => updatePanel(showColoredCount(controller))
      case WordMemory      => updatePanel(showWordMemory(controller))

  override def onGameFinished(controller: GameController): Unit =
    SwingUtilities.invokeLater(() =>
      mainPanel.remove(timeLabel)
      centerPanel.removeAll()
      val brainAge = controller.calculateBrainAge
      val panel    = resultPanels.TestResultPanel(controller, brainAge)
      // val panel = resultPanels.GameResultPanel(controller, 4 ,1 , 115)
      centerPanel.add(panel, BorderLayout.CENTER)
      mainPanel.revalidate()
      mainPanel.repaint()
    )
