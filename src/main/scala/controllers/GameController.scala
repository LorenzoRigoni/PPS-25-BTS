package controllers

import models.*
import models.rightDirections.RightDirectionsLogic
import utils.CountWordsConstants.{AGE_TEST_TURNS, DIFFICULTY_STEP}
import utils.FastCalcConstants.*
import utils.{FastCalcConstants, MiniGames}
import utils.MiniGames.{CountWords, FastCalc, RightDirections}

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
    remainingMiniGames: List[MiniGameLogic] = List(
      FastCalcLogic(FAST_CALC_TURNS, 0, FAST_CALC_DIFFICULTY_STEP),
      CountWordsLogic(AGE_TEST_TURNS, DIFFICULTY_STEP),
      //RightDirectionsLogic(1, 1) // TODO: create file with constants for RightDirections game
    ),
    currentGame: Option[MiniGameLogic] = None,
    lastQuestion: Option[String] = None,
    difficulty: Int = 1,
    rand: Random = new Random(),
    timer: Option[Timer] = None,
    timeLeft: Int = 60, // TODO: 120
    viewCallback: Option[GameViewCallback] = None,
    startTime: Long = 0
):

  private var results: List[QuestionResult] = List()

  private def startTimer(): GameController =
    timer.foreach(_.cancel())
    val t       = new Timer()
    val seconds = new AtomicInteger(60) // TODO: 120
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
    this.copy(timer = Some(t), timeLeft = 60) // TODO: 120

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
          case FastCalcLogic(_, _, _, _)  => MiniGames.FastCalc
          case CountWordsLogic(_, _, _, _)      => MiniGames.CountWords
          case RightDirectionsLogic(_, _, _, _) => MiniGames.RightDirections
        viewCallback.foreach(_.onGameChanged(gameEnum, this))
      case None       =>

  def chooseCurrentGame(gameMode: MiniGames): GameController =
    val game = gameMode match
      case FastCalc        => Some(FastCalcLogic(FAST_CALC_TURNS, 0, FAST_CALC_DIFFICULTY_STEP))
      case CountWords      => Some(CountWordsLogic(AGE_TEST_TURNS, DIFFICULTY_STEP))
      //case RightDirections => Some(RightDirectionsLogic(1, 1)) // TODO: use constants from utils
    this.copy(currentGame = game, timeLeft = 60) // TODO: 120

  def getQuestion: (String, Long) =
    val (gameLogic, generatedQuestion) = currentGame.get.generateQuestion
    val startTime                      = System.currentTimeMillis()
    (generatedQuestion, startTime)

  def checkAnswer(answer: String): Boolean =
    val parsedAnswer    = currentGame match
      case Some(FastCalcLogic(_, _, _, _))  => answer.toInt
      case Some(CountWordsLogic(_, _, _, _))      => answer.toInt
      case Some(RightDirectionsLogic(_, _, _, _)) => answer
      case _                                => answer
    val isAnswerCorrect = currentGame.get.validateAnswer(lastQuestion.get, parsedAnswer)
    val elapsedTime     = System.currentTimeMillis() - startTime
    results = QuestionResult(elapsedTime, isAnswerCorrect) :: results
    isAnswerCorrect

  def increaseDifficulty(): GameController =
    val newDifficulty            = difficulty + 1
    val (gameLogic, newQuestion) = currentGame.get.generateQuestion
    this.copy(difficulty = newDifficulty, lastQuestion = Some(newQuestion))

  def calculateBrainAge: Int = BrainAgeCalculator.calcBrainAge(GameStats(results.reverse))
