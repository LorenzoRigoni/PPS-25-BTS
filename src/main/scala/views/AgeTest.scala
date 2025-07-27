package views

import controllers.{GameController, GameViewCallback}
import models.{CountWordsLogic, FastCalcLogic}
import utils.MiniGames
import utils.MiniGames.{CountWords, FastCalc, RightDirections}
import views.panels.{GamePanels, GamePanelsImpl, ResultPanels}

import javax.swing.*
import java.awt.*
import java.util.{Timer, TimerTask}
import java.util.concurrent.atomic.{AtomicInteger, AtomicReference}

/**
 * This class represents the view of the age test. The user will play 3 random mini-games.
 */
case class AgeTest(gamePanels: GamePanels, resultPanels: ResultPanels) extends BaseView with GameViewCallback:
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

    GameController(viewCallback = Some(this)).nextGame

  private def showFastCalc(controller: GameController): JPanel =
    gamePanels.fastCalcPanel(
      controller,
      nextController => {
        SwingUtilities.invokeLater(() => {
          centerPanel.removeAll()
          val panel = showFastCalc(nextController)
          centerPanel.add(panel, BorderLayout.CENTER)
          centerPanel.revalidate()
          centerPanel.repaint()
        })
      }
    )

  private def showCountWords(controller: GameController): JPanel =
    gamePanels.countWordsPanel(
      controller,
      nextController => {
        SwingUtilities.invokeLater(() => {
          centerPanel.removeAll()
          val panel = showCountWords(nextController)
          centerPanel.add(panel, BorderLayout.CENTER)
          centerPanel.revalidate()
          centerPanel.repaint()
        })
      }
    )

  private def showRightDirections(controller: GameController): JPanel =
    gamePanels.rightDirectionsPanel(
      controller,
      nextController => {
        SwingUtilities.invokeLater(() => {
          centerPanel.removeAll()
          val panel = showRightDirections(nextController)
          centerPanel.add(panel, BorderLayout.CENTER)
          centerPanel.revalidate()
          centerPanel.repaint()
        })
      }
    )

  override def onTimerUpdate(secondsLeft: Int): Unit =
    SwingUtilities.invokeLater(() => timeLabel.setText(s"Time left: $secondsLeft seconds"))

  override def onGameChanged(miniGame: MiniGames, controller: GameController): Unit =
    SwingUtilities.invokeLater(() => {
      centerPanel.removeAll()
      val panel = miniGame match
        case FastCalc        => showFastCalc(controller)
        case CountWords      => showCountWords(controller)
        case RightDirections => showRightDirections(controller)

      centerPanel.add(panel, BorderLayout.CENTER)
      centerPanel.revalidate()
      centerPanel.repaint()
    })

  override def onGameFinished(controller: GameController): Unit =
    SwingUtilities.invokeLater(() =>
      mainPanel.remove(timeLabel)
      centerPanel.removeAll()
      val brainAge = controller.calculateBrainAge
      val panel = resultPanels.TestResultPanel(controller, brainAge)
      centerPanel.add(panel, BorderLayout.CENTER)
      mainPanel.revalidate()
      mainPanel.repaint()
    )
