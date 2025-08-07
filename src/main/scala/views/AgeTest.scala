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
    val (questionController, question) = controller.getQuestion
    gamePanels.fastCalcPanel(
      questionController,
      nextController =>
        if nextController.isCurrentGameFinished then
          val updatedController = nextController.nextGame
          if updatedController.currentGame.isDefined then
            onGameChanged(updatedController.currentGame.get.gameId, updatedController)
        else updatePanel(showFastCalc(nextController)),
      question
    )

  private def showCountWords(controller: GameController): JPanel =
    val (questionController, question) = controller.getQuestion
    gamePanels.countWordsPanel(
      questionController,
      nextController =>
        if nextController.isCurrentGameFinished then
          val updatedController = nextController.nextGame
          if updatedController.currentGame.isDefined then
            onGameChanged(updatedController.currentGame.get.gameId, updatedController)
        else updatePanel(showCountWords(nextController)),
      question
    )

  private def showRightDirections(controller: GameController): JPanel =
    val (questionController, question) = controller.getQuestion
    gamePanels.rightDirectionsPanel(
      questionController,
      nextController =>
        if nextController.isCurrentGameFinished then
          val updatedController = nextController.nextGame
          if updatedController.currentGame.isDefined then
            onGameChanged(updatedController.currentGame.get.gameId, updatedController)
        else updatePanel(showRightDirections(nextController)),
      question
    )

  private def showColoredCount(controller: GameController): JPanel =
    val (questionController, question) = controller.getQuestion
    gamePanels.coloredCountPanel(
      questionController,
      nextController =>
        if nextController.isCurrentGameFinished then
          val updatedController = nextController.nextGame
          if updatedController.currentGame.isDefined then
            onGameChanged(updatedController.currentGame.get.gameId, updatedController)
        else updatePanel(showColoredCount(nextController)),
      question
    )

  private def showWordMemory(controller: GameController): JPanel =
    val (questionController, question) = controller.getQuestion
    gamePanels.wordMemoryPanel(
      questionController,
      nextController =>
        if nextController.isCurrentGameFinished then
          val updatedController = nextController.nextGame
          if updatedController.currentGame.isDefined then
            onGameChanged(updatedController.currentGame.get.gameId, updatedController)
        else updatePanel(showWordMemory(nextController)),
      question
    )

  override def onTimerUpdate(secondsLeft: Int): Unit =
    SwingUtilities.invokeLater(() => timeLabel.setText(s"Time left: $secondsLeft seconds"))

  override def onGameChanged(miniGame: MiniGames, controller: GameController): Unit =
    miniGame match
      case FastCalc        => updatePanel(showFastCalc(controller))
      case CountWords      => updatePanel(showCountWords(controller))
      case RightDirections => updatePanel(showRightDirections(controller))
      case ColoredCount    => updatePanel(showColoredCount(controller))
      case WordMemory      => updatePanel(showWordMemory(controller))

  override def onGameFinished(controller: GameController): Unit =
    SwingUtilities.invokeLater(() =>
      println(s"[onGameFinished] Risultati finali: ${controller.results.map(r => s"${r.responseTime}ms-${r.isCorrect}")}")
      mainPanel.remove(timeLabel)
      centerPanel.removeAll()
      val brainAge = controller.calculateBrainAge
      val panel    = resultPanels.TestResultPanel(controller, brainAge)
      // val panel = resultPanels.GameResultPanel(controller, 4 ,1 , 115)
      centerPanel.add(panel, BorderLayout.CENTER)
      mainPanel.revalidate()
      mainPanel.repaint()
    )
