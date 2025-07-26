package controllers

import models.*
import models.rightDirections.DirectionsLogic
import utils.MiniGames

import java.util.concurrent.atomic.AtomicInteger
import java.util.{Timer, TimerTask}
import javax.swing.SwingUtilities
import scala.util.Random

case class QuestionResult(responseTime: Long, isCorrect: Boolean)

case class GameStats(results: List[QuestionResult])

/**
 * This case class represents the controller of the game. It manages the game loop and the
 * communications between logics and views.
 * @param remainingMiniGames
 *   The mini-games not played yet
 * @param currentGame
 *   The mini-game playing
 * @param lastQuestion
 *   The last question of the mini-game
 * @param difficulty
 *   The difficulty of the level
 * @param rand
 *   The random variable for choosing the next mini-game
 * @param timer
 *   The timer of the mini-games
 * @param timeLeft
 *   The time passed
 * @param viewCallback
 *   The methods to call when an event occurs
 */
case class GameController(
    remainingMiniGames: List[MiniGameLogic] = List(FastCalcLogic, CountWordsLogic, DirectionsLogic),
    currentGame: Option[MiniGameLogic] = None,
    lastQuestion: Option[String] = None,
    difficulty: Int = 1,
    rand: Random = new Random(),
    timer: Option[Timer] = None,
    timeLeft: Int = 120,
    viewCallback: Option[GameViewCallback] = None,
    startTime: Long = 0
):

  private var results: List[QuestionResult] = List()

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

  /**
   * Choose in a random way the next mini-game.
   * @return
   *   a copy of the controller with the mini-game to play
   */
  def nextGame: GameController =
    if remainingMiniGames.isEmpty then
      timer.foreach(_.cancel())
      val finalController = this.copy(currentGame = None)
      viewCallback.foreach(_.onGameFinished(finalController))
      finalController
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
          case DirectionsLogic => MiniGames.RightDirections
        viewCallback.foreach(_.onGameChanged(gameEnum, this))
      case None       =>

  def chooseCurrentGame(gameMode: String): GameController =
    val game = gameMode match
      case "Fast Calc"        => Some(FastCalcLogic)
      case "Count Words"      => Some(CountWordsLogic)
      case "Right Directions" => Some(DirectionsLogic)
      case _                  => None
    this.copy(currentGame = game, timeLeft = 120)

  def getQuestion: (String, Long) =
    val generatedQuestion = currentGame.get.generateQuestion(difficulty)
    val startTime = System.currentTimeMillis()
    (generatedQuestion, startTime)

  def checkAnswer(answer: String): Boolean =
    val parsedAnswer = currentGame match
      case Some(FastCalcLogic) => answer.toInt
      case Some(CountWordsLogic) => answer.toInt
      //case Some(DirectionsLogic) => TODO: parse answer for directions game
      case _ => answer
    val isAnswerCorrect = currentGame.get.validateAnswer(lastQuestion.get, parsedAnswer)
    val elapsedTime = System.currentTimeMillis() - startTime
    results = QuestionResult(elapsedTime, isAnswerCorrect) :: results
    isAnswerCorrect

  def increaseDifficulty(): GameController =
    val newDifficulty = difficulty + 1
    val newQuestion   = currentGame.get.generateQuestion(newDifficulty)
    this.copy(difficulty = newDifficulty, lastQuestion = Some(newQuestion))

  def calculateBrainAge: Int = BrainAgeCalculator.calcBrainAge(GameStats(results.reverse))