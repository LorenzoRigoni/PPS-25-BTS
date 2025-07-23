package controllers

import models.*
import utils.MiniGames

import java.util.concurrent.atomic.AtomicInteger
import java.util.{Timer, TimerTask}
import javax.swing.SwingUtilities
import scala.util.Random

case class GameController(
    remainingMiniGames: List[MiniGameLogic] = List(FastCalcLogic, CountWordsLogic),
    currentGame: Option[MiniGameLogic] = None,
    lastQuestion: Option[String] = None,
    difficulty: Int = 1,
    rand: Random = new Random(),
    timer: Option[Timer] = None,
    timeLeft: Int = 120,
    viewCallback: Option[GameViewCallback] = None
):

  private def startTimer(): GameController =
    timer.foreach(_.cancel())
    val t       = new Timer()
    val seconds = new AtomicInteger(120)
    val task    = new TimerTask {
      override def run(): Unit =
        val remaining = seconds.decrementAndGet()
        viewCallback.foreach(_.onTimerUpdate(remaining))
        if remaining <= 0 then
          t.cancel()
          SwingUtilities.invokeLater(() => {
            val next = nextGame
            next.chooseNextGame()
          })
    }
    t.scheduleAtFixedRate(task, 1000, 1000)
    this.copy(timer = Some(t), timeLeft = 120)

  def nextGame: GameController =
    if remainingMiniGames.isEmpty then
      timer.foreach(_.cancel())
      viewCallback.foreach(_.onGameFinished())
      this.copy(currentGame = None)
    else
      val nextMiniGame   = remainingMiniGames(rand.between(0, remainingMiniGames.length))
      val updatedList    = remainingMiniGames.filter(m => m != nextMiniGame)
      val controllerCopy =
        this.copy(currentGame = Some(nextMiniGame), remainingMiniGames = updatedList)
      controllerCopy.chooseNextGame()
      controllerCopy.startTimer()

  private def chooseNextGame(): Unit =
    currentGame match
      case Some(game) =>
        val gameEnum = game match
          case FastCalcLogic   => MiniGames.FastCalc
          case CountWordsLogic => MiniGames.CountWords
        viewCallback.foreach(_.onGameChanged(gameEnum, this))
      case None       =>

  def chooseCurrentGame(gameMode: String): GameController =
    val game = gameMode match
      case "Fast Calc"        => Some(FastCalcLogic)
      case "Count Words"      => Some(CountWordsLogic)
      case "Right Directions" => None // TODO: modify with RightDirectionsLogic
      case _                  => None
    this.copy(currentGame = game, timeLeft = 120)

  def getQuestion: String =
    currentGame.get.generateQuestion(difficulty) // TODO: handle difficulty increase

  def checkAnswer(answer: String): Boolean =
    currentGame.get.validateAnswer(lastQuestion.get, answer.toInt)

  def increaseDifficulty(): GameController =
    val newDifficulty = difficulty + 1
    val newQuestion = currentGame.get.generateQuestion(newDifficulty)
    this.copy(difficulty = newDifficulty, lastQuestion = Some(newQuestion))
