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

  private def showFastCalc(controller: GameController): JPanel =
    val (questionController, question) = controller.getQuestion
    println(s"[showPanel] Controller utilizzato per creare i panel: ${questionController.results}")
    gamePanels.fastCalcPanel(
      questionController,
      nextController =>
        if nextController.isCurrentGameFinished then
          val updatedController = nextController.nextGame
          if updatedController.currentGame.isDefined then
            onGameChanged(updatedController.currentGame.get.getGameId, updatedController)
        else updatePanel(showFastCalc(nextController)),
      question
    )

  private def showCountWords(controller: GameController): JPanel =
    val (questionController, question) = controller.getQuestion
    println(s"[showPanel] Controller utilizzato per creare i panel: ${questionController.results}")
    gamePanels.countWordsPanel(
      questionController,
      nextController =>
        if nextController.isCurrentGameFinished then
          val updatedController = nextController.nextGame
          if updatedController.currentGame.isDefined then
            onGameChanged(updatedController.currentGame.get.getGameId, updatedController)
        else updatePanel(showCountWords(nextController)),
      question
    )

  private def showRightDirections(controller: GameController): JPanel =
    val (questionController, question) = controller.getQuestion
    println(s"[showPanel] Controller utilizzato per creare i panel: ${questionController.results}")
    gamePanels.rightDirectionsPanel(
      questionController,
      nextController =>
        if nextController.isCurrentGameFinished then
          val updatedController = nextController.nextGame
          if updatedController.currentGame.isDefined then
            onGameChanged(updatedController.currentGame.get.getGameId, updatedController)
        else updatePanel(showRightDirections(nextController)),
      question
    )

  private def showColoredCount(controller: GameController): JPanel =
    val (questionController, question) = controller.getQuestion
    println(s"[showPanel] Controller utilizzato per creare i panel: ${questionController.results}")
    gamePanels.coloredCountPanel(
      questionController,
      nextController =>
        if nextController.isCurrentGameFinished then
          val updatedController = nextController.nextGame
          if updatedController.currentGame.isDefined then
            onGameChanged(updatedController.currentGame.get.getGameId, updatedController)
        else updatePanel(showColoredCount(nextController)),
      question
    )

  private def showWordMemory(controller: GameController): JPanel =
    val (questionController, question) = controller.getQuestion
    println(s"[showPanel] Controller utilizzato per creare i panel: ${questionController.results}")
    gamePanels.wordMemoryPanel(
      questionController,
      nextController =>
        if nextController.isCurrentGameFinished then
          val updatedController = nextController.nextGame
          if updatedController.currentGame.isDefined then
            onGameChanged(updatedController.currentGame.get.getGameId, updatedController)
        else updatePanel(showWordMemory(nextController)),
      question
    )

  override def onGameChanged(miniGame: MiniGames, controller: GameController): Unit =
    miniGame match
      case FastCalc        => updatePanel(showFastCalc(controller))
      case CountWords      => updatePanel(showCountWords(controller))
      case RightDirections => updatePanel(showRightDirections(controller))
      case ColoredCount    => updatePanel(showColoredCount(controller))
      case WordMemory      => updatePanel(showWordMemory(controller))

  override def onGameFinished(controller: GameController): Unit =
    SwingUtilities.invokeLater(() =>
      println(s"[onGameFinished] Risultati finali: ${controller.results
          .map(r => s"${r.responseTime}ms-${r.isCorrect}")}")
      centerPanel.removeAll()
      val brainAge = controller.calculateBrainAge
      val panel    = resultPanels.TestResultPanel(controller, brainAge)
      centerPanel.add(panel, BorderLayout.CENTER)
      mainPanel.revalidate()
      mainPanel.repaint()
    )
